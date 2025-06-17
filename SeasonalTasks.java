package me.jacob.seasonaltasks;

import org.bukkit.plugin.java.JavaPlugin;
import me.jacob.seasonaltasks.command.SeasonalTasksCommand;
import me.jacob.seasonaltasks.data.DataHandler;
import me.jacob.seasonaltasks.task.TaskManager;
import me.jacob.seasonaltasks.listener.BlockListener;

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
