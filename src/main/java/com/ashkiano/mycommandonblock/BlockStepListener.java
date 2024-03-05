package com.ashkiano.mycommandonblock;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.entity.Player;

import java.util.List;

public class BlockStepListener implements Listener {

    private MyCommandOnBlock plugin;

    public BlockStepListener(MyCommandOnBlock plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerStep(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location loc = player.getLocation();

        // Získání souřadnic bloku pod hráčem
        int playerX = loc.getBlockX();
        int playerY = loc.getBlockY() - 1; // Abychom získali blok přímo pod hráčem
        int playerZ = loc.getBlockZ();

        FileConfiguration config = plugin.getConfig();
        List<?> blocks = config.getList("blocks");

        for (Object obj : blocks) {
            if (obj instanceof ConfigurationSection) {
                ConfigurationSection block = (ConfigurationSection) obj;
                int x = block.getInt("x");
                int y = block.getInt("y");
                int z = block.getInt("z");

                // Kontrola, zda se hráč nachází v blízkosti definovaného bloku
                if (Math.abs(playerX - x) <= 1 && Math.abs(playerY - y) <= 1 && Math.abs(playerZ - z) <= 1) {
                    String command = block.getString("command").replace("<player>", player.getName());
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                    break;
                }
            }
        }
    }
}
