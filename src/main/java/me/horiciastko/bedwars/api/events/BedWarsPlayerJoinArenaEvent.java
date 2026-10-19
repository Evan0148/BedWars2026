package me.horiciastko.bedwars.api.events;

import lombok.Getter;
import me.horiciastko.bedwars.models.Arena;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Fired when a player joins an arena as a player (not spectator).
 */
@Getter
public class BedWarsPlayerJoinArenaEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final Arena arena;

    public BedWarsPlayerJoinArenaEvent(Player player, Arena arena) {
        this.player = player;
        this.arena = arena;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    @SuppressWarnings("unused")
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
