package me.horiciastko.bedwars.gui;

import com.cryptomorin.xseries.XMaterial;
import me.horiciastko.bedwars.BedWars;
import me.horiciastko.bedwars.models.Arena;
import me.horiciastko.bedwars.models.Team;
import me.horiciastko.bedwars.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class TeamSelectGUI extends BaseGUI {

    private final Arena arena;
    private final Map<Integer, Team> slotTeamMap = new HashMap<>();
    private int autoAssignSlot = -1;

    public TeamSelectGUI(Arena arena) {
        super(resolveTitle(), calculateRows(arena));
        this.arena = arena;
    }

    private static String resolveTitle() {
        String title = BedWars.getInstance().getLanguageManager().getMessage(null, "team-select-gui-title");
        if (title == null || title.startsWith("§cMissing path:")) {
            return "&8选择队伍";
        }
        return title;
    }

    private static int calculateRows(Arena arena) {
        if (arena == null) {
            return 3;
        }
        int count = arena.getTeams().size();
        if (count <= 4) {
            return 3;
        } else if (count <= 8) {
            return 4;
        } else {
            return 5;
        }
    }

    public static XMaterial getTeamBed(Team team) {
        if (team == null) {
            return XMaterial.WHITE_BED;
        }
        String colorName = team.getColor().name();
        switch (colorName) {
            case "RED":
                return XMaterial.RED_BED;
            case "BLUE":
                return XMaterial.BLUE_BED;
            case "GREEN":
                return XMaterial.LIME_BED;
            case "YELLOW":
                return XMaterial.YELLOW_BED;
            case "AQUA":
                return XMaterial.CYAN_BED;
            case "WHITE":
                return XMaterial.WHITE_BED;
            case "LIGHT_PURPLE":
                return XMaterial.PINK_BED;
            case "DARK_GRAY":
            case "GRAY":
                return XMaterial.GRAY_BED;
            case "GOLD":
                return XMaterial.ORANGE_BED;
            case "DARK_PURPLE":
                return XMaterial.PURPLE_BED;
            case "DARK_BLUE":
                return XMaterial.BLUE_BED;
            case "DARK_GREEN":
                return XMaterial.GREEN_BED;
            case "DARK_AQUA":
                return XMaterial.CYAN_BED;
            default:
                return XMaterial.matchXMaterial(colorName + "_BED").orElse(XMaterial.WHITE_BED);
        }
    }

    @Override
    public void setContents(Player player) {
        slotTeamMap.clear();

        ItemStack glass = new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE).setName(" ").build();
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, glass);
        }

        if (arena == null) {
            return;
        }

        List<Team> teams = arena.getTeams();
        int teamCount = teams.size();
        int[] teamSlots = calculateSlots(teamCount);

        Team preSelected = BedWars.getInstance().getGameManager().getPreSelectedTeam(player);
        int maxPerTeam = arena.getMode().getPlayersPerTeam();

        for (int i = 0; i < teams.size() && i < teamSlots.length; i++) {
            Team team = teams.get(i);
            int slot = teamSlots[i];
            slotTeamMap.put(slot, team);

            boolean isCurrent = team.equals(preSelected);
            List<Player> members = BedWars.getInstance().getGameManager().getPreSelectedMembers(arena, team);
            int currentCount = members.size();
            boolean isFull = (currentCount >= maxPerTeam);

            List<String> lore = new ArrayList<>();
            String playersFmt = BedWars.getInstance().getLanguageManager().getMessage(player.getUniqueId(), "team-select-lore-players");
            if (playersFmt == null || playersFmt.startsWith("§cMissing path:")) {
                playersFmt = "&7队伍人数: &f%current%/%max%";
            }
            lore.add(ChatColor.translateAlternateColorCodes('&', playersFmt
                    .replace("%current%", String.valueOf(currentCount))
                    .replace("%max%", String.valueOf(maxPerTeam))));

            String membersHeader = BedWars.getInstance().getLanguageManager().getMessage(player.getUniqueId(), "team-select-lore-members");
            if (membersHeader == null || membersHeader.startsWith("§cMissing path:")) {
                membersHeader = "&7当前成员:";
            }
            lore.add(ChatColor.translateAlternateColorCodes('&', membersHeader));

            if (members.isEmpty()) {
                String noMembers = BedWars.getInstance().getLanguageManager().getMessage(player.getUniqueId(), "team-select-lore-no-members");
                if (noMembers == null || noMembers.startsWith("§cMissing path:")) {
                    noMembers = "  &8- &7(暂无成员)";
                }
                lore.add(ChatColor.translateAlternateColorCodes('&', noMembers));
            } else {
                for (Player m : members) {
                    lore.add(ChatColor.translateAlternateColorCodes('&', "  &8- &f" + m.getName()));
                }
            }

            lore.add("");
            if (isCurrent) {
                String joinedText = BedWars.getInstance().getLanguageManager().getMessage(player.getUniqueId(), "team-select-lore-joined");
                if (joinedText == null || joinedText.startsWith("§cMissing path:")) {
                    joinedText = "&a✔ 你已加入此队伍";
                }
                lore.add(ChatColor.translateAlternateColorCodes('&', joinedText));
            } else if (isFull) {
                String fullText = BedWars.getInstance().getLanguageManager().getMessage(player.getUniqueId(), "team-select-lore-full");
                if (fullText == null || fullText.startsWith("§cMissing path:")) {
                    fullText = "&c✖ 队伍已满员";
                }
                lore.add(ChatColor.translateAlternateColorCodes('&', fullText));
            } else {
                String clickText = BedWars.getInstance().getLanguageManager().getMessage(player.getUniqueId(), "team-select-lore-click");
                if (clickText == null || clickText.startsWith("§cMissing path:")) {
                    clickText = "&e点击加入此队伍";
                }
                lore.add(ChatColor.translateAlternateColorCodes('&', clickText));
            }

            XMaterial bedMat = getTeamBed(team);
            ItemBuilder builder = new ItemBuilder(bedMat)
                    .setName(team.getColor() + "§l" + team.getDisplayName())
                    .setLore(lore);

            inventory.setItem(slot, builder.build());
        }

        // Auto Assign / Clear item at bottom center
        autoAssignSlot = (rows == 3) ? 22 : ((rows == 4) ? 31 : 40);
        String autoName = BedWars.getInstance().getLanguageManager().getMessage(player.getUniqueId(), "team-select-auto-name");
        if (autoName == null || autoName.startsWith("§cMissing path:")) {
            autoName = "&f&l自动分配队伍";
        }
        String autoDesc = BedWars.getInstance().getLanguageManager().getMessage(player.getUniqueId(), "team-select-auto-lore-desc");
        if (autoDesc == null || autoDesc.startsWith("§cMissing path:")) {
            autoDesc = "&7清除手动选队，由系统平衡分配";
        }

        List<String> autoLore = new ArrayList<>();
        autoLore.add(ChatColor.translateAlternateColorCodes('&', autoDesc));
        autoLore.add("");
        if (preSelected == null) {
            String onText = BedWars.getInstance().getLanguageManager().getMessage(player.getUniqueId(), "team-select-auto-status-on");
            if (onText == null || onText.startsWith("§cMissing path:")) {
                onText = "&a✔ 当前状态: 自动分配";
            }
            autoLore.add(ChatColor.translateAlternateColorCodes('&', onText));
        } else {
            String offText = BedWars.getInstance().getLanguageManager().getMessage(player.getUniqueId(), "team-select-auto-status-off");
            if (offText == null || offText.startsWith("§cMissing path:")) {
                offText = "&e点击切换为自动分配";
            }
            autoLore.add(ChatColor.translateAlternateColorCodes('&', offText));
        }

        inventory.setItem(autoAssignSlot, new ItemBuilder(XMaterial.COMPASS)
                .setName(ChatColor.translateAlternateColorCodes('&', autoName))
                .setLore(autoLore)
                .build());
    }

    private int[] calculateSlots(int count) {
        switch (count) {
            case 2:
                return new int[]{12, 14};
            case 3:
                return new int[]{11, 13, 15};
            case 4:
                return new int[]{10, 12, 14, 16};
            case 5:
                return new int[]{11, 13, 15, 21, 23};
            case 6:
                return new int[]{11, 13, 15, 20, 22, 24};
            case 7:
                return new int[]{10, 12, 14, 16, 20, 22, 24};
            case 8:
                return new int[]{10, 12, 14, 16, 19, 21, 23, 25};
            default:
                int[] slots = new int[count];
                int s = 10;
                for (int i = 0; i < count; i++) {
                    slots[i] = s++;
                    if ((s % 9) == 8) s += 2;
                }
                return slots;
        }
    }

    @Override
    public void handleAction(Player player, int slot, ItemStack item, ClickType clickType) {
        if (item == null || item.getType() == Material.AIR) {
            return;
        }

        Arena currentArena = BedWars.getInstance().getArenaManager().getPlayerArena(player);
        if (currentArena == null || (currentArena.getState() != Arena.GameState.WAITING && currentArena.getState() != Arena.GameState.STARTING)) {
            player.closeInventory();
            return;
        }

        if (slot == autoAssignSlot) {
            Team current = BedWars.getInstance().getGameManager().getPreSelectedTeam(player);
            if (current == null) {
                String alreadyAuto = BedWars.getInstance().getLanguageManager().getMessage(player.getUniqueId(), "team-select-already-auto");
                if (alreadyAuto == null || alreadyAuto.startsWith("§cMissing path:")) {
                    alreadyAuto = "&e你当前已经是自动分配队伍状态。";
                }
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', alreadyAuto));
                return;
            }

            BedWars.getInstance().getGameManager().setPreSelectedTeam(player, null);
            String clearedMsg = BedWars.getInstance().getLanguageManager().getMessage(player.getUniqueId(), "team-select-cleared");
            if (clearedMsg == null || clearedMsg.startsWith("§cMissing path:")) {
                clearedMsg = "&e已取消自选队伍，将在游戏开始时自动分配。";
            }
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', clearedMsg));
            try {
                player.playSound(player.getLocation(), org.bukkit.Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
            } catch (Throwable ignored) {}

            BedWars.getInstance().getArenaManager().updateTeamSelectItem(player, currentArena);
            open(player);
            return;
        }

        Team team = slotTeamMap.get(slot);
        if (team == null) {
            return;
        }

        Team current = BedWars.getInstance().getGameManager().getPreSelectedTeam(player);
        if (team.equals(current)) {
            String alreadyIn = BedWars.getInstance().getLanguageManager().getMessage(player.getUniqueId(), "team-select-already-in");
            if (alreadyIn == null || alreadyIn.startsWith("§cMissing path:")) {
                alreadyIn = "&e你已经加入了该队伍！";
            }
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', alreadyIn));
            return;
        }

        int maxPerTeam = currentArena.getMode().getPlayersPerTeam();
        int preSelectedCount = BedWars.getInstance().getGameManager().getPreSelectedCount(currentArena, team);
        if (preSelectedCount >= maxPerTeam) {
            String fullMsg = BedWars.getInstance().getLanguageManager().getMessage(player.getUniqueId(), "team-select-full");
            if (fullMsg == null || fullMsg.startsWith("§cMissing path:")) {
                fullMsg = "&c该队伍已满员！";
            }
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', fullMsg));
            try {
                player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            } catch (Throwable ignored) {}
            return;
        }

        BedWars.getInstance().getGameManager().setPreSelectedTeam(player, team);
        String successFmt = BedWars.getInstance().getLanguageManager().getMessage(player.getUniqueId(), "team-select-success");
        if (successFmt == null || successFmt.startsWith("§cMissing path:")) {
            successFmt = "&a成功加入 %color%%team% &a队伍！";
        }
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', successFmt
                .replace("%color%", team.getColor().toString())
                .replace("%team%", team.getDisplayName())));

        try {
            player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.5f);
        } catch (Throwable ignored) {}

        BedWars.getInstance().getArenaManager().updateTeamSelectItem(player, currentArena);
        open(player);
    }
}
