package me.horiciastko.bedwars.gui;

import me.horiciastko.bedwars.BedWars;
import me.horiciastko.bedwars.models.Arena;
import me.horiciastko.bedwars.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("deprecation")
public class ArenaSettingsGUI extends BaseGUI {

        private final Arena arena;

        public ArenaSettingsGUI(Arena arena) {
                super("§8§l✦ §9" + arena.getName() + " §8§l✦ §7房间配置向导", 5);
                this.arena = arena;
        }

        @Override
        public void open(Player player) {
                super.open(player);
                BedWars.getInstance().getArenaManager().setEditArena(player, arena);
                BedWars.getInstance().getVisualizationManager().showHolograms(player, arena);
        }

        @Override
        public void setContents(Player player) {
                inventory.setItem(0, new ItemBuilder(com.cryptomorin.xseries.XMaterial.BLUE_STAINED_GLASS_PANE)
                                .setName(" ").build());
                inventory.setItem(1, new ItemBuilder(com.cryptomorin.xseries.XMaterial.CYAN_STAINED_GLASS_PANE)
                                .setName(" ").build());
                inventory.setItem(2, new ItemBuilder(com.cryptomorin.xseries.XMaterial.LIGHT_BLUE_STAINED_GLASS_PANE)
                                .setName(" ").build());
                inventory.setItem(3, new ItemBuilder(com.cryptomorin.xseries.XMaterial.WHITE_STAINED_GLASS_PANE)
                                .setName(" ").build());
                inventory.setItem(4, new ItemBuilder(com.cryptomorin.xseries.XMaterial.YELLOW_STAINED_GLASS_PANE)
                                .setName("§e§l" + arena.getName()).build());
                inventory.setItem(5, new ItemBuilder(com.cryptomorin.xseries.XMaterial.WHITE_STAINED_GLASS_PANE)
                                .setName(" ").build());
                inventory.setItem(6, new ItemBuilder(com.cryptomorin.xseries.XMaterial.LIGHT_BLUE_STAINED_GLASS_PANE)
                                .setName(" ").build());
                inventory.setItem(7, new ItemBuilder(com.cryptomorin.xseries.XMaterial.CYAN_STAINED_GLASS_PANE)
                                .setName(" ").build());
                inventory.setItem(8, new ItemBuilder(com.cryptomorin.xseries.XMaterial.BLUE_STAINED_GLASS_PANE)
                                .setName(" ").build());

                boolean worldSet = arena.getWorldName() != null;
                inventory.setItem(10,
                                new ItemBuilder(worldSet ? com.cryptomorin.xseries.XMaterial.GRASS_BLOCK
                                                : com.cryptomorin.xseries.XMaterial.DIRT)
                                                .setName(worldSet ? "§a§l✓ §f世界已选定" : "§c§l✗ §f选择世界")
                                                .setLore(
                                                                "§8━━━━━━━━━━━━━━━━━━━━━━━",
                                                                "§7当前世界: " + (worldSet ? "§e" + arena.getWorldName()
                                                                                : "§c未设置"),
                                                                "",
                                                                worldSet ? "§a✓ 该世界已配置完成！"
                                                                                : "§c✗ 请先选择地图所在世界！",
                                                                "",
                                                                "§e▸ 左键点击打开世界选择器",
                                                                "§7▸ 右键点击清除当前世界绑定",
                                                                "§8━━━━━━━━━━━━━━━━━━━━━━━")
                                                .build());

                boolean boundsSet = arena.getPos1() != null && arena.getPos2() != null;
                boolean lobbySet = arena.getLobbyLocation() != null;
                boolean step2Done = boundsSet && lobbySet;
                inventory.setItem(11, new ItemBuilder(step2Done ? com.cryptomorin.xseries.XMaterial.EMERALD_ORE : com.cryptomorin.xseries.XMaterial.STONE)
                                .setName(step2Done ? "§a§l✓ §f房间边界与大厅" : "§6§l⚠ §f房间边界与大厅")
                                .setLore(
                                                "§8━━━━━━━━━━━━━━━━━━━━━━━",
                                                "§7边界点 1: " + (arena.getPos1() != null ? "§a✓ 已设置" : "§c✗ 未设置"),
                                                "§7边界点 2: " + (arena.getPos2() != null ? "§a✓ 已设置" : "§c✗ 未设置"),
                                                "§7等待大厅: " + (lobbySet ? "§a✓ 已设置" : "§c✗ 未设置"),
                                                "",
                                                step2Done ? "§a✓ 边界与大厅已配置就绪！" : "§6⚠ 必须完成此步骤！",
                                                "",
                                                "§e▸ 点击进入坐标设置界面",
                                                "§7▸ 右键点击清除上述所有坐标",
                                                "§8━━━━━━━━━━━━━━━━━━━━━━━")
                                .build());

                int teamCount = arena.getTeams().size();
                boolean teamsReady = teamCount >= 2 && arena.getTeams().stream()
                                .allMatch(t -> t.getSpawnLocation() != null && t.getBedLocation() != null);
                inventory.setItem(12, new ItemBuilder(teamsReady ? com.cryptomorin.xseries.XMaterial.WHITE_BANNER
                                : com.cryptomorin.xseries.XMaterial.BLACK_BANNER)
                                .setName(teamsReady ? "§a§l✓ §f队伍配置" : "§6§l⚠ §f队伍配置")
                                .setLore(
                                                "§8━━━━━━━━━━━━━━━━━━━━━━━",
                                                "§7当前队伍数: §e" + teamCount,
                                                "",
                                                step2Done
                                                                ? (teamsReady ? "§a✓ 所有队伍配置完整！"
                                                                                : "§6⚠ 请配置队伍出生点与床！")
                                                                : "§c✗ 请先完成边界与大厅步骤！",
                                                "",
                                                step2Done ? "§e▸ 点击管理队伍"
                                                                : "§c▸ 已锁定 - 请先完成第 2 步！",
                                                "§8━━━━━━━━━━━━━━━━━━━━━━━")
                                .build());

                int diamondCount = arena.getDiamondGenerators().size();
                int emeraldCount = arena.getEmeraldGenerators().size();
                boolean gensSet = diamondCount > 0 || emeraldCount > 0;
                inventory.setItem(13, new ItemBuilder(com.cryptomorin.xseries.XMaterial.DIAMOND)
                                .setName(gensSet ? "§b§l✓ §f全局资源生成点" : "§6§l⚠ §f全局资源生成点")
                                .setLore(
                                                "§8━━━━━━━━━━━━━━━━━━━━━━━",
                                                "§b钻石刷新点: §f" + diamondCount,
                                                "§a绿宝石刷新点: §f" + emeraldCount,
                                                "",
                                                gensSet ? "§a✓ 全局生成点已配置！" : "§6⚠ 请在地图中添加生成点！",
                                                "",
                                                "§e▸ 点击管理生成点",
                                                "§8━━━━━━━━━━━━━━━━━━━━━━━")
                                .build());

                long shopsSet = arena.getTeams().stream().filter(t -> t.getShopLocation() != null).count();
                inventory.setItem(19, new ItemBuilder(com.cryptomorin.xseries.XMaterial.VILLAGER_SPAWN_EGG)
                                .setName("§a§l商店 NPC 配置")
                                .setLore(
                                                "§8━━━━━━━━━━━━━━━━━━━━━━━",
                                                "§7已配置: §e" + shopsSet + "/" + teamCount,
                                                "",
                                                "§e▸ 点击管理商店 NPC",
                                                "§7▸ 支持自动检测与手动设置",
                                                "§8━━━━━━━━━━━━━━━━━━━━━━━")
                                .build());

                inventory.setItem(21, new ItemBuilder(com.cryptomorin.xseries.XMaterial.COMPARATOR)
                                .setName("§6§l⚙ §f常规游戏设置")
                                .setLore(
                                                "§8━━━━━━━━━━━━━━━━━━━━━━━",
                                                "§7最小人数: §e" + arena.getMinPlayers(),
                                                "§7最大人数: §e" + arena.getMaxPlayers(),
                                                "§7模式: §b" + arena.getMode().getDisplayName(),
                                                "§7PvP 机制: §a" + arena.getPvpMode().getDisplayName(),
                                                "",
                                                "§e▸ 点击进行调整",
                                                "§8━━━━━━━━━━━━━━━━━━━━━━━")
                                .build());

                boolean multiModeEnabled = BedWars.getInstance().getConfig()
                                .getBoolean("join-gui.allow-multi-mode-arenas", true);
                inventory.setItem(22, new ItemBuilder(multiModeEnabled ? com.cryptomorin.xseries.XMaterial.LIME_CONCRETE : com.cryptomorin.xseries.XMaterial.GRAY_CONCRETE)
                                .setName("§b§l房间模式动态复用")
                                .setLore(
                                                "§8━━━━━━━━━━━━━━━━━━━━━━━",
                                                "§7空闲的等待房间可根据",
                                                "§7玩家选择的模式动态复用。",
                                                "",
                                                "§7当前状态: " + (multiModeEnabled ? "§a开启" : "§c关闭"),
                                                "",
                                                "§e▸ 点击切换全局开关",
                                                "§8━━━━━━━━━━━━━━━━━━━━━━━")
                                .build());

                boolean enabled = arena.isEnabled();
                inventory.setItem(23,
                                new ItemBuilder(enabled ? com.cryptomorin.xseries.XMaterial.LIME_DYE
                                                : com.cryptomorin.xseries.XMaterial.GRAY_DYE)
                                                .setName(enabled ? "§a§l✓ §f房间状态: 已启用" : "§c§l✗ §f房间状态: 已禁用")
                                                .setLore(
                                                                "§8━━━━━━━━━━━━━━━━━━━━━━━",
                                                                "§7状态: " + (enabled ? "§a已启用 (ENABLED)" : "§c已禁用 (DISABLED)"),
                                                                "",
                                                                enabled
                                                                                ? "§a玩家可以正常加入该房间"
                                                                                : "§c玩家无法加入该房间",
                                                                "",
                                                                "§e▸ 点击切换启用状态",
                                                                "§8━━━━━━━━━━━━━━━━━━━━━━━")
                                                .build());

                inventory.setItem(25, new ItemBuilder(com.cryptomorin.xseries.XMaterial.STICK)
                                .setName("§e§l⚡ §f选区魔杖")
                                .setLore(
                                                "§8━━━━━━━━━━━━━━━━━━━━━━━",
                                                "§7用于圈定队伍基地的保护区域",
                                                "",
                                                "§a左键点击: §7设置对角点 1",
                                                "§a右键点击: §7设置对角点 2",
                                                "",
                                                "§e▸ 点击获取魔杖",
                                                "§8━━━━━━━━━━━━━━━━━━━━━━━")
                                .build());

                boolean isReady = worldSet && step2Done && teamsReady;
                inventory.setItem(31, new ItemBuilder(isReady ? com.cryptomorin.xseries.XMaterial.EMERALD_BLOCK : com.cryptomorin.xseries.XMaterial.COAL_BLOCK)
                                .setName(isReady ? "§a§l✓ §f完成并启用房间" : "§c§l✗ §f尚未就绪")
                                .setLore(
                                                "§8━━━━━━━━━━━━━━━━━━━━━━━",
                                                isReady ? "§a所有配置项均已达标！" : "§c请先完成以下必须项:",
                                                "",
                                                worldSet ? "§a✓ §7世界已选择" : "§c✗ §7尚未绑定世界",
                                                step2Done ? "§a✓ §7边界与大厅已配置" : "§c✗ §7缺少边界坐标或等待大厅",
                                                teamsReady ? "§a✓ §7队伍已完整配置" : "§c✗ §7队伍不足2个或缺少床/出生点",
                                                "",
                                                isReady ? "§e▸ 点击保存并启用房间" : "§c请先完成上述未勾选的步骤！",
                                                "§8━━━━━━━━━━━━━━━━━━━━━━━")
                                .build());

                inventory.setItem(33, new ItemBuilder(com.cryptomorin.xseries.XMaterial.BARRIER)
                                .setName("§c§l✖ §f退出配置")
                                .setLore(
                                                "§7关闭菜单并退出编辑状态",
                                                "",
                                                "§c▸ 点击退出")
                                .build());

                for (int i = 36; i < 45; i++) {
                        inventory.setItem(i, new ItemBuilder(com.cryptomorin.xseries.XMaterial.BLACK_STAINED_GLASS_PANE)
                                        .setName(" ").build());
                }

                for (int i = 0; i < inventory.getSize(); i++) {
                        if (inventory.getItem(i) == null) {
                                inventory.setItem(i,
                                                new ItemBuilder(com.cryptomorin.xseries.XMaterial.GRAY_STAINED_GLASS_PANE)
                                                                .setName(" ").build());
                        }
                }
        }

