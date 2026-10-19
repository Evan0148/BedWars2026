package me.horiciastko.bedwars.api.events;

import lombok.Getter;
import lombok.Setter;
import me.horiciastko.bedwars.models.Arena;
import me.horiciastko.bedwars.models.Team;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Fired when a player dies in a running game (before the death message is broadcast).
 * killer/killerTeam may be null for environmental deaths (e.g. falling into the void).
 * finalKill is true when the victim's bed was already broken.
 * Listeners may override the broadcast message via setCustomMessage(String).
 */
@Getter
public class BedWarsPlayerKillEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Arena arena;
    private final Player victim;
    private final Team victimTeam;
    private final Player killer;
    private final Team killerTeam;
    private final boolean finalKill;
    private final String killReason;
    private final String killMessage;

    @Setter
    private String customMessage;

    public BedWarsPlayerKillEvent(Arena arena, Player victim, Team victimTeam, Player killer,
                                  Team killerTeam, boolean finalKill, String killReason, String killMessage) {
        this.arena = arena;
        this.victim = victim;
        this.victimTeam = victimTeam;
        this.killer = killer;
        this.killerTeam = killerTeam;
        this.finalKill = finalKill;
        this.killReason = killReason;
        this.killMessage = killMessage;
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
