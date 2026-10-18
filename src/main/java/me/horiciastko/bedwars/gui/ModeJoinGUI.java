package me.horiciastko.bedwars.gui;

import com.cryptomorin.xseries.XMaterial;
import me.horiciastko.bedwars.BedWars;
import me.horiciastko.bedwars.models.Arena;
import me.horiciastko.bedwars.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class ModeJoinGUI extends BaseGUI {

    private final BedWars plugin;
    private final Arena.ArenaMode mode;

    public ModeJoinGUI(Arena.ArenaMode mode) {
        super("&8起床战争 " + readableMode(mode), 3);
        this.plugin = BedWars.getInstance();
        this.mode = mode;
    }

    @Override
    public void setContents(Player player) {
        ItemStack filler = new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE).setName(" ").build();
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, filler);
        }

        int online = (int) plugin.getArenaManager().getArenas().stream()
                .filter(a -> canUseArenaForMode(a, mode))
                .flatMap(a -> a.getPlayers().stream())
                .count();

        inventory.setItem(11, new ItemBuilder(XMaterial.RED_BED)
                .setName("&a起床战争 (" + readableMode(mode) + ")")
                .setLore(
                        "&7加入 " + readableMode(mode) + " 比赛",
                        " ",
                        "&7当前在线: &f" + online,
                        " ",
                        "&e点击加入游戏！")
                .build());

        inventory.setItem(13, new ItemBuilder(XMaterial.OAK_SIGN)
                .setName("&a地图选择器 (" + readableMode(mode) + ")")
                .setLore(
                        "&7从可用的游戏房间中",
                        "&7选择你想玩的地图",
                        " ",
                        "&e点击查看房间列表！")
                .build());

        boolean canRejoin = player.hasPermission("bedwars.command.rejoin")
                && plugin.getArenaManager().getLastArena(player.getUniqueId()) != null;
        inventory.setItem(15, new ItemBuilder(canRejoin ? XMaterial.ENDER_PEARL : XMaterial.GRAY_DYE)
                .setName(canRejoin ? "&c点击重连比赛！" : "&7暂无进行中的比赛")
                .setLore(canRejoin
                        ? java.util.Arrays.asList(
                                "&7点击此处重新连接到",
                                "&7你之前意外掉线的",
                                "&7起床战争游戏。")
                        : java.util.Arrays.asList(
                                "&7你当前没有任何进行中",
                                "&7的比赛可供重新连接。"))
                .build());

        inventory.setItem(22, new ItemBuilder(XMaterial.BARRIER)
                .setName("&c关闭")
                .setLore("&7返回大厅视图")
                .build());
    }

    @Override
    public void handleAction(Player player, int slot, ItemStack item, ClickType clickType) {
        if (item == null) {
            return;
        }

        if (slot == 11) {
            joinQuick(player);
            return;
        }

        if (slot == 13) {
            new ArenaSelectorGUI(mode).open(player);
            return;
        }

        if (slot == 15) {
            if (!player.hasPermission("bedwars.command.rejoin")) {
                player.sendMessage(plugin.getLanguageManager().getMessage(player.getUniqueId(), "admin-no-permission"));
                return;
            }

            String lastArenaName = plugin.getArenaManager().getLastArena(player.getUniqueId());
            if (lastArenaName == null) {
                player.sendMessage(plugin.getLanguageManager().getMessage(player.getUniqueId(), "rejoin-no-arena"));
                return;
            }

            Arena arena = plugin.getArenaManager().getArena(lastArenaName);
            if (arena == null) {
                player.sendMessage(plugin.getLanguageManager().getMessage(player.getUniqueId(), "rejoin-arena-not-exists"));
                return;
            }

            plugin.getArenaManager().joinArena(player, arena);
            return;
        }

        if (slot == 22) {
            player.closeInventory();
        }
    }

    private void joinQuick(Player player) {
        List<Arena> available = plugin.getArenaManager().getArenas().stream()
                .filter(Arena::isEnabled)
                .filter(a -> a.getState() == Arena.GameState.WAITING || a.getState() == Arena.GameState.STARTING)
                .filter(a -> a.getPlayers().size() < a.getMaxPlayers())
                .filter(a -> canUseArenaForMode(a, mode))
                .sorted((a1, a2) -> Integer.compare(a2.getPlayers().size(), a1.getPlayers().size()))
                .collect(Collectors.toList());

        if (available.isEmpty()) {
            player.sendMessage(plugin.getLanguageManager().getMessage(player.getUniqueId(), "join-no-arenas-mode")
                    .replace("%mode%", mode.getDisplayName()));
            return;
        }

        Arena target = available.get(0);
        if (plugin.getConfig().getBoolean("join-gui.allow-multi-mode-arenas", true)
                && target.getState() == Arena.GameState.WAITING && target.getPlayers().isEmpty()) {
            target.setMode(mode);
        }

        plugin.getArenaManager().joinArena(player, target);
    }

    private boolean canUseArenaForMode(Arena arena, Arena.ArenaMode requestedMode) {
        if (arena == null || requestedMode == null) {
            return false;
        }

        if (!plugin.getConfig().getBoolean("join-gui.allow-multi-mode-arenas", true)) {
            return arena.getMode() == requestedMode;
        }

        if (arena.getState() == Arena.GameState.WAITING && arena.getPlayers().isEmpty()) {
            return true;
        }

        return arena.getMode() == requestedMode;
    }

    public static String readableMode(Arena.ArenaMode mode) {
        if (mode == null) {
            return "起床战争";
        }
        switch (mode) {
            case SOLO:
                return "单人模式";
            case DUO:
                return "双人模式";
            case TRIO:
                return "三人模式";
            case SQUAD:
                return "四人模式";
            default:
                return ChatColor.stripColor(mode.getDisplayName());
        }
    }
}
