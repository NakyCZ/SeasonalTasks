package me.jacob.seasonaltasks.data;

import me.jacob.seasonaltasks.SeasonalTasks;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.UUID;

public class DataHandler {

    private final SeasonalTasks plugin;
    private final boolean useMySQL;

    public DataHandler(SeasonalTasks plugin) {
        this.plugin = plugin;
        this.useMySQL = plugin.getConfig().getBoolean("mysql.enabled");
    }

    public void incrementBlocksMined(Player player, int amount) {
        UUID uuid = player.getUniqueId();
        if (useMySQL) {
            try (Connection conn = plugin.getMySQLManager().getConnection()) {
                PreparedStatement check = conn.prepareStatement("SELECT progress FROM seasonaltasks WHERE uuid=? AND task_id='mine_blocks'");
                check.setString(1, uuid.toString());
                ResultSet rs = check.executeQuery();
                if (rs.next()) {
                    long current = rs.getLong("progress") + amount;
                    PreparedStatement update = conn.prepareStatement("UPDATE seasonaltasks SET progress=? WHERE uuid=? AND task_id='mine_blocks'");
                    update.setLong(1, current);
                    update.setString(2, uuid.toString());
                    update.executeUpdate();
                } else {
                    PreparedStatement insert = conn.prepareStatement("INSERT INTO seasonaltasks (uuid, task_id, progress, completed) VALUES (?, 'mine_blocks', ?, false)");
                    insert.setString(1, uuid.toString());
                    insert.setLong(2, amount);
                    insert.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            File file = new File(plugin.getDataFolder(), "data/" + uuid + ".yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            long mined = config.getLong("blocks-mined", 0) + amount;
            config.set("blocks-mined", mined);
            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public int getBlocksMined(UUID uuid) {
        if (useMySQL) {
            try (Connection conn = plugin.getMySQLManager().getConnection()) {
                PreparedStatement ps = conn.prepareStatement("SELECT progress FROM seasonaltasks WHERE uuid=? AND task_id='mine_blocks'");
                ps.setString(1, uuid.toString());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) return rs.getInt("progress");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return 0;
        } else {
            File file = new File(plugin.getDataFolder(), "data/" + uuid + ".yml");
            if (!file.exists()) return 0;
            return YamlConfiguration.loadConfiguration(file).getInt("blocks-mined", 0);
        }
    }

    public boolean hasCompleted(Player player, String taskId) {
        return hasCompleted((OfflinePlayer) player, taskId);
    }

    public boolean hasCompleted(OfflinePlayer player, String taskId) {
        UUID uuid = player.getUniqueId();
        if (useMySQL) {
            try (Connection conn = plugin.getMySQLManager().getConnection()) {
                PreparedStatement ps = conn.prepareStatement("SELECT completed FROM seasonaltasks WHERE uuid=? AND task_id=?");
                ps.setString(1, uuid.toString());
                ps.setString(2, taskId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) return rs.getBoolean("completed");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        } else {
            File file = new File(plugin.getDataFolder(), "data/" + uuid + ".yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            return config.getBoolean("completed." + taskId, false);
        }
    }

    public void markCompleted(Player player, String taskId) {
        markCompleted((OfflinePlayer) player, taskId);
    }

    public void markCompleted(OfflinePlayer player, String taskId) {
        UUID uuid = player.getUniqueId();
        if (useMySQL) {
            try (Connection conn = plugin.getMySQLManager().getConnection()) {
                PreparedStatement ps = conn.prepareStatement("UPDATE seasonaltasks SET completed=true WHERE uuid=? AND task_id=?");
                ps.setString(1, uuid.toString());
                ps.setString(2, taskId);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            File file = new File(plugin.getDataFolder(), "data/" + uuid + ".yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            config.set("completed." + taskId, true);
            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
