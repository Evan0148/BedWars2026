package me.horiciastko.bedwars.commands;

import me.horiciastko.bedwars.BedWars;
import me.horiciastko.bedwars.models.Arena;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StartCommand implements SubCommand {

    private final BedWars plugin;

    public StartCommand(BedWars plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "start";
    }

    @Override
    public String getDescription() {
        return "强制开始游戏 (管理员)";
    }

    @Override
    public String getSyntax() {
        return "/bw start [地图名]";
    }

    @Override
    public void perform(Player player, String[] args) {
        if (!player.hasPermission("bedwars.admin")) {
            player.sendMessage(plugin.getLanguageManager().getMessage(player.getUniqueId(), "admin-no-permission"));
            return;
        }

        Arena arena = null;
        if (args.length >= 2) {
            String arenaName = args[1];
            arena = plugin.getArenaManager().getArena(arenaName);
            if (arena == null) {
                player.sendMessage(plugin.getLanguageManager().getMessage(player.getUniqueId(), "admin-arena-not-found")
                        .replace("%name%", arenaName));
                return;
            }
        } else {
            arena = plugin.getArenaManager().getPlayerArena(player);
            if (arena == null) {
                player.sendMessage(plugin.getLanguageManager().getMessage(player.getUniqueId(), "admin-start-usage"));
                return;
            }
        }

        if (arena.getPlayers().isEmpty()) {
            player.sendMessage(plugin.getLanguageManager().getMessage(player.getUniqueId(), "admin-start-no-players")
                    .replace("%name%", arena.getName()));
            return;
        }

        plugin.getGameManager().forceStart(arena);
        player.sendMessage(plugin.getLanguageManager().getMessage(player.getUniqueId(), "admin-start-forced")
                .replace("%name%", arena.getName()));
        try {
            player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.5f);
        } catch (Throwable ignored) {}
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        if (!player.hasPermission("bedwars.admin")) {
            return new ArrayList<>();
        }
        if (args.length == 2) {
            return plugin.getArenaManager().getArenas().stream()
                    .map(Arena::getName)
                    .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
