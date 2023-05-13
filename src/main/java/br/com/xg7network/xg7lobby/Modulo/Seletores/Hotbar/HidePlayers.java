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
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static br.com.xg7network.xg7lobby.Modulo.Seletores.InventoryManager.addMeta;
import static br.com.xg7network.xg7lobby.XG7Lobby.*;

public class HidePlayers implements Listener  {

    private XG7Lobby pl;

    int verf = 0;

    public HidePlayers (XG7Lobby pl) {
        this.pl = pl;
    }

    private List<UUID> vanished = new ArrayList<>();

    public static List<String> HPlore = seletor.getSelector().getStringList("EsconderJogadores.lore");

    public static int HPslot = seletor.getSelector().getInt("EsconderJogadores.slot") - 1;

    public static ItemStack HPitemAtivado = new ItemStack(Material.valueOf(seletor.getSelector().getString("EsconderJogadores.item_ativado")));
    public static ItemMeta HPmetaAtivado = HPitemAtivado.getItemMeta();
    public static ItemStack HPitemDesativado = new ItemStack(Material.valueOf(seletor.getSelector().getString("EsconderJogadores.item_desativado")));
    public static ItemMeta HPmetaDesativado = HPitemDesativado.getItemMeta();

    private Cache<UUID, Long> cooldown = CacheBuilder.newBuilder().expireAfterWrite(seletor.getSelector().getInt("EsconderJogadores.Cooldown"), TimeUnit.SECONDS).build();



    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (seletor.getSelector().getBoolean("EsconderJogadores.ativado")) {
            Player p = e.getPlayer();
            if ((e.getItem() != null && HPitemAtivado != null && e.getItem().equals(HPitemAtivado)) || (e.getItem() != null && HPitemDesativado != null && e.getItem().equals(HPitemDesativado))) {
                if (pl.getConfig().getStringList("mundos_ativados").contains(p.getWorld().getName())) {
                        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
                            long segundos = 0;
                            if (!cooldown.asMap().containsKey(p.getUniqueId()) || cooldown.asMap().get(p.getUniqueId()) <= System.currentTimeMillis()) {
                                segundos = seletor.getSelector().getInt("EsconderJogadores.Cooldown");
                                cooldown.put(p.getUniqueId(), System.currentTimeMillis() + segundos * 1000L);

                                if (e.getItem().equals(HPitemAtivado)) {
                                    if (verf == 0) {
                                        if (!vanished.contains(p.getUniqueId())) {
                                            vanished.add(p.getUniqueId());
                                            for (Player target : Bukkit.getOnlinePlayers()) {
                                                target.hidePlayer(p);
                                            }
                                            action.mandarAction(p, seletor.getSelector().getString("EsconderJogadores.Mensagem_On"));
                                            int slot = p.getInventory().getHeldItemSlot();
                                            if (p.hasPermission("xg7lobby.admin")) {
                                                p.getInventory().setItem(slot, HPitemDesativado);
                                            } else {
                                                p.getInventory().setItem(HPslot, HPitemDesativado);
                                            }
                                        }
                                        verf++;
                                    }
                                } else if (e.getItem().equals(HPitemDesativado)) {
                                    if (verf == 1) {
                                        if (vanished.contains(p.getUniqueId())) {
                                            vanished.remove(p.getUniqueId());
                                            for (Player target : Bukkit.getOnlinePlayers()) {
                                                target.showPlayer(p);
                                            }
                                            action.mandarAction(p, seletor.getSelector().getString("EsconderJogadores.Mensagem_Off"));
                                            int slot = p.getInventory().getHeldItemSlot();
                                            p.getInventory().clear(slot);
                                            if (p.hasPermission("xg7lobby.admin")) {
                                                p.getInventory().setItem(slot, HPitemAtivado);
                                            } else {
                                                p.getInventory().setItem(HPslot, HPitemAtivado);
                                            }
                                        }
                                        verf--;
                                    }
                                }
                            } else {
                                long distancia = cooldown.asMap().get(p.getUniqueId()) - System.currentTimeMillis();
                                action.mandarAction(p, seletor.getSelector().getString("EsconderJogadores.Mensagem_EmCooldown").replace("[SEGUNDOS]", String.valueOf(TimeUnit.MILLISECONDS.toSeconds(distancia))));
                            }

                        }
                }
            }
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        vanished.remove(p.getUniqueId());
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e) {
        if (pl.getConfig().getStringList("mundos_ativados").contains(e.getFrom().getName())) {
            vanished.remove(e.getPlayer().getUniqueId());
        }
    }

}
