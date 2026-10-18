package me.horiciastko.bedwars.gui;

import me.horiciastko.bedwars.BedWars;
import me.horiciastko.bedwars.models.Arena;
import me.horiciastko.bedwars.models.Team;
import me.horiciastko.bedwars.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("deprecation")
public class GeneratorSetupGUI extends BaseGUI {

    private final Arena arena;
    private final Team team;

    public GeneratorSetupGUI(Arena arena) {
        super("§8全局生成点设置", 3);
        this.arena = arena;
        this.team = null;
    }

    public GeneratorSetupGUI(Arena arena, Team team) {
        super("§8队伍生成点设置: " + team.getName(), 3);
        this.arena = arena;
        this.team = team;
    }

    @Override
    public void setContents(Player player) {
        if (team == null) {
            inventory.setItem(11, new ItemBuilder(Material.DIAMOND)
                    .setName("§a添加钻石生成点")
                    .setLore("§7当前数量: " + arena.getDiamondGenerators().size(), "",
                            "§e点击在你所在位置添加")
                    .build());

            inventory.setItem(15, new ItemBuilder(Material.EMERALD)
                    .setName("§a添加绿宝石生成点")
                    .setLore("§7当前数量: " + arena.getEmeraldGenerators().size(), "",
                            "§e点击在你所在位置添加")
                    .build());

            inventory.setItem(22, new ItemBuilder(Material.BARRIER)
                    .setName("§c清空所有全局生成点")
                    .setLore("§7移除当前房间所有的钻石与绿宝石刷新点")
                    .build());

            inventory.setItem(26, new ItemBuilder(Material.ARROW).setName("§c返回上一级").build());

        } else {
            inventory.setItem(11, new ItemBuilder(Material.IRON_INGOT)
                    .setName("§a添加铁锭生成点")
                    .setLore("§7点击在你所在位置添加", "§7(通常与金锭设在同一方块)")
                    .build());

            inventory.setItem(15, new ItemBuilder(Material.GOLD_INGOT)
                    .setName("§a添加金锭生成点")
                    .setLore("§7点击在你所在位置添加")
                    .build());

            inventory.setItem(22, new ItemBuilder(Material.BARRIER)
                    .setName("§c清空队伍生成点")
                    .setLore("§7移除该队伍的所有资源刷新点")
                    .build());

            inventory.setItem(26, new ItemBuilder(Material.ARROW).setName("§c返回上一级").build());
        }

        ItemStack glass = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName(" ").build();
        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, glass);
            }
        }
    }

    @Override
    public void handleAction(Player player, int slot, ItemStack item, org.bukkit.event.inventory.ClickType clickType) {
        if (item == null || !item.hasItemMeta())
            return;
        String name = item.getItemMeta().getDisplayName();

        if (name.equals("§c返回上一级") || name.equals("§cBack")) {
            if (team == null)
                new ArenaSettingsGUI(arena).open(player);
            else
                new TeamSettingsGUI(arena, team).open(player);
            return;
        }

        if (team == null) {
            if (name.contains("添加钻石生成点") || name.contains("Add Diamond Generator")) {
                arena.getDiamondGenerators().add(player.getLocation());
                player.sendMessage(BedWars.getInstance().getLanguageManager().getMessage(player.getUniqueId(), "generator-diamond-added-gui"));
                setContents(player);
            } else if (name.contains("添加绿宝石生成点") || name.contains("Add Emerald Generator")) {
                arena.getEmeraldGenerators().add(player.getLocation());
                player.sendMessage(BedWars.getInstance().getLanguageManager().getMessage(player.getUniqueId(), "generator-emerald-added-gui"));
                setContents(player);
            } else if (name.contains("清空所有全局生成点") || name.contains("Clear All")) {
                arena.getDiamondGenerators().clear();
                arena.getEmeraldGenerators().clear();
                player.sendMessage(BedWars.getInstance().getLanguageManager().getMessage(player.getUniqueId(), "generator-global-cleared"));
                setContents(player);
            }
        } else {
            if (name.contains("添加铁锭生成点") || name.contains("Add Iron Generator")) {
                team.getGenerators().add(player.getLocation());
                player.sendMessage(BedWars.getInstance().getLanguageManager().getMessage(player.getUniqueId(), "generator-team-added-iron"));
                setContents(player);
            } else if (name.contains("添加金锭生成点") || name.contains("Add Gold Generator")) {
                team.getGenerators().add(player.getLocation());
                player.sendMessage(BedWars.getInstance().getLanguageManager().getMessage(player.getUniqueId(), "generator-team-added-gold"));
                setContents(player);
            } else if (name.contains("清空队伍生成点") || name.contains("Clear Team")) {
                team.getGenerators().clear();
                player.sendMessage(BedWars.getInstance().getLanguageManager().getMessage(player.getUniqueId(), "generator-team-cleared"));
                setContents(player);
            }
        }
        BedWars.getInstance().getArenaManager().saveArena(arena);
    }
}
