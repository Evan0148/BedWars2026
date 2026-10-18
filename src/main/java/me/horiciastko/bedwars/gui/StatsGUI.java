package me.horiciastko.bedwars.gui;

import me.horiciastko.bedwars.BedWars;
import me.horiciastko.bedwars.models.PlayerStats;
import me.horiciastko.bedwars.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class StatsGUI extends BaseGUI {

    public StatsGUI(Player target) {
        super(BedWars.getInstance().getLanguageManager().getMessage(null, "stats-gui-title") + " " + target.getName(), 3);
        setContents(target);
    }

    @Override
    public void setContents(Player player) {
        PlayerStats stats = BedWars.getInstance().getStatsManager().getStats(player.getUniqueId());

        ItemStack levelInfo = new ItemBuilder(Material.EXPERIENCE_BOTTLE)
                .setName("§b§l等级与经验")
                .setLore(
                    "§7当前等级: §b" + stats.getLevel(),
                    "§7称号: " + stats.getRank(),
                    "§7总经验值: §e" + stats.getExperience(),
                    "",
                    "§7升级进度: §e" + stats.getCurrentLevelXp() + "§7/§e" + stats.getRequiredXpForNextLevel(),
                    stats.getProgressBar() + " §7" + String.format("%.1f", stats.getProgressPercent()) + "%",
                    "",
                    "§8下一等级: §b" + (stats.getLevel() + 1)
                )
                .build();
        inventory.setItem(4, levelInfo);

        ItemStack wins = new ItemBuilder(Material.DIAMOND)
                .setName("§b获胜场次")
                .setLore("§7总胜场: §e" + (stats != null ? stats.getWins() : 0))
                .build();
        inventory.setItem(10, wins);

        ItemStack kills = new ItemBuilder(Material.IRON_SWORD)
                .setName("§c击杀数")
                .setLore("§7总击杀: §e" + (stats != null ? stats.getKills() : 0))
                .build();
        inventory.setItem(11, kills);

        ItemStack deaths = new ItemBuilder(Material.SKELETON_SKULL)
                .setName("§c死亡数")
                .setLore(
                    "§7总死亡: §e" + (stats != null ? stats.getDeaths() : 0),
                    "§7K/D 战绩比: §e" + (stats.getDeaths() > 0 ? String.format("%.2f", (double)stats.getKills() / stats.getDeaths()) : stats.getKills())
                )
                .build();
        inventory.setItem(12, deaths);

        ItemStack finalKills = new ItemBuilder(Material.DIAMOND_SWORD)
                .setName("§5最终击杀")
                .setLore("§7总最终击杀: §e" + (stats != null ? stats.getFinalKills() : 0))
                .build();
        inventory.setItem(13, finalKills);

        ItemStack beds = new ItemBuilder(Material.RED_BED)
                .setName("§a拆床数")
                .setLore("§7摧毁敌方床总数: §e" + (stats != null ? stats.getBedsBroken() : 0))
                .build();
        inventory.setItem(14, beds);

        int killXp = BedWars.getInstance().getLevelsManager().getXpForAction("kill");
        int finalKillXp = BedWars.getInstance().getLevelsManager().getXpForAction("final-kill");
        int bedBrokenXp = BedWars.getInstance().getLevelsManager().getXpForAction("bed-broken");
        int winXp = BedWars.getInstance().getLevelsManager().getXpForAction("win");

        ItemStack xpInfo = new ItemBuilder(Material.BOOK)
                .setName("§e§l经验奖励规则")
                .setLore(
                    "§7普通击杀: §a+" + killXp + " 经验",
                    "§7最终击杀: §a+" + finalKillXp + " 经验",
                    "§7拆除敌床: §a+" + bedBrokenXp + " 经验",
                    "§7取得胜利: §a+" + winXp + " 经验"
                )
                .build();
        inventory.setItem(16, xpInfo);

        ItemStack glass = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName(" ").build();
        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, glass);
            }
        }
    }

    @Override
    public void handleAction(Player player, int slot, ItemStack item, org.bukkit.event.inventory.ClickType clickType) {
    }
}
