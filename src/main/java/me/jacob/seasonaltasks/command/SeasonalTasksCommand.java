package me.jacob.seasonaltasks.command;

import me.jacob.seasonaltasks.gui.GUIManager;
import me.jacob.seasonaltasks.SeasonalTasks;
import me.jacob.seasonaltasks.task.TaskManager;
import me.jacob.seasonaltasks.model.Task;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.Map;

public class SeasonalTasksCommand implements CommandExecutor {

    private final SeasonalTasks plugin;

    public SeasonalTasksCommand(SeasonalTasks plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("§cOnly players can open the task menu.");
                return true;
            }

            if (!player.hasPermission("seasonaltasks.use")) {
                player.sendMessage("§cYou don’t have permission to use this command.");
                return true;
            }

            new GUIManager(plugin).openTaskGUI(player);
            return true;
        }

        // Admin commands
        if (!sender.hasPermission("seasonaltasks.admin")) {
            sender.sendMessage("§cNo permission.");
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            plugin.getTaskManager().loadTasks();
            plugin.reloadConfig(); // reload config.yml
            sender.sendMessage("§a[SeasonalTasks] Reloaded configs and tasks.");
            return true;
        }

        if (args[0].equalsIgnoreCase("progress")) {
            if (args.length < 2) {
                sender.sendMessage("§cUsage: /seasonaltasks progress <player>");
                return true;
            }

            OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
            if (target == null || !target.hasPlayedBefore()) {
                sender.sendMessage("§cPlayer not found.");
                return true;
            }

            int mined = plugin.getDataHandler().getBlocksMined(target.getUniqueId());
            sender.sendMessage("§eProgress for §f" + target.getName() + ":");
            for (Map.Entry<String, Task> entry : plugin.getTaskManager().getTaskMap().entrySet()) {
                Task task = entry.getValue();
                if (!task.getType().equalsIgnoreCase("BLOCKS_MINED")) continue;

                boolean complete = plugin.getDataHandler().hasCompleted(target, task.getId());
                sender.sendMessage("§7- " + task.getName() + ": " +
                        (complete ? "§a✔ Completed" : "§f" + mined + " / " + task.getTarget()));
            }
            return true;
        }

        sender.sendMessage("§cUnknown subcommand.");
        return true;
    }
}
