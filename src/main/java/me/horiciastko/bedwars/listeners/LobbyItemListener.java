package me.horiciastko.bedwars.listeners;

import me.horiciastko.bedwars.BedWars;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class LobbyItemListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        ItemStack item = event.getItem();
        if (item == null || !item.hasItemMeta()) {
            return;
        }

        String special = me.horiciastko.bedwars.utils.ItemTagUtils.getTag(item, "special_item");
        if ("lobby_leave".equals(special)) {
            Player player = event.getPlayer();
            if (BedWars.getInstance().getArenaManager().getPlayerArena(player) != null) {
                BedWars.getInstance().getArenaManager().leaveArena(player);
                event.setCancelled(true);
            }
        } else if ("lobby_team_select".equals(special)) {
            Player player = event.getPlayer();
            me.horiciastko.bedwars.models.Arena arena = BedWars.getInstance().getArenaManager().getPlayerArena(player);
            if (arena != null && (arena.getState() == me.horiciastko.bedwars.models.Arena.GameState.WAITING ||
                    arena.getState() == me.horiciastko.bedwars.models.Arena.GameState.STARTING)) {
                new me.horiciastko.bedwars.gui.TeamSelectGUI(arena).open(player);
                event.setCancelled(true);
            }
        } else if ("lobby_force_start".equals(special)) {
            Player player = event.getPlayer();
            if (!player.hasPermission("bedwars.admin")) {
                player.sendMessage(BedWars.getInstance().getLanguageManager().getMessage(player.getUniqueId(), "admin-no-permission"));
                event.setCancelled(true);
                return;
            }
            me.horiciastko.bedwars.models.Arena arena = BedWars.getInstance().getArenaManager().getPlayerArena(player);
            if (arena != null) {
                if (arena.getState() == me.horiciastko.bedwars.models.Arena.GameState.WAITING ||
                        arena.getState() == me.horiciastko.bedwars.models.Arena.GameState.STARTING) {
                    if (arena.getPlayers().isEmpty()) {
                        player.sendMessage(BedWars.getInstance().getLanguageManager().getMessage(player.getUniqueId(), "admin-start-no-players")
                                .replace("%name%", arena.getName()));
                    } else {
                        BedWars.getInstance().getGameManager().forceStart(arena);
                        player.sendMessage(BedWars.getInstance().getLanguageManager().getMessage(player.getUniqueId(), "admin-start-forced")
                                .replace("%name%", arena.getName()));
                        try {
                            player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.5f);
                        } catch (Throwable ignored) {}
                    }
                }
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockPlace(org.bukkit.event.block.BlockPlaceEvent event) {
        if (isLobbyItem(event.getItemInHand())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrop(org.bukkit.event.player.PlayerDropItemEvent event) {
        ItemStack item = event.getItemDrop().getItemStack();
        if (isLobbyItem(item)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(org.bukkit.event.inventory.InventoryClickEvent event) {
        if (isLobbyItem(event.getCurrentItem()) || isLobbyItem(event.getCursor())) {
            event.setCancelled(true);
            return;
        }

        if (event.getClick() == org.bukkit.event.inventory.ClickType.NUMBER_KEY) {
            ItemStack hotbarItem = event.getWhoClicked().getInventory().getItem(event.getHotbarButton());
            if (isLobbyItem(hotbarItem)) {
                event.setCancelled(true);
            }
        }
    }

    private boolean isLobbyItem(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }
        String special = me.horiciastko.bedwars.utils.ItemTagUtils.getTag(item, "special_item");
        return "lobby_leave".equals(special) || "lobby_team_select".equals(special) || "lobby_force_start".equals(special);
    }
}
