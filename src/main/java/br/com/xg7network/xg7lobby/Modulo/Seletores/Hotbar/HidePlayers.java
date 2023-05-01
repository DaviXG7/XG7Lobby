package br.com.xg7network.xg7lobby.Modulo.Seletores.Hotbar;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import br.com.xg7network.xg7lobby.XG7Lobby;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static br.com.xg7network.xg7lobby.XG7Lobby.mensagem;
import static br.com.xg7network.xg7lobby.XG7Lobby.seletor;
import static br.com.xg7network.xg7lobby.Modulo.Seletores.InventoryManager.addMeta;

public class HidePlayers implements Listener  {

    private XG7Lobby pl;

    int verf = 0;

    public HidePlayers (XG7Lobby pl) {
        this.pl = pl;
    }

    private List<UUID> vanished = new ArrayList<>();

    public static List<String> HPlore = seletor.getSelector().getStringList("EsconderJogadores.lore");

    int primeiraVez = 0;

    public static int HPslot = seletor.getSelector().getInt("EsconderJogadores.slot") - 1;

    public static ItemStack HPitemAtivado = new ItemStack(Material.valueOf(seletor.getSelector().getString("EsconderJogadores.item_ativado")));
    public static ItemMeta HPmetaAtivado = HPitemAtivado.getItemMeta();
    public static ItemStack HPitemDesativado = new ItemStack(Material.valueOf(seletor.getSelector().getString("EsconderJogadores.item_desativado")));
    public static ItemMeta HPmetaDesativado = HPitemDesativado.getItemMeta();

    private Cache<UUID, Long> cooldown = CacheBuilder.newBuilder().expireAfterWrite(seletor.getSelector().getInt("EsconderJogadores.Cooldown"), TimeUnit.SECONDS).build();

