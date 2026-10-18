package me.horiciastko.bedwars.gui;

import me.horiciastko.bedwars.BedWars;
import me.horiciastko.bedwars.models.Arena;
import me.horiciastko.bedwars.models.Team;
import me.horiciastko.bedwars.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class TeamManagementGUI extends BaseGUI {

    private final Arena arena;

    public TeamManagementGUI(Arena arena) {
        super("§8§l⚔ §9队伍列表: §f" + arena.getName(), 4);
        this.arena = arena;
    }

    @Override
    public void open(Player player) {
        super.open(player);
        BedWars.getInstance().getVisualizationManager().showHolograms(player, arena);
    }

    @Override
    public void setContents(Player player) {
        for (int i = 0; i < 9; i++) {
            inventory.setItem(i,
                    new ItemBuilder(com.cryptomorin.xseries.XMaterial.BLACK_STAINED_GLASS_PANE).setName(" ").build());
        }

        int slot = 9;
        for (Team team : arena.getTeams()) {
            boolean spawnSet = team.getSpawnLocation() != null;
            boolean bedSet = team.getBedLocation() != null;
            boolean shopSet = team.getShopLocation() != null;
            boolean upgradeSet = team.getUpgradeLocation() != null;
            boolean ready = spawnSet && bedSet;

            Material teamMat = team.getMaterial();
            if (!ready) {
                teamMat = Material.BARRIER;
            }

            List<String> lore = new ArrayList<>();
            lore.add("§8━━━━━━━━━━━━━━━━━━━━━━━");
            lore.add("§7颜色代号: " + team.getColor() + team.getColor().name());
            lore.add("");
            lore.add((spawnSet ? "§a✓" : "§c✗") + " §7出生点: " + (spawnSet ? "§a已设置" : "§c未设置"));
            lore.add((bedSet ? "§a✓" : "§c✗") + " §7床坐标: " + (bedSet ? "§a已设置" : "§c未设置"));
            lore.add((shopSet ? "§a✓" : "§c✗") + " §7道具商店 NPC: " + (shopSet ? "§a已设置" : "§c未设置"));
            lore.add((upgradeSet ? "§a✓" : "§c✗") + " §7团队升级 NPC: " + (upgradeSet ? "§a已设置" : "§c未设置"));
            lore.add("§7基地生成点数量: §e" + team.getGenerators().size());
            lore.add("");
            lore.add(ready ? "§a✓ 队伍已就绪！" : "§c✗ 请设置出生点与床！");
            lore.add("");
            lore.add("§e▸ 左键点击详细配置");
            lore.add("§c▸ 右键点击删除此队伍");
            lore.add("§8━━━━━━━━━━━━━━━━━━━━━━━");

            inventory.setItem(slot++, new ItemBuilder(teamMat)
                    .setName(team.getColor() + "§l" + team.getDisplayName())
                    .setLore(lore.toArray(new String[0]))
                    .build());

            if (slot >= 27)
                break;
        }

        for (int i = 27; i < 36; i++) {
            inventory.setItem(i,
                    new ItemBuilder(com.cryptomorin.xseries.XMaterial.BLACK_STAINED_GLASS_PANE).setName(" ").build());
        }

        inventory.setItem(31, new ItemBuilder(Material.NETHER_STAR)
                .setName("§a§l➕ §f添加新队伍")
                .setLore("§7点击从预设可用颜色中",
                        "§7选择并添加一支新队伍")
                .build());

        inventory.setItem(27, new ItemBuilder(Material.ARROW)
                .setName("§c§l← §f返回上一级")
                .setLore("§7返回房间主配置菜单")
                .build());

        int teamCount = arena.getTeams().size();
        inventory.setItem(35, new ItemBuilder(Material.PLAYER_HEAD)
                .setName("§e§l当前队伍数: §f" + teamCount)
                .setLore("§7最少需要: §e2 支队伍",
                        teamCount >= 2 ? "§a✓ 符合开赛要求！" : "§c✗ 队伍不足，请继续添加！")
                .build());

        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, new ItemBuilder(com.cryptomorin.xseries.XMaterial.GRAY_STAINED_GLASS_PANE)
                        .setName(" ").build());
            }
        }
    }

    @Override
    public void handleAction(Player player, int slot, ItemStack item, org.bukkit.event.inventory.ClickType clickType) {
        if (item == null || !item.hasItemMeta())
            return;
        if (slot == 27) {
            new ArenaSettingsGUI(arena).open(player);
            return;
        }

        if (slot == 31) {
            new ColorSelectorGUI(arena).open(player);
            return;
        }

        if (slot >= 9 && slot < 27) {
            int teamIndex = slot - 9;
            if (teamIndex < arena.getTeams().size()) {
                Team team = arena.getTeams().get(teamIndex);
                if (clickType.isRightClick()) {
                    arena.getTeams().remove(team);
                    BedWars.getInstance().getArenaManager().saveArena(arena);
                    player.sendMessage(BedWars.getInstance().getLanguageManager().getMessage(player.getUniqueId(), "team-removed").replace("%color%", team.getColor().toString()).replace("%name%", team.getDisplayName()));
                    open(player);
                } else {
                    new TeamSettingsGUI(arena, team).open(player);
                }
            }
        }
    }

    private static class ColorSelectorGUI extends BaseGUI {
        private final Arena arena;
        private final List<ColorOption> options = new ArrayList<>();

        public ColorSelectorGUI(Arena arena) {
            super("§8§l➕ §aSelect Team Color", 2);
            this.arena = arena;
            setupOptions();
        }

        private void setupOptions() {
            org.bukkit.configuration.file.FileConfiguration config = BedWars.getInstance().getConfigManager()
                    .getTeamConfig();
            if (config == null || config.getConfigurationSection("teams") == null) {
                options.add(new ColorOption("red", "Red", ChatColor.RED,
                        com.cryptomorin.xseries.XMaterial.RED_WOOL.parseMaterial()));
                options.add(new ColorOption("blue", "Blue", ChatColor.BLUE,
                        com.cryptomorin.xseries.XMaterial.BLUE_WOOL.parseMaterial()));
                options.add(new ColorOption("green", "Green", ChatColor.GREEN,
                        com.cryptomorin.xseries.XMaterial.LIME_WOOL.parseMaterial()));
                options.add(new ColorOption("yellow", "Yellow", ChatColor.YELLOW,
                        com.cryptomorin.xseries.XMaterial.YELLOW_WOOL.parseMaterial()));
                options.add(new ColorOption("aqua", "Aqua", ChatColor.AQUA,
                        com.cryptomorin.xseries.XMaterial.CYAN_WOOL.parseMaterial()));
                options.add(new ColorOption("white", "White", ChatColor.WHITE,
                        com.cryptomorin.xseries.XMaterial.WHITE_WOOL.parseMaterial()));
                options.add(new ColorOption("pink", "Pink", ChatColor.LIGHT_PURPLE,
                        com.cryptomorin.xseries.XMaterial.PINK_WOOL.parseMaterial()));
                options.add(new ColorOption("gray", "Gray", ChatColor.GRAY,
                        com.cryptomorin.xseries.XMaterial.GRAY_WOOL.parseMaterial()));
                return;
            }

            for (String key : config.getConfigurationSection("teams").getKeys(false)) {
                String displayName = config.getString("teams." + key + ".display_name", key);
                ChatColor color = ChatColor.valueOf(config.getString("teams." + key + ".color", "WHITE"));
                Material material = com.cryptomorin.xseries.XMaterial
                        .matchXMaterial(config.getString("teams." + key + ".material", "WHITE_WOOL"))
                        .orElse(com.cryptomorin.xseries.XMaterial.WHITE_WOOL).parseMaterial();
                options.add(new ColorOption(key, displayName, color, material));
            }
        }

        @Override
        public void setContents(Player player) {
            int slot = 0;
            for (ColorOption option : options) {
                boolean taken = arena.getTeams().stream()
                        .anyMatch(t -> t.getName().equalsIgnoreCase(option.name));

                if (taken) {
                    inventory.setItem(slot++, new ItemBuilder(Material.BARRIER)
                            .setName("§c§m" + option.displayName + " §8(Already added)")
                            .setLore("§7This team already exists")
                            .build());
                } else {
                    inventory.setItem(slot++, new ItemBuilder(option.material)
                            .setName(option.color + "§l" + option.displayName)
                            .setLore("§e▸ Click to add this team")
                            .build());
                }

                if (slot >= inventory.getSize())
                    break;
            }

            for (int i = slot; i < inventory.getSize(); i++) {
                inventory.setItem(i, new ItemBuilder(com.cryptomorin.xseries.XMaterial.GRAY_STAINED_GLASS_PANE)
                        .setName(" ").build());
            }
        }

        @Override
        public void handleAction(Player player, int slot, ItemStack item,
                org.bukkit.event.inventory.ClickType clickType) {
            if (item == null || item.getType() == Material.BARRIER
                    || item.getType() == Material.GRAY_STAINED_GLASS_PANE)
                return;

            if (slot < options.size()) {
                ColorOption option = options.get(slot);
                boolean taken = arena.getTeams().stream()
                        .anyMatch(t -> t.getName().equalsIgnoreCase(option.name));

                if (!taken) {
                    Team team = new Team(option.name, option.displayName, option.color, option.material);
                    arena.getTeams().add(team);
                    BedWars.getInstance().getArenaManager().saveArena(arena);
                    player.sendMessage(BedWars.getInstance().getLanguageManager().getMessage(player.getUniqueId(), "team-added").replace("%color%", option.color.toString()).replace("%name%", option.displayName));
                    new TeamManagementGUI(arena).open(player);
                }
            }
        }

        private static class ColorOption {
            public final String name;
            public final String displayName;
            public final ChatColor color;
            public final Material material;

            public ColorOption(String name, String displayName, ChatColor color, Material material) {
                this.name = name;
                this.displayName = displayName;
                this.color = color;
                this.material = material;
            }
        }
    }
}
