package me.horiciastko.bedwars.api.events;

import lombok.Getter;
import me.horiciastko.bedwars.models.Arena;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

/**
 * Fired when a player successfully purchases an item from the shop,
 * after team colouring was applied and before the item is delivered.
 * The purchased stack reference is mutable - listeners may modify it
 * (e.g. recolour wooden blocks) before it reaches the player's inventory.
 */
@Getter
public class BedWarsShopPurchaseEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player buyer;
    private final Arena arena;
    private final ItemStack purchasedStack;

    public BedWarsShopPurchaseEvent(Player buyer, Arena arena, ItemStack purchasedStack) {
        this.buyer = buyer;
        this.arena = arena;
        this.purchasedStack = purchasedStack;
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