    @EventHandler
    public void giveItem(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (pl.getConfig().getStringList("mundos_ativados").contains(p.getWorld().getName())) {
            if (seletor.getSelector().getBoolean("EsconderJogadores.ativado")) {
                addMeta();

                if (!p.hasPermission("xg7lobby.admin") && seletor.getSelector().getBoolean("GanharItens.QuandoEntrar")) {
                    p.getInventory().setItem(HPslot, HPitemAtivado);
                } else if (p.hasPermission("xg7lobby.admin") && seletor.getSelector().getBoolean("GanharItens.QuandoEntrar") && seletor.getSelector().getBoolean("PADMREIESSCSI")) {
                    p.getInventory().addItem(HPitemAtivado);
                }
            }
        }
    }



    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (seletor.getSelector().getBoolean("EsconderJogadores.ativado")) {
            if (HPitemAtivado.getItemMeta() != null && primeiraVez == 0) {
                addMeta();
                primeiraVez++;
            }
            Player p = e.getPlayer();
            if (e.getItem() != null && (e.getItem().equals(HPitemAtivado) || e.getItem().equals(HPitemDesativado))) {
                if (pl.getConfig().getStringList("mundos_ativados").contains(p.getWorld().getName())) {
                    if (p.getInventory().getItemInHand().getType().equals(Material.valueOf(seletor.getSelector().getString("EsconderJogadores.item_ativado"))) ||
                            p.getInventory().getItemInHand().getType().equals(Material.valueOf(seletor.getSelector().getString("EsconderJogadores.item_desativado")))) {
                        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
                            if (!cooldown.asMap().containsKey(p.getUniqueId()) || cooldown.asMap().get(p.getUniqueId()) <= System.currentTimeMillis()) {
                                long segundos = seletor.getSelector().getInt("EsconderJogadores.Cooldown");
                                cooldown.put(p.getUniqueId(), System.currentTimeMillis() + segundos * 1000L);

                                if (e.getItem().equals(HPitemAtivado)) {
                                    if (verf == 0) {
                                        if (!vanished.contains(p.getUniqueId())) {
                                            int slot = p.getInventory().getHeldItemSlot();
                                            if (p.hasPermission("xg7lobby.admin")) {
                                                p.getInventory().setItem(slot, HPitemDesativado);
                                            } else {
                                                p.getInventory().setItem(HPslot, HPitemDesativado);
                                            }
                                            vanished.add(p.getUniqueId());
                                            for (Player target : Bukkit.getOnlinePlayers()) {
                                                target.hidePlayer(p);
                                            }
                                            if (Bukkit.getServer().getVersion().contains("1.10")) {
                                                if (mensagem.getMessage().getBoolean("AvisoEmActionBars")) {
                                                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("adsf"));
                                                    ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
                                                    executorService.scheduleAtFixedRate(() -> {

                                                        long distancia = cooldown.asMap().get(p.getUniqueId()) - System.currentTimeMillis();

                                                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText((seletor.getSelector().getString("EsconderJogadores.Mensagem_EmCooldownRegressiva").replace("&", "§").replace("[SEGUNDOS]", String.valueOf(TimeUnit.MILLISECONDS.toSeconds(distancia))))));
                                                    }, 0, 1, TimeUnit.SECONDS);
                                                } else {
                                                    p.sendMessage(seletor.getSelector().getString("EsconderJogadores.Mensagem_On").replace("&", "§"));
                                                }
                                            } else {
                                                p.sendMessage(seletor.getSelector().getString("EsconderJogadores.Mensagem_On").replace("&", "§"));
                                            }
                                        }
                                        verf++;
                                    }
                                } else if (e.getItem().equals(HPitemDesativado) && verf == 1) {
                                    if (vanished.contains(p.getUniqueId())) {
                                        int slot = p.getInventory().getHeldItemSlot();
                                        p.getInventory().clear(slot);
                                        if (p.hasPermission("xg7lobby.admin")) {
                                            p.getInventory().setItem(slot, HPitemAtivado);
                                        } else {
                                            p.getInventory().setItem(HPslot, HPitemAtivado);
                                        }
                                        vanished.remove(p.getUniqueId());
                                        for (Player target : Bukkit.getOnlinePlayers()) {
                                            target.showPlayer(p);
                                        }
                                        if (Bukkit.getVersion().contains("1.10")) {
                                            if (mensagem.getMessage().getBoolean("AvisoEmActionBars")) {
                                                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(seletor.getSelector().getString("EsconderJogadores.Mensagem_Off").replace("&", "§")));
                                                ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
                                                executorService.scheduleAtFixedRate(() -> {

                                                    long distancia = cooldown.asMap().get(p.getUniqueId()) - System.currentTimeMillis();

                                                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText((seletor.getSelector().getString("EsconderJogadores.Mensagem_EmCooldownRegressiva").replace("&", "§").replace("[SEGUNDOS]", String.valueOf(TimeUnit.MILLISECONDS.toSeconds(distancia))))));
                                                }, 0, 1, TimeUnit.SECONDS);
                                            } else {
                                                p.sendMessage(seletor.getSelector().getString("EsconderJogadores.Mensagem_Off").replace("&", "§"));
                                            }
                                        } else {
                                            p.sendMessage(seletor.getSelector().getString("EsconderJogadores.Mensagem_Off").replace("&", "§"));
                                        }
                                    }
                                    verf--;
                                }
                            }

                        } else {
                            long distancia = cooldown.asMap().get(p.getUniqueId()) - System.currentTimeMillis();
                            if (mensagem.getMessage().getBoolean("AvisoEmActionBars")) {
                                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(seletor.getSelector().getString("EsconderJogadores.Mensagem_EmCooldown").replace("&", "§").replace("[SEGUNDOS]", String.valueOf(TimeUnit.MILLISECONDS.toSeconds(distancia)))));
                            } else {
                                p.sendMessage(seletor.getSelector().getString("EsconderJogadores.Mensagem_EmCooldown").replace("&", "§").replace("[SEGUNDOS]", String.valueOf(TimeUnit.MILLISECONDS.toSeconds(distancia))));
                            }
                        }
                    }
                }
            }
        }
        e.setCancelled(true);
    }


    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (pl.getConfig().getStringList("mundos_ativados").contains(p.getWorld().getName())) {
            if (seletor.getSelector().getBoolean("EsconderJogadores.ativado")) {
                addMeta();
                if (!p.hasPermission("xg7lobby.admin")) {
                    p.getInventory().clear(HPslot);
                } else {
                    if (p.getInventory().contains(HPitemAtivado)) {
                        p.getInventory().removeItem(HPitemAtivado);
                    } else if (p.getInventory().contains(HPitemDesativado)) {
                        p.getInventory().removeItem(HPitemDesativado);
                    }
                }

                vanished.remove(p.getUniqueId());
            }
        }
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e) {
        if (pl.getConfig().getStringList("mundos_ativados").contains(e.getFrom().getName())) {
            vanished.remove(e.getPlayer().getUniqueId());
        }
    }

}
