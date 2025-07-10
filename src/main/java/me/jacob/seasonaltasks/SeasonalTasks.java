package me.jacob.seasonaltasks;

import org.bukkit.plugin.java.JavaPlugin;
import me.jacob.seasonaltasks.command.SeasonalTasksCommand;
import me.jacob.seasonaltasks.data.DataHandler;
import me.jacob.seasonaltasks.data.MySQLManager;
import me.jacob.seasonaltasks.task.TaskManager;
import me.jacob.seasonaltasks.listener.BlockListener;
import me.jacob.seasonaltasks.listener.MenuListener;

public class SeasonalTasks extends JavaPlugin {

    private static SeasonalTasks instance;
    private TaskManager taskManager;
    private DataHandler dataHandler;
    private MySQLManager mySQLManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        
        // Initialize MySQL if enabled
        if (getConfig().getBoolean("mysql.enabled")) {
            this.mySQLManager = new MySQLManager(this);
            this.mySQLManager.connect();
        }
        
        this.taskManager = new TaskManager(this);
        this.dataHandler = new DataHandler(this);

        getCommand("seasonaltasks").setExecutor(new SeasonalTasksCommand(this));
        getServer().getPluginManager().registerEvents(new BlockListener(this), this);
        getServer().getPluginManager().registerEvents(new MenuListener(this), this);

        getLogger().info("✅ SeasonalTasks enabled!");
    }

    @Override
    public void onDisable() {
        if (mySQLManager != null) {
            mySQLManager.disconnect();
        }
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
    
    public MySQLManager getMySQLManager() {
        return mySQLManager;
    }
}
