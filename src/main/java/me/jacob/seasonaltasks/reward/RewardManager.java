package me.jacob.seasonaltasks.reward;

import me.jacob.seasonaltasks.model.Task;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class RewardManager {

    public static void giveReward(Player player, Task task) {
        switch (task.getRewardType().toLowerCase()) {
            case "command" -> runCommand(task.getRewardValue(), player);
            case "permission" -> givePermission(player, task.getRewardValue());
            // TODO: Add "item", "pet", etc.
            default -> player.sendMessage("§cUnknown reward type: " + task.getRewardType());
        }
    }

    private static void runCommand(String command, Player player) {
        String parsedCommand = command.replace("%player%", player.getName());
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), parsedCommand);
    }

    private static void givePermission(Player player, String permission) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " permission set " + permission);
    }
}
