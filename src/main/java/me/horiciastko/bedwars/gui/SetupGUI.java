package me.horiciastko.bedwars.gui;

import me.horiciastko.bedwars.BedWars;
import me.horiciastko.bedwars.models.Arena;
import me.horiciastko.bedwars.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("deprecation")
public class SetupGUI extends BaseGUI {

    private final Arena arena;

    public SetupGUI(Arena arena) {
        super("§8房间配置: " + arena.getName(), 5);
        this.arena = arena;
    }

    @Override
    public void setContents(Player player) {
        inventory.setItem(10, new ItemBuilder(Material.GRASS_BLOCK)
                .setName("§a设置房间所在世界")
                .setLore("§7当前世界: " + (arena.getWorldName() != null ? "§e" + arena.getWorldName() : "§c未设置"),
                        "",
                        "§e点击打开世界选择器",
                        "§7你将被传送至所选世界")
                .build());

        inventory.setItem(11, new ItemBuilder(Material.COMPASS)
                .setName("§a设置等待大厅出生点")
                .setLore("§7当前状态: " + (arena.getLobbyLocation() != null ? "§a已设置" : "§c未设置"),
                        "",
                        "§e点击设置为你当前站立位置")
                .build());

        inventory.setItem(12, new ItemBuilder(com.cryptomorin.xseries.XMaterial.WOODEN_AXE)
                .setName("§a设置边界点 1 (Pos1)")
                .setLore("§7当前坐标: " + formatLocation(arena.getPos1()),
                        "",
                        "§e左键点击设为当前位置",
                        "§7右键点击清除坐标")
                .build());

        inventory.setItem(13, new ItemBuilder(com.cryptomorin.xseries.XMaterial.GOLDEN_AXE)
                .setName("§a设置边界点 2 (Pos2)")
                .setLore("§7当前坐标: " + formatLocation(arena.getPos2()),
                        "",
                        "§e左键点击设为当前位置",
                        "§7右键点击清除坐标")
                .build());

        boolean enabled = arena.isEnabled();
        inventory.setItem(16, new ItemBuilder(enabled ? Material.LIME_DYE : Material.GRAY_DYE)
                .setName(enabled ? "§a房间状态: 已启用" : "§c房间状态: 已禁用")
                .setLore("§7状态: " + (enabled ? "§a已启用" : "§c已禁用"),
                        "",
                        "§e点击切换启用状态")
                .build());

        inventory.setItem(20, new ItemBuilder(Material.WHITE_BED)
                .setName("§a管理队伍配置")
                .setLore("§7点击配置队伍出生点、床坐标等",
                        "§7当前队伍数: §e" + arena.getTeams().size())
                .build());

        inventory.setItem(24, new ItemBuilder(Material.DIAMOND)
                .setName("§a管理资源生成点")
                .setLore("§7点击配置钻石与绿宝石刷新点",
                        "§7钻石刷新点: §b" + arena.getDiamondGenerators().size(),
                        "§7绿宝石刷新点: §a" + arena.getEmeraldGenerators().size())
                .build());

        inventory.setItem(29, new ItemBuilder(Material.PLAYER_HEAD)
                .setName("§a最少玩家数")
                .setLore("§7当前设定: §e" + arena.getMinPlayers(),
                        "",
                        "§a左键点击: +1",
                        "§c右键点击: -1",
                        "§7Shift+点击: ±5")
                .build());

        inventory.setItem(30, new ItemBuilder(Material.PLAYER_HEAD)
                .setName("§a最多玩家数")
                .setLore("§7当前设定: §e" + arena.getMaxPlayers(),
                        "",
                        "§a左键点击: +1",
                        "§c右键点击: -1",
                        "§7Shift+点击: ±5")
                .build());

        inventory.setItem(32, new ItemBuilder(Material.IRON_SWORD)
                .setName("§a游戏模式")
                .setLore("§7当前模式: §e" + arena.getMode().getDisplayName(),
                        "§7每队玩家数: §e" + arena.getMode().getPlayersPerTeam(),
                        "",
                        "§e点击循环切换模式")
                .build());

        inventory.setItem(33, new ItemBuilder(Material.DIAMOND_SWORD)
                .setName("§aPvP 攻击机制")
                .setLore("§7当前设定: §e" + arena.getPvpMode().getDisplayName(),
                        "",
                        "§7LEGACY_1_8: 无攻击冷却CD (1.8连点)",
                        "§7MODERN_1_16: 开启攻击冷却槽机制",
                        "",
                        "§e点击切换机制")
                .build());

        inventory.setItem(40, new ItemBuilder(Material.EMERALD_BLOCK)
                .setName("§a§l保存并退出编辑")
                .setLore("§7点击保存所有配置并退出配置状态")
                .build());

        ItemStack glass = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName(" ").build();
        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, glass);
            }
        }
    }

    private String formatLocation(org.bukkit.Location loc) {
        if (loc == null)
            return "§c未设置";
        return String.format("§a%.0f, %.0f, %.0f", loc.getX(), loc.getY(), loc.getZ());
    }

    @Override
    public void handleAction(Player player, int slot, ItemStack item, org.bukkit.event.inventory.ClickType clickType) {
        if (item == null || !item.hasItemMeta())
            return;
        String name = item.getItemMeta().getDisplayName();

        if (name.contains("设置房间所在世界") || name.contains("Set Arena World")) {
            new WorldSelectorGUI(arena).open(player);
        } else if (name.contains("设置等待大厅出生点") || name.contains("Set Waiting Lobby")) {
            arena.setLobbyLocation(player.getLocation());
            player.sendMessage(BedWars.getInstance().getLanguageManager().getMessage(player.getUniqueId(), "setup-lobby-set"));
            setContents(player);
        } else if (name.contains("边界点 1") || name.contains("Set Arena Pos1")) {
            if (clickType.isRightClick()) {
                arena.setPos1(null);
                player.sendMessage(BedWars.getInstance().getLanguageManager().getMessage(player.getUniqueId(), "setup-pos1-cleared"));
            } else {
                arena.setPos1(player.getLocation());
                player.sendMessage(BedWars.getInstance().getLanguageManager().getMessage(player.getUniqueId(), "setup-pos1-set"));
            }
            setContents(player);
        } else if (name.contains("边界点 2") || name.contains("Set Arena Pos2")) {
            if (clickType.isRightClick()) {
                arena.setPos2(null);
                player.sendMessage(BedWars.getInstance().getLanguageManager().getMessage(player.getUniqueId(), "setup-pos2-cleared"));
            } else {
                arena.setPos2(player.getLocation());
                player.sendMessage(BedWars.getInstance().getLanguageManager().getMessage(player.getUniqueId(), "setup-pos2-set"));
            }
            setContents(player);
        } else if (name.contains("已启用") || name.contains("已禁用") || name.contains("Arena Enabled") || name.contains("Arena Disabled")) {
            arena.setEnabled(!arena.isEnabled());
            player.sendMessage(BedWars.getInstance().getLanguageManager().getMessage(player.getUniqueId(), arena.isEnabled() ? "setup-arena-enabled" : "setup-arena-disabled"));
            setContents(player);
        } else if (name.contains("管理队伍") || name.contains("Manage Teams")) {
            new TeamListGUI(arena).open(player);
        } else if (name.contains("管理资源生成点") || name.contains("Manage Generators")) {
            new GeneratorSetupGUI(arena).open(player);
        } else if (name.contains("最少玩家数") || name.contains("Min Players")) {
            int change = clickType.isShiftClick() ? 5 : 1;
            if (clickType.isRightClick())
                change = -change;
            int newValue = Math.max(1, arena.getMinPlayers() + change);
            arena.setMinPlayers(newValue);
            player.sendMessage(BedWars.getInstance().getLanguageManager().getMessage(player.getUniqueId(), "setup-min-players-set").replace("%value%", String.valueOf(newValue)));
            setContents(player);
        } else if (name.contains("最多玩家数") || name.contains("Max Players")) {
            int change = clickType.isShiftClick() ? 5 : 1;
            if (clickType.isRightClick())
                change = -change;
            int newValue = Math.max(arena.getMinPlayers(), arena.getMaxPlayers() + change);
            arena.setMaxPlayers(newValue);
            player.sendMessage(BedWars.getInstance().getLanguageManager().getMessage(player.getUniqueId(), "setup-max-players-set").replace("%value%", String.valueOf(newValue)));
            setContents(player);
        } else if (name.contains("游戏模式") || name.contains("Game Mode")) {
            Arena.ArenaMode[] modes = Arena.ArenaMode.values();
            int currentIndex = arena.getMode().ordinal();
            int nextIndex = (currentIndex + 1) % modes.length;
            arena.setMode(modes[nextIndex]);
            player.sendMessage(BedWars.getInstance().getLanguageManager().getMessage(player.getUniqueId(), "setup-mode-set").replace("%mode%", arena.getMode().getDisplayName()));
            setContents(player);
        } else if (name.contains("PvP") || name.contains("PVP")) {
            if (arena.getPvpMode() == Arena.PvpMode.LEGACY_1_8) {
                arena.setPvpMode(Arena.PvpMode.MODERN_1_16);
            } else {
                arena.setPvpMode(Arena.PvpMode.LEGACY_1_8);
            }
            player.sendMessage(BedWars.getInstance().getLanguageManager().getMessage(player.getUniqueId(), "setup-pvp-set").replace("%mode%", arena.getPvpMode().getDisplayName()));
            setContents(player);
        } else if (name.contains("保存并退出") || name.contains("SAVE AND EXIT")) {
            BedWars.getInstance().getArenaManager().saveArena(arena);
            BedWars.getInstance().getArenaManager().stopEditing(player);
            player.sendMessage(BedWars.getInstance().getLanguageManager().getMessage(player.getUniqueId(), "setup-saved"));
            player.closeInventory();
        }
    }
}
