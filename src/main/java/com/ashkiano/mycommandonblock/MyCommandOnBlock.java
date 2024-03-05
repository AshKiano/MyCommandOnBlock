package com.ashkiano.mycommandonblock;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class MyCommandOnBlock extends JavaPlugin {

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        startPlayerPositionCheck();
    }

    private void startPlayerPositionCheck() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : getServer().getOnlinePlayers()) {
                    checkPlayerPosition(player);
                }
            }
        }.runTaskTimer(this, 0L, 20L); // Kontrola každou sekundu (20 ticků = 1 sekunda)
    }

    private void checkPlayerPosition(Player player) {
        Location loc = player.getLocation();
        FileConfiguration config = getConfig();
        List<?> blocks = config.getList("blocks");

        for (Object obj : blocks) {
            if (obj instanceof ConfigurationSection) {
                ConfigurationSection block = (ConfigurationSection) obj;
                int x = block.getInt("x");
                int y = block.getInt("y");
                int z = block.getInt("z");
                String command = block.getString("command").replace("<player>", player.getName());

                if (loc.getBlockX() == x && loc.getBlockY() - 1 == y && loc.getBlockZ() == z) { // -1 pro blok pod hráčem
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                }
            }
        }
    }

    @Override
    public void onDisable() {
        // Logika pro vypnutí pluginu, pokud je potřeba
    }
}