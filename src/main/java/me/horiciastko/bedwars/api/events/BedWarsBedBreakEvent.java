package me.horiciastko.bedwars.api.events;

import lombok.Getter;
import me.horiciastko.bedwars.models.Arena;
import me.horiciastko.bedwars.models.Team;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Fired when a team's bed is destroyed by a player.
 */
@Getter
public class BedWarsBedBreakEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Arena arena;
    private final Team victimTeam;
    private final Player attacker;

    public BedWarsBedBreakEvent(Arena arena, Team victimTeam, Player attacker) {
        this.arena = arena;
        this.victimTeam = victimTeam;
        this.attacker = attacker;
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
