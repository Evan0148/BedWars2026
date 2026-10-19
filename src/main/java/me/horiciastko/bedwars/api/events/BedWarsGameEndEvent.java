package me.horiciastko.bedwars.api.events;

import lombok.Getter;
import me.horiciastko.bedwars.models.Arena;
import me.horiciastko.bedwars.models.Team;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Fired once when an arena ends the game (state becomes ENDING).
 * winner may be null if the game ended in a draw (no alive teams).
 */
@Getter
public class BedWarsGameEndEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Arena arena;
    private final Team winner;

    public BedWarsGameEndEvent(Arena arena, Team winner) {
        this.arena = arena;
        this.winner = winner;
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
