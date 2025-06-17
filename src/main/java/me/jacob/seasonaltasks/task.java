package me.jacob.seasonaltasks.task;

import me.jacob.seasonaltasks.model.Task;
import me.jacob.seasonaltasks.reward.RewardManager;
import me.jacob.seasonaltasks.SeasonalTasks;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class TaskManager {

    private final SeasonalTasks plugin;
    private final Map<String, Task> taskMap = new HashMap<>();

    public TaskManager(SeasonalTasks plugin) {
        this.plugin = plugin;
        loadTasks();
    }

    public void loadTasks() {
        taskMap.clear();
        File file = new File(plugin.getDataFolder(), "tasks.yml");
        if (!file.exists()) {
            plugin.saveResource("tasks.yml", false);
        }
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection section = config.getConfigurationSection("tasks");
        if (section == null) return;

        for (String key : section.getKeys(false)) {
            String name = section.getString(key + ".name");
            String type = section.getString(key + ".type");
            long target = section.getLong(key + ".target");
            String rewardType = section.getString(key + ".reward.type");
            String rewardValue = section.getString(key + ".reward.value");
            String message = section.getString(key + ".message");

            Task task = new Task(key, name, type, target, rewardType, rewardValue, message);
            taskMap.put(key, task);
        }
        plugin.getLogger().info("✅ Loaded " + taskMap.size() + " seasonal tasks.");
    }

    public Map<String, Task> getTaskMap() {
        return taskMap;
    }

    public void checkTaskCompletion(Player player, long currentBlocksMined) {
        for (Task task : taskMap.values()) {
            if (!task.getType().equalsIgnoreCase("BLOCKS_MINED")) continue;

            long target = task.getTarget();
            if (currentBlocksMined >= target && !plugin.getDataHandler().hasCompleted(player, task.getId())) {
                plugin.getDataHandler().markCompleted(player, task.getId());
                RewardManager.giveReward(player, task);
                player.sendMessage(color(task.getMessage()));
            }
        }
    }

    private String color(String msg) {
        return msg == null ? "" : msg.replace("&", "§");
    }
}
