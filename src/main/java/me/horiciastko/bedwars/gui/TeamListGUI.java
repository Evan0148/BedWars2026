package me.horiciastko.bedwars.gui;

import me.horiciastko.bedwars.BedWars;
import me.horiciastko.bedwars.models.Arena;
import me.horiciastko.bedwars.models.Team;
import me.horiciastko.bedwars.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TeamListGUI extends BaseGUI {

    private final Arena arena;

    public TeamListGUI(Arena arena) {
        super("§8管理队伍列表", 6);
        this.arena = arena;
    }

    @Override
    public void setContents(Player player) {
        int slot = 0;
        for (Team team : arena.getTeams()) {
            // Row 6 (slots 45-53) is reserved for navigation buttons — skip it.
            if (slot >= 45) {
                slot = -1; // sentinel: no more room
                break;
            }
            ItemStack teamItem = new ItemBuilder(team.getMaterial())
                    .setName(team.getColor() + team.getDisplayName())
                    .setLore(
                            "§7队伍颜色: " + team.getColor().name(),
                            "§7出生点: " + (team.getSpawnLocation() != null ? "§a已设置" : "§c未设置"),
                            "§7床坐标: " + (team.getBedLocation() != null ? "§a已设置" : "§c未设置"),
                            "",
                            "§e左键点击编辑该队伍",
                            "§c右键点击删除该队伍")
                    .build();

            if (teamItem.getItemMeta() != null) {
                me.horiciastko.bedwars.utils.ItemTagUtils.setTag(teamItem, "team_name", team.getName());
            }
            inventory.setItem(slot++, teamItem);
        }

        inventory.setItem(53, new ItemBuilder(Material.EMERALD)
                .setName("§a添加新队伍")
                .setLore("§7点击创建一支新队伍")
                .build());

        inventory.setItem(45, new ItemBuilder(Material.ARROW).setName("§c返回上一级").build());
    }

    @Override
    public void handleAction(Player player, int slot, ItemStack item, org.bukkit.event.inventory.ClickType clickType) {
        if (item == null || !item.hasItemMeta())
            return;

        if (slot == 45) { 
            new ArenaSettingsGUI(arena).open(player);
            return;
        }

        if (slot == 53) {
            new TeamColorSelectorGUI(arena).open(player);
            return;
        }

        String teamName = me.horiciastko.bedwars.utils.ItemTagUtils.getTag(item, "team_name");

        if (teamName != null) {
            Team team = arena.getTeams().stream()
                    .filter(t -> t.getName().equalsIgnoreCase(teamName))
                    .findFirst()
                    .orElse(null);

            if (team != null) {
                if (clickType.isRightClick()) {
                    arena.getTeams().remove(team);
                    BedWars.getInstance().getArenaManager().saveArena(arena);
                    setContents(player);
                    player.sendMessage(BedWars.getInstance().getLanguageManager().getMessage(player.getUniqueId(), "team-removed-short"));
                } else {
                    new TeamSettingsGUI(arena, team).open(player);
                }
            }
        }
    }
}
