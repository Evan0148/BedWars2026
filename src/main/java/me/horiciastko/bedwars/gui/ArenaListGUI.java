package me.horiciastko.bedwars.gui;

import me.horiciastko.bedwars.BedWars;
import me.horiciastko.bedwars.models.Arena;
import me.horiciastko.bedwars.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ArenaListGUI extends BaseGUI {

    public ArenaListGUI() {
        super("§8起床战争 房间列表", 6);
    }

    @Override
    public void setContents(Player player) {
        int slot = 0;
        for (Arena arena : BedWars.getInstance().getArenaManager().getArenas()) {
            if (slot >= rows * 9)
                break;

            inventory.setItem(slot++, new ItemBuilder(Material.MAP)
                    .setName("§b§l房间: §f" + arena.getName())
                    .setLore(
                            "§7所属世界: §e" + (arena.getWorldName() != null ? arena.getWorldName() : "§c未设置"),
                            "§7队伍数量: §e" + arena.getTeams().size(),
                            "§7当前状态: §e" + arena.getState(),
                            "",
                            "§e左键点击编辑该房间",
                            "§c右键点击删除该房间")
                    .build());
        }

        inventory.setItem(48, new ItemBuilder(Material.NETHER_STAR)
                .setName("§a§l创建新房间")
                .setLore("§7点击获取创建新房间命令提示。").build());

        inventory.setItem(50, new ItemBuilder(Material.BEACON)
                .setName("§b§l设置主大厅")
                .setLore("§7将全服主大厅坐标设置在", "§7你当前的站立位置。", "", "§e▸ 点击设置")
                .build());

        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName(" ").build());
            }
        }
    }

    @Override
    public void handleAction(Player player, int slot, ItemStack item, org.bukkit.event.inventory.ClickType clickType) {
        if (item == null || item.getType() == Material.GRAY_STAINED_GLASS_PANE)
            return;

        if (slot == 48) {
            player.closeInventory();
            player.sendMessage(BedWars.getInstance().getLanguageManager().getMessage(player.getUniqueId(), "arena-list-create-hint"));
            return;
        }

        if (slot == 50) {
            BedWars.getInstance().getGameManager().setMainLobbyLocation(player.getLocation());
            player.sendMessage(BedWars.getInstance().getLanguageManager().getMessage(player.getUniqueId(), "arena-list-lobby-set"));
            return;
        }

        if (slot < BedWars.getInstance().getArenaManager().getArenas().size()) {
            Arena arena = BedWars.getInstance().getArenaManager().getArenas().get(slot);
            if (clickType == org.bukkit.event.inventory.ClickType.RIGHT) {
                BedWars.getInstance().getArenaManager().deleteArena(arena);
                player.sendMessage(BedWars.getInstance().getLanguageManager().getMessage(player.getUniqueId(), "arena-list-removed").replace("%name%", arena.getName()));
                open(player);
            } else {
                new ArenaSettingsGUI(arena).open(player);
            }
        }
    }
}
