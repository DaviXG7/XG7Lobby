package br.com.xg7network.xg7lobby.Modulo.Seletores.Hotbar;

import br.com.xg7network.xg7lobby.Configs.SeletorManager;
import br.com.xg7network.xg7lobby.XG7Lobby;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static br.com.xg7network.xg7lobby.XG7Lobby.seletor;

public class HidePlayers implements Listener {
    private XG7Lobby pl;

    int verf = 0;

    public HidePlayers(XG7Lobby pl) {
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
        if (XG7Lobby.seletor.getSelector().getBoolean("EsconderJogadores.ativado")) {
            Player p = e.getPlayer();
            if ((e.getItem() != null && HPitemAtivado != null && e.getItem().equals(HPitemAtivado) || e.getItem() != null && HPitemDesativado != null && e.getItem().equals(HPitemDesativado)) && this.pl.getConfig().getStringList("mundos_ativados").contains(p.getWorld().getName()) && (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR))) {
                long segundos = 0L;
                if (this.cooldown.asMap().containsKey(p.getUniqueId()) && (Long) this.cooldown.asMap().get(p.getUniqueId()) > System.currentTimeMillis()) {
                    long distancia = (Long) this.cooldown.asMap().get(p.getUniqueId()) - System.currentTimeMillis();
                    XG7Lobby.action.mandarAction(p, XG7Lobby.seletor.getSelector().getString("Mensagem_EmCooldown").replace("[SEGUNDOS]", String.valueOf(TimeUnit.MILLISECONDS.toSeconds(distancia))));
                } else {
                    segundos = (long) XG7Lobby.seletor.getSelector().getInt("EsconderJogadores.Cooldown");
                    this.cooldown.put(p.getUniqueId(), System.currentTimeMillis() + segundos * 1000L);
                    int slot;
                    if (e.getItem().equals(HPitemAtivado)) {
                        if (this.verf == 0) {
                            if (!this.vanished.contains(p.getUniqueId())) {
                                this.vanished.add(p.getUniqueId());
                                for (Player target : Bukkit.getOnlinePlayers()) {
                                    target.hidePlayer(p);
                                }

                                XG7Lobby.action.mandarAction(p, XG7Lobby.seletor.getSelector().getString("EsconderJogadores.Mensagem_On"));
                                slot = p.getInventory().getHeldItemSlot();
                                if (p.hasPermission("xg7lobby.admin")) {
                                    p.getInventory().setItem(slot, HPitemDesativado);
                                } else {
                                    p.getInventory().setItem(HPslot, HPitemDesativado);
                                }
                            }

                            verf++;
                        }
                    } else if (e.getItem().equals(HPitemDesativado) && this.verf == 1) {
                        if (this.vanished.contains(p.getUniqueId())) {
                            this.vanished.remove(p.getUniqueId());

                            for (Player target : Bukkit.getOnlinePlayers()) {
                                target.showPlayer(p);
                            }

                            XG7Lobby.action.mandarAction(p, XG7Lobby.seletor.getSelector().getString("EsconderJogadores.Mensagem_Off"));
                            slot = p.getInventory().getHeldItemSlot();
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
            }
        }

    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        this.vanished.remove(p.getUniqueId());
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e) {
        if (this.pl.getConfig().getStringList("mundos_ativados").contains(e.getFrom().getName())) {
            this.vanished.remove(e.getPlayer().getUniqueId());
        }

    }
}