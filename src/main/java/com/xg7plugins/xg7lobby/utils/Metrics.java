package com.xg7plugins.xg7lobby.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.zip.GZIPOutputStream;
import javax.net.ssl.HttpsURLConnection;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Metrics {
    private final Plugin plugin;

    private final MetricsBase metricsBase;

    public static Metrics getMetrics(JavaPlugin pl) {
        return new Metrics(pl, 20981);
    }

    public Metrics(JavaPlugin plugin, int serviceId) {
        this.plugin = (Plugin)plugin;
        File bStatsFolder = new File(plugin.getDataFolder().getParentFile(), "bStats");
        File configFile = new File(bStatsFolder, "config.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        if (!config.isSet("serverUuid")) {
            config.addDefault("enabled", Boolean.valueOf(true));
            config.addDefault("serverUuid", UUID.randomUUID().toString());
            config.addDefault("logFailedRequests", Boolean.valueOf(false));
            config.addDefault("logSentData", Boolean.valueOf(false));
            config.addDefault("logResponseStatusText", Boolean.valueOf(false));
            config
                    .options()
                    .header("bStats (https://bStats.org) collects some basic information for plugin authors, like how\nmany people use their plugin and their total player count. It's recommended to keep bStats\nenabled, but if you're not comfortable with this, you can turn this setting off. There is no\nperformance penalty associated with having metrics enabled, and data sent to bStats is fully\nanonymous.")

                    .copyDefaults(true);
            try {
                config.save(configFile);
            } catch (IOException iOException) {}
        }
        boolean enabled = config.getBoolean("enabled", true);
        String serverUUID = config.getString("serverUuid");
        boolean logErrors = config.getBoolean("logFailedRequests", false);
        boolean logSentData = config.getBoolean("logSentData", false);
        boolean logResponseStatusText = config.getBoolean("logResponseStatusText", false);
        Objects.requireNonNull(plugin);
        this.metricsBase = new MetricsBase("bukkit", serverUUID, serviceId, enabled, this::appendPlatformData, this::appendServiceData, submitDataTask -> Bukkit.getScheduler().runTask((Plugin)plugin, submitDataTask), plugin::isEnabled, (message, error) -> this.plugin.getLogger().log(Level.WARNING, message, error), message -> this.plugin.getLogger().log(Level.INFO, message), logErrors, logSentData, logResponseStatusText);
    }

    public void shutdown() {
        this.metricsBase.shutdown();
    }

    public void addCustomChart(CustomChart chart) {
        this.metricsBase.addCustomChart(chart);
    }

    private void appendPlatformData(JsonObjectBuilder builder) {
        builder.appendField("playerAmount", getPlayerAmount());
        builder.appendField("onlineMode", Bukkit.getOnlineMode() ? 1 : 0);
        builder.appendField("bukkitVersion", Bukkit.getVersion());
        builder.appendField("bukkitName", Bukkit.getName());
        builder.appendField("javaVersion", System.getProperty("java.version"));
        builder.appendField("osName", System.getProperty("os.name"));
        builder.appendField("osArch", System.getProperty("os.arch"));
        builder.appendField("osVersion", System.getProperty("os.version"));
        builder.appendField("coreCount", Runtime.getRuntime().availableProcessors());
    }

    private void appendServiceData(JsonObjectBuilder builder) {
        builder.appendField("pluginVersion", this.plugin.getDescription().getVersion());
    }

    private int getPlayerAmount() {
        try {
            Method onlinePlayersMethod = Class.forName("org.bukkit.Server").getMethod("getOnlinePlayers", new Class[0]);
            return onlinePlayersMethod.getReturnType().equals(Collection.class) ? (
                    (Collection)onlinePlayersMethod.invoke(Bukkit.getServer(), new Object[0])).size() : (
                    (Player[])onlinePlayersMethod.invoke(Bukkit.getServer(), new Object[0])).length;
        } catch (Exception e) {
            return Bukkit.getOnlinePlayers().size();
        }
    }

    public static class MetricsBase {
        public static final String METRICS_VERSION = "3.0.2";

        private static final String REPORT_URL = "https://bStats.org/api/v2/data/%s";

        private final ScheduledExecutorService scheduler;

        private final String platform;

        private final String serverUuid;

        private final int serviceId;

        private final Consumer<Metrics.JsonObjectBuilder> appendPlatformDataConsumer;

        private final Consumer<Metrics.JsonObjectBuilder> appendServiceDataConsumer;

        private final Consumer<Runnable> submitTaskConsumer;

        private final Supplier<Boolean> checkServiceEnabledSupplier;

        private final BiConsumer<String, Throwable> errorLogger;

        private final Consumer<String> infoLogger;

        private final boolean logErrors;

        private final boolean logSentData;

        private final boolean logResponseStatusText;

        private final Set<Metrics.CustomChart> customCharts = new HashSet<>();

        private final boolean enabled;

        public MetricsBase(String platform, String serverUuid, int serviceId, boolean enabled, Consumer<Metrics.JsonObjectBuilder> appendPlatformDataConsumer, Consumer<Metrics.JsonObjectBuilder> appendServiceDataConsumer, Consumer<Runnable> submitTaskConsumer, Supplier<Boolean> checkServiceEnabledSupplier, BiConsumer<String, Throwable> errorLogger, Consumer<String> infoLogger, boolean logErrors, boolean logSentData, boolean logResponseStatusText) {
            ScheduledThreadPoolExecutor scheduler = new ScheduledThreadPoolExecutor(1, task -> new Thread(task, "bStats-Metrics"));
            scheduler.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
            this.scheduler = scheduler;
            this.platform = platform;
            this.serverUuid = serverUuid;
            this.serviceId = serviceId;
            this.enabled = enabled;
            this.appendPlatformDataConsumer = appendPlatformDataConsumer;
            this.appendServiceDataConsumer = appendServiceDataConsumer;
            this.submitTaskConsumer = submitTaskConsumer;
            this.checkServiceEnabledSupplier = checkServiceEnabledSupplier;
            this.errorLogger = errorLogger;
            this.infoLogger = infoLogger;
            this.logErrors = logErrors;
            this.logSentData = logSentData;
            this.logResponseStatusText = logResponseStatusText;
            checkRelocation();
            if (enabled)
                startSubmitting();
        }

        public void addCustomChart(Metrics.CustomChart chart) {
            this.customCharts.add(chart);
        }

        public void shutdown() {
            this.scheduler.shutdown();
        }

        private void startSubmitting() {
            Runnable submitTask = () -> {
                if (!this.enabled || !((Boolean)this.checkServiceEnabledSupplier.get()).booleanValue()) {
                    this.scheduler.shutdown();
                    return;
                }
                if (this.submitTaskConsumer != null) {
                    this.submitTaskConsumer.accept(this::submitData);
                } else {
                    submitData();
                }
            };
            long initialDelay = (long)(60000.0D * (3.0D + Math.random() * 3.0D));
            long secondDelay = (long)(60000.0D * Math.random() * 30.0D);
            this.scheduler.schedule(submitTask, initialDelay, TimeUnit.MILLISECONDS);
            this.scheduler.scheduleAtFixedRate(submitTask, initialDelay + secondDelay, 1800000L, TimeUnit.MILLISECONDS);
        }

        private void submitData() {
            Metrics.JsonObjectBuilder baseJsonBuilder = new Metrics.JsonObjectBuilder();
            this.appendPlatformDataConsumer.accept(baseJsonBuilder);
            Metrics.JsonObjectBuilder serviceJsonBuilder = new Metrics.JsonObjectBuilder();
            this.appendServiceDataConsumer.accept(serviceJsonBuilder);
            Metrics.JsonObjectBuilder.JsonObject[] chartData = (Metrics.JsonObjectBuilder.JsonObject[])this.customCharts.stream().map(customChart -> customChart.getRequestJsonObject(this.errorLogger, this.logErrors)).filter(Objects::nonNull).toArray(x$0 -> new Metrics.JsonObjectBuilder.JsonObject[x$0]);
            serviceJsonBuilder.appendField("id", this.serviceId);
            serviceJsonBuilder.appendField("customCharts", chartData);
            baseJsonBuilder.appendField("service", serviceJsonBuilder.build());
            baseJsonBuilder.appendField("serverUUID", this.serverUuid);
            baseJsonBuilder.appendField("metricsVersion", "3.0.2");
            Metrics.JsonObjectBuilder.JsonObject data = baseJsonBuilder.build();
            this.scheduler.execute(() -> {
                try {
                    sendData(data);
                } catch (Exception e) {
                    if (this.logErrors)
                        this.errorLogger.accept("Could not submit bStats metrics data", e);
                }
            });
        }

        private void sendData(Metrics.JsonObjectBuilder.JsonObject data) throws Exception {
            if (this.logSentData)
                this.infoLogger.accept("Sent bStats metrics data: " + data.toString());
            String url = String.format("https://bStats.org/api/v2/data/%s", new Object[] { this.platform });
            HttpsURLConnection connection = (HttpsURLConnection)(new URL(url)).openConnection();
            byte[] compressedData = compress(data.toString());
            connection.setRequestMethod("POST");
            connection.addRequestProperty("Accept", "application/json");
            connection.addRequestProperty("Connection", "close");
            connection.addRequestProperty("Content-Encoding", "gzip");
            connection.addRequestProperty("Content-Length", String.valueOf(compressedData.length));
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("User-Agent", "Metrics-Service/1");
            connection.setDoOutput(true);
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            try {
                outputStream.write(compressedData);
                outputStream.close();
            } catch (Throwable throwable) {
                try {
                    outputStream.close();
                } catch (Throwable throwable1) {
                    throwable.addSuppressed(throwable1);
                }
                throw throwable;
            }
            StringBuilder builder = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            try {
                String line;
                while ((line = bufferedReader.readLine()) != null)
                    builder.append(line);
                bufferedReader.close();
            } catch (Throwable throwable) {
                try {
                    bufferedReader.close();
                } catch (Throwable throwable1) {
                    throwable.addSuppressed(throwable1);
                }
                throw throwable;
            }
            if (this.logResponseStatusText)
                this.infoLogger.accept("Sent data to bStats and received response: " + builder);
        }

        private void checkRelocation() {
            if (System.getProperty("bstats.relocatecheck") == null ||
                    !System.getProperty("bstats.relocatecheck").equals("false")) {
                String defaultPackage = new String(new byte[] { 111, 114, 103, 46, 98, 115, 116, 97, 116, 115 });
                String examplePackage = new String(new byte[] {
                        121, 111, 117, 114, 46, 112, 97, 99, 107, 97,
                        103, 101 });
                if (MetricsBase.class.getPackage().getName().startsWith(defaultPackage) || MetricsBase.class
                        .getPackage().getName().startsWith(examplePackage))
                    throw new IllegalStateException("bStats Metrics class has not been relocated correctly!");
            }
        }

        private static byte[] compress(String str) throws IOException {
            if (str == null)
                return null;
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(outputStream);
            try {
                gzip.write(str.getBytes(StandardCharsets.UTF_8));
                gzip.close();
            } catch (Throwable throwable) {
                try {
                    gzip.close();
                } catch (Throwable throwable1) {
                    throwable.addSuppressed(throwable1);
                }
                throw throwable;
            }
            return outputStream.toByteArray();
        }
    }

    public static class SimplePie extends CustomChart {
        private final Callable<String> callable;

        public SimplePie(String chartId, Callable<String> callable) {
            super(chartId);
            this.callable = callable;
        }

        protected Metrics.JsonObjectBuilder.JsonObject getChartData() throws Exception {
            String value = this.callable.call();
            if (value == null || value.isEmpty())
                return null;
            return (new Metrics.JsonObjectBuilder()).appendField("value", value).build();
        }
    }

    public static class MultiLineChart extends CustomChart {
        private final Callable<Map<String, Integer>> callable;

        public MultiLineChart(String chartId, Callable<Map<String, Integer>> callable) {
            super(chartId);
            this.callable = callable;
        }

        protected Metrics.JsonObjectBuilder.JsonObject getChartData() throws Exception {
            Metrics.JsonObjectBuilder valuesBuilder = new Metrics.JsonObjectBuilder();
            Map<String, Integer> map = this.callable.call();
            if (map == null || map.isEmpty())
                return null;
            boolean allSkipped = true;
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                if (((Integer)entry.getValue()).intValue() == 0)
                    continue;
                allSkipped = false;
                valuesBuilder.appendField(entry.getKey(), ((Integer)entry.getValue()).intValue());
            }
            if (allSkipped)
                return null;
            return (new Metrics.JsonObjectBuilder()).appendField("values", valuesBuilder.build()).build();
        }
    }

    public static class AdvancedPie extends CustomChart {
        private final Callable<Map<String, Integer>> callable;

        public AdvancedPie(String chartId, Callable<Map<String, Integer>> callable) {
            super(chartId);
            this.callable = callable;
        }

        protected Metrics.JsonObjectBuilder.JsonObject getChartData() throws Exception {
            Metrics.JsonObjectBuilder valuesBuilder = new Metrics.JsonObjectBuilder();
            Map<String, Integer> map = this.callable.call();
            if (map == null || map.isEmpty())
                return null;
            boolean allSkipped = true;
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                if (((Integer)entry.getValue()).intValue() == 0)
                    continue;
                allSkipped = false;
                valuesBuilder.appendField(entry.getKey(), ((Integer)entry.getValue()).intValue());
            }
            if (allSkipped)
                return null;
            return (new Metrics.JsonObjectBuilder()).appendField("values", valuesBuilder.build()).build();
        }
    }

    public static class SimpleBarChart extends CustomChart {
        private final Callable<Map<String, Integer>> callable;

        public SimpleBarChart(String chartId, Callable<Map<String, Integer>> callable) {
            super(chartId);
            this.callable = callable;
        }

        protected Metrics.JsonObjectBuilder.JsonObject getChartData() throws Exception {
            Metrics.JsonObjectBuilder valuesBuilder = new Metrics.JsonObjectBuilder();
            Map<String, Integer> map = this.callable.call();
            if (map == null || map.isEmpty())
                return null;
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                valuesBuilder.appendField(entry.getKey(), new int[] { ((Integer)entry.getValue()).intValue() });
            }
            return (new Metrics.JsonObjectBuilder()).appendField("values", valuesBuilder.build()).build();
        }
    }

    public static class AdvancedBarChart extends CustomChart {
        private final Callable<Map<String, int[]>> callable;

        public AdvancedBarChart(String chartId, Callable<Map<String, int[]>> callable) {
            super(chartId);
            this.callable = callable;
        }

        protected Metrics.JsonObjectBuilder.JsonObject getChartData() throws Exception {
            Metrics.JsonObjectBuilder valuesBuilder = new Metrics.JsonObjectBuilder();
            Map<String, int[]> map = this.callable.call();
            if (map == null || map.isEmpty())
                return null;
            boolean allSkipped = true;
            for (Map.Entry<String, int[]> entry : map.entrySet()) {
                if (((int[])entry.getValue()).length == 0)
                    continue;
                allSkipped = false;
                valuesBuilder.appendField(entry.getKey(), entry.getValue());
            }
            if (allSkipped)
                return null;
            return (new Metrics.JsonObjectBuilder()).appendField("values", valuesBuilder.build()).build();
        }
    }

    public static class DrilldownPie extends CustomChart {
        private final Callable<Map<String, Map<String, Integer>>> callable;

        public DrilldownPie(String chartId, Callable<Map<String, Map<String, Integer>>> callable) {
            super(chartId);
            this.callable = callable;
        }

        public Metrics.JsonObjectBuilder.JsonObject getChartData() throws Exception {
            Metrics.JsonObjectBuilder valuesBuilder = new Metrics.JsonObjectBuilder();
            Map<String, Map<String, Integer>> map = this.callable.call();
            if (map == null || map.isEmpty())
                return null;
            boolean reallyAllSkipped = true;
            for (Map.Entry<String, Map<String, Integer>> entryValues : map.entrySet()) {
                Metrics.JsonObjectBuilder valueBuilder = new Metrics.JsonObjectBuilder();
                boolean allSkipped = true;
                for (Map.Entry<String, Integer> valueEntry : (Iterable<Map.Entry<String, Integer>>)((Map)map.get(entryValues.getKey())).entrySet()) {
                    valueBuilder.appendField(valueEntry.getKey(), ((Integer)valueEntry.getValue()).intValue());
                    allSkipped = false;
                }
                if (!allSkipped) {
                    reallyAllSkipped = false;
                    valuesBuilder.appendField(entryValues.getKey(), valueBuilder.build());
                }
            }
            if (reallyAllSkipped)
                return null;
            return (new Metrics.JsonObjectBuilder()).appendField("values", valuesBuilder.build()).build();
        }
    }

    public static abstract class CustomChart {
        private final String chartId;

        protected CustomChart(String chartId) {
            if (chartId == null)
                throw new IllegalArgumentException("chartId must not be null");
            this.chartId = chartId;
        }

        public Metrics.JsonObjectBuilder.JsonObject getRequestJsonObject(BiConsumer<String, Throwable> errorLogger, boolean logErrors) {
            Metrics.JsonObjectBuilder builder = new Metrics.JsonObjectBuilder();
            builder.appendField("chartId", this.chartId);
            try {
                Metrics.JsonObjectBuilder.JsonObject data = getChartData();
                if (data == null)
                    return null;
                builder.appendField("data", data);
            } catch (Throwable t) {
                if (logErrors)
                    errorLogger.accept("Failed to get data for custom chart with id " + this.chartId, t);
                return null;
            }
            return builder.build();
        }

        protected abstract Metrics.JsonObjectBuilder.JsonObject getChartData() throws Exception;
    }

    public static class SingleLineChart extends CustomChart {
        private final Callable<Integer> callable;

        public SingleLineChart(String chartId, Callable<Integer> callable) {
            super(chartId);
            this.callable = callable;
        }

        protected Metrics.JsonObjectBuilder.JsonObject getChartData() throws Exception {
            int value = ((Integer)this.callable.call()).intValue();
            if (value == 0)
                return null;
            return (new Metrics.JsonObjectBuilder()).appendField("value", value).build();
        }
    }

    public static class JsonObjectBuilder {
        private StringBuilder builder = new StringBuilder();

        private boolean hasAtLeastOneField = false;

        public JsonObjectBuilder() {
            this.builder.append("{");
        }

        public JsonObjectBuilder appendNull(String key) {
            appendFieldUnescaped(key, "null");
            return this;
        }

        public JsonObjectBuilder appendField(String key, String value) {
            if (value == null)
                throw new IllegalArgumentException("JSON value must not be null");
            appendFieldUnescaped(key, "\"" + escape(value) + "\"");
            return this;
        }

        public JsonObjectBuilder appendField(String key, int value) {
            appendFieldUnescaped(key, String.valueOf(value));
            return this;
        }

        public JsonObjectBuilder appendField(String key, JsonObject object) {
            if (object == null)
                throw new IllegalArgumentException("JSON object must not be null");
            appendFieldUnescaped(key, object.toString());
            return this;
        }

        public JsonObjectBuilder appendField(String key, String[] values) {
            if (values == null)
                throw new IllegalArgumentException("JSON values must not be null");
            String escapedValues = Arrays.<String>stream(values).map(value -> "\"" + escape(value) + "\"").collect(Collectors.joining(","));
            appendFieldUnescaped(key, "[" + escapedValues + "]");
            return this;
        }

        public JsonObjectBuilder appendField(String key, int[] values) {
            if (values == null)
                throw new IllegalArgumentException("JSON values must not be null");
            String escapedValues = Arrays.stream(values).<CharSequence>mapToObj(String::valueOf).collect(Collectors.joining(","));
            appendFieldUnescaped(key, "[" + escapedValues + "]");
            return this;
        }

        public JsonObjectBuilder appendField(String key, JsonObject[] values) {
            if (values == null)
                throw new IllegalArgumentException("JSON values must not be null");
            String escapedValues = Arrays.<JsonObject>stream(values).map(JsonObject::toString).collect(Collectors.joining(","));
            appendFieldUnescaped(key, "[" + escapedValues + "]");
            return this;
        }

        private void appendFieldUnescaped(String key, String escapedValue) {
            if (this.builder == null)
                throw new IllegalStateException("JSON has already been built");
            if (key == null)
                throw new IllegalArgumentException("JSON key must not be null");
            if (this.hasAtLeastOneField)
                this.builder.append(",");
            this.builder.append("\"").append(escape(key)).append("\":").append(escapedValue);
            this.hasAtLeastOneField = true;
        }

        public JsonObject build() {
            if (this.builder == null)
                throw new IllegalStateException("JSON has already been built");
            JsonObject object = new JsonObject(this.builder.append("}").toString());
            this.builder = null;
            return object;
        }

        private static String escape(String value) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < value.length(); i++) {
                char c = value.charAt(i);
                if (c == '"') {
                    builder.append("\\\"");
                } else if (c == '\\') {
                    builder.append("\\\\");
                } else if (c <= '\017') {
                    builder.append("\\u000").append(Integer.toHexString(c));
                } else if (c <= '\037') {
                    builder.append("\\u00").append(Integer.toHexString(c));
                } else {
                    builder.append(c);
                }
            }
            return builder.toString();
        }

        public static class JsonObject {
            private final String value;

            private JsonObject(String value) {
                this.value = value;
            }

            public String toString() {
                return this.value;
            }
        }
    }
}

