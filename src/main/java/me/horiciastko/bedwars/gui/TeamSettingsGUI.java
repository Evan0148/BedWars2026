package me.horiciastko.bedwars.gui;

import me.horiciastko.bedwars.BedWars;
import me.horiciastko.bedwars.models.Arena;
import me.horiciastko.bedwars.models.Team;
import me.horiciastko.bedwars.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("deprecation")
public class TeamSettingsGUI extends BaseGUI {

    private final Arena arena;
    private final Team team;

    public TeamSettingsGUI(Arena arena, Team team) {
        super("§8" + team.getColor() + "⬛ §8队伍设置: " + team.getColor() + team.getDisplayName(), 3);
        this.arena = arena;
        this.team = team;
    }

    @Override
    public void open(Player player) {
        super.open(player);
        BedWars.getInstance().getVisualizationManager().showHolograms(player, arena);
    }

    @Override
    public void setContents(Player player) {
        Material teamGlass = getTeamGlass(team.getColor());
        for (int i = 0; i < 9; i++) {
            inventory.setItem(i, new ItemBuilder(teamGlass).setName(" ").build());
            inventory.setItem(18 + i, new ItemBuilder(teamGlass).setName(" ").build());
        }

        inventory.setItem(10, new ItemBuilder(Material.ENDER_PEARL)
                .setName(team.getColor() + "§l设置队伍出生点")
                .setLore("§7当前坐标: " + formatLocation(team.getSpawnLocation()),
                        "",
                        "§a左键点击 §7设置为你当前的站立位置",
                        "§c右键点击 §7清除当前出生点")
                .build());

        inventory.setItem(11, new ItemBuilder(getTeamBed(team.getColor()))
                .setName(team.getColor() + "§l绑定队伍床坐标")
                .setLore("§7当前坐标: " + formatLocation(team.getBedLocation()),
                        "",
                        "§e点击 §7自动搜索周围最近的床方块",
                        "§7(搜索范围: 10 格内)")
                .build());

        inventory.setItem(12, new ItemBuilder(Material.IRON_BLOCK)
                .setName(team.getColor() + "§l配置队伍资源生成点")
                .setLore("§7已设生成点: §e" + team.getGenerators().size() + " 个",
                        "",
                        "§a左键点击 §7在此处添加一个基地生成点",
                        "§c右键点击 §7清空该队伍的所有生成点")
                .build());

        inventory.setItem(14, new ItemBuilder(Material.VILLAGER_SPAWN_EGG)
                .setName("§a§l设置道具商店 NPC")
                .setLore("§7当前坐标: " + formatLocation(team.getShopLocation()),
                        "",
                        "§a左键点击 §7设置为你当前的站立位置",
                        "§c右键点击 §7清除当前商店 NPC")
                .build());

        inventory.setItem(15, new ItemBuilder(Material.EXPERIENCE_BOTTLE)
                .setName("§b§l设置团队升级 NPC")
                .setLore("§7当前坐标: " + formatLocation(team.getUpgradeLocation()),
                        "",
                        "§a左键点击 §7设置为你当前的站立位置",
                        "§c右键点击 §7清除当前升级 NPC")
                .build());

        inventory.setItem(16, new ItemBuilder(team.getMaterial())
                .setName(team.getColor() + "§l队伍信息概览")
                .setLore("§7队伍颜色: " + team.getColor() + team.getColor().name(),
                        "§7出生点: " + (team.getSpawnLocation() != null ? "§a已设置" : "§c未设置"),
                        "§7床坐标: " + (team.getBedLocation() != null ? "§a已设置" : "§c未设置"),
                        "§7道具商店: " + (team.getShopLocation() != null ? "§a已设置" : "§c未设置"),
                        "§7团队升级: " + (team.getUpgradeLocation() != null ? "§a已设置" : "§c未设置"),
                        "§7基地生成点数: §e" + team.getGenerators().size())
                .build());

        inventory.setItem(22, new ItemBuilder(Material.BARRIER)
                .setName("§c§l返回队伍列表")
                .setLore("§7返回队伍管理界面")
                .build());

        for (int i = 9; i < 18; i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName(" ").build());
            }
        }
    }

    private String formatLocation(org.bukkit.Location loc) {
        if (loc == null)
            return "§c未设置";
        return String.format("§a%.0f, %.0f, %.0f", loc.getX(), loc.getY(), loc.getZ());
    }

    private Material getTeamGlass(org.bukkit.ChatColor color) {
        switch (color) {
            case RED:
                return Material.RED_STAINED_GLASS_PANE;
            case BLUE:
                return Material.BLUE_STAINED_GLASS_PANE;
            case GREEN:
                return Material.LIME_STAINED_GLASS_PANE;
            case YELLOW:
                return Material.YELLOW_STAINED_GLASS_PANE;
            case AQUA:
                return Material.CYAN_STAINED_GLASS_PANE;
            case WHITE:
                return Material.WHITE_STAINED_GLASS_PANE;
            case LIGHT_PURPLE:
                return Material.PINK_STAINED_GLASS_PANE;
            case GRAY:
                return Material.GRAY_STAINED_GLASS_PANE;
            default:
                return Material.LIGHT_GRAY_STAINED_GLASS_PANE;
        }
    }

    private Material getTeamBed(org.bukkit.ChatColor color) {
        switch (color) {
            case RED:
                return Material.RED_BED;
            case BLUE:
                return Material.BLUE_BED;
            case GREEN:
                return Material.LIME_BED;
            case YELLOW:
                return Material.YELLOW_BED;
            case AQUA:
                return Material.CYAN_BED;
            case WHITE:
                return Material.WHITE_BED;
            case LIGHT_PURPLE:
                return Material.PINK_BED;
            case GRAY:
                return Material.GRAY_BED;
            default:
                return Material.WHITE_BED;
        }
    }

    @Override
    public void handleAction(Player player, int slot, ItemStack item, org.bukkit.event.inventory.ClickType clickType) {
        if (item == null || !item.hasItemMeta())
            return;

        boolean save = false;

        switch (slot) {
            case 22:
                new TeamManagementGUI(arena).open(player);
                return;
            case 10:
                if (clickType.isRightClick()) {
                    team.setSpawnLocation(null);
                    player.sendMessage(BedWars.getInstance().getLanguageManager().getMessage(player.getUniqueId(), "team-spawn-cleared"));
                } else {
                    team.setSpawnLocation(player.getLocation());
                    player.sendMessage(BedWars.getInstance().getLanguageManager().getMessage(player.getUniqueId(), "team-spawn-set"));
                }
                save = true;
                break;
            case 11:
                Block bed = findNearbyBed(player.getLocation(), 10);
                if (bed != null) {
                    team.setBedLocation(bed.getLocation());
                    player.sendMessage(BedWars.getInstance().getLanguageManager().getMessage(player.getUniqueId(), "team-bed-detected").replace("%location%", formatLocation(bed.getLocation())));
                } else {
                    player.sendMessage(BedWars.getInstance().getLanguageManager().getMessage(player.getUniqueId(), "team-bed-not-found"));
                }
                save = true;
                break;
            case 12:
                if (clickType.isRightClick()) {
                    team.getGenerators().clear();
                    player.sendMessage(BedWars.getInstance().getLanguageManager().getMessage(player.getUniqueId(), "team-generators-cleared"));
                } else {
                    team.getGenerators().add(player.getLocation().getBlock().getLocation().add(0.5, 0, 0.5));
                    player.sendMessage(BedWars.getInstance().getLanguageManager().getMessage(player.getUniqueId(), "team-generator-added"));
                }
                save = true;
                break;
            case 14:
                if (clickType.isRightClick()) {
                    team.setShopLocation(null);
                    player.sendMessage(BedWars.getInstance().getLanguageManager().getMessage(player.getUniqueId(), "team-shop-cleared"));
                } else {
                    team.setShopLocation(player.getLocation());
                    player.sendMessage(BedWars.getInstance().getLanguageManager().getMessage(player.getUniqueId(), "team-shop-set"));
                }
                save = true;
                break;
            case 15:
                if (clickType.isRightClick()) {
                    team.setUpgradeLocation(null);
                    player.sendMessage(BedWars.getInstance().getLanguageManager().getMessage(player.getUniqueId(), "team-upgrade-cleared"));
                } else {
                    team.setUpgradeLocation(player.getLocation());
                    player.sendMessage(BedWars.getInstance().getLanguageManager().getMessage(player.getUniqueId(), "team-upgrade-set"));
                }
                save = true;
                break;
        }

        if (save) {
            BedWars.getInstance().getArenaManager().saveArena(arena);
            BedWars.getInstance().getVisualizationManager().showHolograms(player, arena);
            open(player);
        }
    }

    private Block findNearbyBed(org.bukkit.Location location, int radius) {
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Block b = location.clone().add(x, y, z).getBlock();
                    if (b.getType().name().contains("_BED")) {
                        return b;
                    }
                }
            }
        }
        return null;
    }
}
