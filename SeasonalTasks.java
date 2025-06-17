package me.yourname.seasonaltasks;

import org.bukkit.plugin.java.JavaPlugin;
import me.yourname.seasonaltasks.command.SeasonalTasksCommand;
import me.yourname.seasonaltasks.data.DataHandler;
import me.yourname.seasonaltasks.task.TaskManager;
import me.yourname.seasonaltasks.listener.BlockListener;

public class SeasonalTasks extends JavaPlugin {

    private static SeasonalTasks instance;
    private TaskManager taskManager;
    private DataHandler dataHandler;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        this.taskManager = new TaskManager(this);
        this.dataHandler = new DataHandler(this);

        getCommand("seasonaltasks").setExecutor(new SeasonalTasksCommand(this));
        getServer().getPluginManager().registerEvents(new BlockListener(this), this);

        getLogger().info("✅ SeasonalTasks enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("🛑 SeasonalTasks disabled.");
    }

    public static SeasonalTasks getInstance() {
        return instance;
    }

    public TaskManager getTaskManager() {
        return taskManager;
    }

    public DataHandler getDataHandler() {
        return dataHandler;
    }
}
