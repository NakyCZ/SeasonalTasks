package me.jacob.seasonaltasks.listener;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.entity.Player;
import me.jacob.seasonaltasks.SeasonalTasks;

public class MenuListener implements Listener {

    private final SeasonalTasks plugin;

    public MenuListener(SeasonalTasks plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getView().getTitle().equalsIgnoreCase("§6§lSeasonal Tasks")) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent e) {
        if (e.getView().getTitle().equalsIgnoreCase("§6§lSeasonal Tasks")) {
            e.setCancelled(true);
        }
    }
}
