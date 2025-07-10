package me.jacob.seasonaltasks.listener;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import me.jacob.seasonaltasks.SeasonalTasks;

public class BlockListener implements Listener {

    private final SeasonalTasks plugin;

    public BlockListener(SeasonalTasks plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getPlayer().hasPermission("seasonaltasks.use")) {
            plugin.getDataHandler().incrementBlocksMined(event.getPlayer(), 1);
        }
        
        // Check if any tasks should be completed after incrementing
        int currentBlocks = plugin.getDataHandler().getBlocksMined(event.getPlayer().getUniqueId());
        plugin.getTaskManager().checkTaskCompletion(event.getPlayer(), currentBlocks);
    }
}
