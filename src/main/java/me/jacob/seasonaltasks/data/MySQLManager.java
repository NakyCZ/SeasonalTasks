package me.jacob.seasonaltasks.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.bukkit.configuration.file.FileConfiguration;
import me.jacob.seasonaltasks.SeasonalTasks;

public class MySQLManager {

    private final SeasonalTasks plugin;
    private Connection connection;

    public MySQLManager(SeasonalTasks plugin) {
        this.plugin = plugin;
    }

    public void connect() {
        FileConfiguration config = plugin.getConfig();
        try {
            String url = "jdbc:mysql://" + config.getString("mysql.host") + ":" +
                    config.getInt("mysql.port") + "/" +
                    config.getString("mysql.database") + "?useSSL=false&autoReconnect=true";

            connection = DriverManager.getConnection(url,
                    config.getString("mysql.username"),
                    config.getString("mysql.password"));

            createTable();
            plugin.getLogger().info("✅ Connected to MySQL!");
        } catch (SQLException e) {
            plugin.getLogger().severe("❌ Failed to connect to MySQL!");
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    private void createTable() throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS seasonaltasks (
                    uuid VARCHAR(36),
                    task_id VARCHAR(50),
                    progress BIGINT,
                    completed BOOLEAN,
                    PRIMARY KEY (uuid, task_id)
                );
                """;
        Statement stmt = connection.createStatement();
        stmt.execute(sql);
    }

    public void disconnect() {
        if (connection != null) try {
            connection.close();
        } catch (SQLException ignored) {}
    }
}
