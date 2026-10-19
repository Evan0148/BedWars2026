package me.horiciastko.bedwars.api.events;

import lombok.Getter;
import me.horiciastko.bedwars.models.Arena;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Fired when an arena starts its game (state becomes IN_GAME).
 * Teams are already assigned, NPCs are spawned and players teleported.
 */
@Getter
public class BedWarsGameStartEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Arena arena;

    public BedWarsGameStartEvent(Arena arena) {
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
