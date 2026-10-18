package me.horiciastko.bedwars.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InventoryUtils {

    public static boolean hasEnough(Player player, Material material, int amount) {
        if (amount <= 0)
            return true;
        int count = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == material) {
                count += item.getAmount();
                if (count >= amount)
                    return true;
            }
        }
        return false;
    }

    public static int countItems(Player player, Material material) {
        if (material == null)
            return 0;
        int count = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == material) {
                count += item.getAmount();
            }
        }
        return count;
    }

    public static void removeItems(Player player, Material material, int amount) {
        if (amount <= 0)
            return;
        ItemStack[] contents = player.getInventory().getContents();
        for (int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];
            if (item != null && item.getType() == material) {
                if (item.getAmount() > amount) {
                    item.setAmount(item.getAmount() - amount);
                    break;
                } else {
                    amount -= item.getAmount();
                    contents[i] = null;
                    if (amount <= 0)
                        break;
                }
            }
        }
        player.getInventory().setContents(contents);
    }

    public static boolean canFit(Player player, ItemStack item) {
        if (item == null || item.getType() == Material.AIR)
            return true;

        ItemStack[] contents = player.getInventory().getStorageContents();
        int remaining = item.getAmount();

        for (ItemStack slot : contents) {
            if (slot == null || slot.getType() == Material.AIR) {
                return true;
            }
            if (slot.isSimilar(item)) {
                remaining -= (slot.getMaxStackSize() - slot.getAmount());
                if (remaining <= 0)
                    return true;
            }
        }
        return false;
    }

    public static int getTotalExperience(Player player) {
        int exp = Math.round(getExpAtLevel(player.getLevel()) * player.getExp());
        int currentLevel = player.getLevel();
        while (currentLevel > 0) {
            currentLevel--;
            exp += getExpAtLevel(currentLevel);
        }
        if (exp < 0) {
            exp = Integer.MAX_VALUE;
        }
        return exp;
    }

    public static int getExpAtLevel(int level) {
        if (level <= 15) {
            return (2 * level) + 7;
        }
        if (level <= 30) {
            return (5 * level) - 38;
        }
        return (9 * level) - 158;
    }

    public static void setTotalExperience(Player player, int exp) {
        if (exp < 0) {
            exp = 0;
        }
        player.setExp(0);
        player.setLevel(0);
        player.setTotalExperience(0);

        int currentExp = exp;
        while (currentExp > 0) {
            int expToNextLevel = getExpAtLevel(player.getLevel());
            if (currentExp >= expToNextLevel) {
                currentExp -= expToNextLevel;
                player.setLevel(player.getLevel() + 1);
            } else {
                player.setExp((float) currentExp / (float) expToNextLevel);
                currentExp = 0;
            }
        }
        player.setTotalExperience(exp);
    }

    public static void changeTotalExperience(Player player, int amount) {
        int current = getTotalExperience(player);
        setTotalExperience(player, Math.max(0, current + amount));
    }
}