        @Override
        public void handleAction(Player player, int slot, ItemStack item,
                        org.bukkit.event.inventory.ClickType clickType) {
                if (item == null || !item.hasItemMeta())
                        return;

                switch (slot) {
                        case 10:
                                if (clickType.isRightClick()) {
                                        arena.setWorldName(null);
                                        player.sendMessage(BedWars.getInstance().getLanguageManager().getMessage(player.getUniqueId(), "arena-world-cleared"));
                                        BedWars.getInstance().getArenaManager().saveArena(arena);
                                        open(player);
                                } else {
                                        new WorldSelectorGUI(arena).open(player);
                                }
                                break;
                        case 11:
                                if (clickType.isRightClick()) {
                                        arena.setPos1(null);
                                        arena.setPos2(null);
                                        arena.setLobbyLocation(null);
                                        player.sendMessage(BedWars.getInstance().getLanguageManager().getMessage(player.getUniqueId(), "arena-positions-cleared"));
                                        BedWars.getInstance().getArenaManager().saveArena(arena);
                                        open(player);
                                } else {
                                        new ArenaLocationsGUI(arena).open(player);
                                }
                                break;
                        case 12:
                                boolean b2 = arena.getPos1() != null && arena.getPos2() != null
                                                && arena.getLobbyLocation() != null;
                                if (b2) {
                                        new TeamManagementGUI(arena).open(player);
                                } else {
                                        player.sendMessage(BedWars.getInstance().getLanguageManager().getMessage(player.getUniqueId(), "arena-bounds-required"));
                                }
                                break;
                        case 13: 
                                new GeneratorManagementGUI(arena).open(player);
                                break;
                        case 19:
                                new ShopManagementGUI(arena).open(player);
                                break;
                        case 21:
                                new ArenaGeneralSettingsGUI(arena).open(player);
                                break;
                        case 22:
                                boolean current = BedWars.getInstance().getConfig()
                                                .getBoolean("join-gui.allow-multi-mode-arenas", true);
                                BedWars.getInstance().getConfig().set("join-gui.allow-multi-mode-arenas", !current);
                                BedWars.getInstance().saveConfig();
                                player.sendMessage("§7[BedWars] Join mode reuse: " + (!current ? "§aON" : "§cOFF"));
                                open(player);
                                break;
                        case 23:
                                arena.setEnabled(!arena.isEnabled());
                                player.sendMessage(arena.isEnabled()
                                                ? BedWars.getInstance().getLanguageManager().getMessage(player.getUniqueId(), "setup-arena-enabled")
                                                : BedWars.getInstance().getLanguageManager().getMessage(player.getUniqueId(), "setup-arena-disabled"));
                                BedWars.getInstance().getArenaManager().saveArena(arena);
                                open(player);
                                break;
                        case 25:
                                giveSelectionWand(player);
                                break;
                        case 31: 
                                boolean wSet = arena.getWorldName() != null;
                                boolean sReady = arena.getPos1() != null && arena.getPos2() != null
                                                && arena.getLobbyLocation() != null;
                                boolean tReady = arena.getTeams().size() >= 2 && arena.getTeams().stream()
                                                .allMatch(t -> t.getSpawnLocation() != null
                                                                && t.getBedLocation() != null);

                                if (wSet && sReady && tReady) {
                                        arena.setEnabled(true);
                                        BedWars.getInstance().getArenaManager().saveArena(arena);
                                        BedWars.getInstance().getArenaManager().stopEditing(player);
                                        player.sendMessage(BedWars.getInstance().getLanguageManager().getMessage(player.getUniqueId(), "arena-ready")
                                                        .replace("%name%", arena.getName()));
                                        player.closeInventory();
                                } else {
                                        player.sendMessage(BedWars.getInstance().getLanguageManager().getMessage(player.getUniqueId(), "arena-setup-incomplete"));
                                }
                                break;
                        case 33:
                                BedWars.getInstance().getArenaManager().stopEditing(player);
                                player.closeInventory();
                                break;
                }
        }

        private void giveSelectionWand(Player player) {
                ItemStack stick = new ItemStack(Material.STICK);
                org.bukkit.inventory.meta.ItemMeta meta = stick.getItemMeta();
                if (meta != null) {
                        meta.setDisplayName("§e§l基地选区工具 (木棒)");
                        java.util.List<String> lore = new java.util.ArrayList<>();
                        lore.add("§7房间: §b" + arena.getName());
                        lore.add("");
                        lore.add("§a左键: §7设置点 1");
                        lore.add("§a右键: §7设置点 2");
                        lore.add("§7(自动检测最近的队伍)");
                        meta.setLore(lore);
                        stick.setItemMeta(meta);
                }
                me.horiciastko.bedwars.utils.ItemTagUtils.setTag(stick, "special_item", "base_selection_tool");
                player.getInventory().addItem(stick);
                player.sendMessage(BedWars.getInstance().getLanguageManager().getMessage(player.getUniqueId(), "arena-wand-received"));
                player.closeInventory();
        }
}
