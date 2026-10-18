package me.horiciastko.bedwars.gui;

import me.horiciastko.bedwars.BedWars;
import me.horiciastko.bedwars.models.Arena;
import me.horiciastko.bedwars.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ArenaLocationManagementGUI extends BaseGUI {

    private final Arena arena;

    public ArenaLocationManagementGUI(Arena arena) {
        super("§8坐标状态管理: §1" + arena.getName(), 3);
        this.arena = arena;
    }

    @Override
    public void setContents(Player player) {
        inventory.setItem(26, new ItemBuilder(Material.ARROW)
                .setName("§c返回上一级")
                .setLore("§7返回房间主设置")
                .build());

        if (arena.getPos1() != null) {
            inventory.setItem(11, new ItemBuilder(Material.LIME_CONCRETE)
                    .setName("§a边界点 1 (Pos 1)")
                    .setLore("§7状态: §a已设置", "", "§e点击清除坐标")
                    .build());
        } else {
            inventory.setItem(11, new ItemBuilder(Material.RED_CONCRETE)
                    .setName("§c边界点 1 (Pos 1)")
                    .setLore("§7状态: §c未设置")
                    .build());
        }

        if (arena.getPos2() != null) {
            inventory.setItem(13, new ItemBuilder(Material.LIME_CONCRETE)
                    .setName("§a边界点 2 (Pos 2)")
                    .setLore("§7状态: §a已设置", "", "§e点击清除坐标")
                    .build());
        } else {
            inventory.setItem(13, new ItemBuilder(Material.RED_CONCRETE)
                    .setName("§c边界点 2 (Pos 2)")
                    .setLore("§7状态: §c未设置")
                    .build());
        }

        if (arena.getLobbyLocation() != null) {
            inventory.setItem(15, new ItemBuilder(Material.LIME_CONCRETE)
                    .setName("§a等待大厅出生点")
                    .setLore("§7状态: §a已设置", "", "§e点击清除坐标")
                    .build());
        } else {
            inventory.setItem(15, new ItemBuilder(Material.RED_CONCRETE)
                    .setName("§c等待大厅出生点")
                    .setLore("§7状态: §c未设置")
                    .build());
        }

        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName(" ").build());
            }
        }
    }

    @Override
    public void handleAction(Player player, int slot, ItemStack item, org.bukkit.event.inventory.ClickType clickType) {
        if (slot == 26) {
            new ArenaSettingsGUI(arena).open(player);
        } else if (slot == 11 && arena.getPos1() != null) {
            arena.setPos1(null);
            player.sendMessage(BedWars.getInstance().getLanguageManager().getMessage(player.getUniqueId(), "location-pos1-cleared"));
            saveAndReopen(player);
        } else if (slot == 13 && arena.getPos2() != null) {
            arena.setPos2(null);
            player.sendMessage(BedWars.getInstance().getLanguageManager().getMessage(player.getUniqueId(), "location-pos2-cleared"));
            saveAndReopen(player);
        } else if (slot == 15 && arena.getLobbyLocation() != null) {
            arena.setLobbyLocation(null);
            player.sendMessage(BedWars.getInstance().getLanguageManager().getMessage(player.getUniqueId(), "location-lobby-cleared"));
            saveAndReopen(player);
        }
    }

    private void saveAndReopen(Player player) {
        me.horiciastko.bedwars.BedWars.getInstance().getArenaManager().saveArena(arena);
        me.horiciastko.bedwars.BedWars.getInstance().getVisualizationManager().showHolograms(player, arena);
        open(player);
    }
}
