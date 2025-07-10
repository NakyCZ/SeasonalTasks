package me.jacob.seasonaltasks.gui;

import me.jacob.seasonaltasks.SeasonalTasks;
import me.jacob.seasonaltasks.model.Task;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GUIManager {

    private final SeasonalTasks plugin;
    private final YamlConfiguration config;

    public GUIManager(SeasonalTasks plugin) {
        this.plugin = plugin;
        File file = new File(plugin.getDataFolder(), "gui.yml");
        if (!file.exists()) plugin.saveResource("gui.yml", false);
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void openTaskGUI(Player player) {
        String title = color(config.getString("menu.title", "&bSeasonal Tasks"));
        int size = config.getInt("menu.size", 54);
        Inventory gui = Bukkit.createInventory(null, size, title);

        if (config.getBoolean("menu.fill-empty", false)) {
            ItemStack filler = createItem(
                Material.valueOf(config.getString("menu.filler-item.material", "GRAY_STAINED_GLASS_PANE")),
                config.getString("menu.filler-item.name", " ")
            );
            for (int i = 0; i < size; i++) gui.setItem(i, filler);
        }

        // Add information items
        if (config.contains("menu.info-items")) {
            for (Object obj : config.getList("menu.info-items", new ArrayList<>())) {
                if (obj instanceof Map<?, ?> infoMap) {
                    int slot = (Integer) infoMap.get("slot");
                    String material = (String) infoMap.get("material");
                    String name = (String) infoMap.get("name");
                    @SuppressWarnings("unchecked")
                    List<String> lore = (List<String>) infoMap.get("lore");
                    
                    ItemStack infoItem = createInfoItem(material, name, lore);
                    gui.setItem(slot, infoItem);
                }
            }
        }

        List<Integer> slots = config.getIntegerList("menu.tasks.slot-layout");
        int index = 0;

        for (Map.Entry<String, Task> entry : plugin.getTaskManager().getTaskMap().entrySet()) {
            if (index >= slots.size()) break;

            Task task = entry.getValue();
            int current = plugin.getDataHandler().getBlocksMined(player.getUniqueId()); // TEMP: Based only on mined blocks
            boolean completed = plugin.getDataHandler().hasCompleted(player, task.getId());

            ItemStack item = createTaskItem(task, current, completed);
            gui.setItem(slots.get(index++), item);
        }

        player.openInventory(gui);
    }

    private ItemStack createTaskItem(Task task, int current, boolean completed) {
        String baseMaterial = config.getString("menu.tasks.task-item.material", "DIAMOND_PICKAXE");
        Material mat = Material.getMaterial(baseMaterial);
        if (mat == null) mat = Material.STONE;

        String name = color(config.getString("menu.tasks.task-item.name", "&f{task_name}")
                .replace("{task_name}", task.getName()));

        List<String> loreTemplate = config.getStringList("menu.tasks.task-item.lore");
        List<String> lore = loreTemplate.stream().map(line ->
                color(line
                        .replace("{task_name}", task.getName())
                        .replace("{task_type}", task.getType())
                        .replace("{current}", String.valueOf(current))
                        .replace("{target}", String.valueOf(task.getTarget()))
                        .replace("{reward}", task.getRewardValue())
                        .replace("{status}", completed ? "§a✔ Completed" : "§c✘ Incomplete")
                )
        ).toList();

        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }

    private ItemStack createInfoItem(String materialName, String name, List<String> lore) {
        Material material = Material.getMaterial(materialName);
        if (material == null) material = Material.STONE;
        
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(color(name));
            if (lore != null) {
                List<String> coloredLore = lore.stream().map(this::color).toList();
                meta.setLore(coloredLore);
            }
            item.setItemMeta(meta);
        }
        return item;
    }

    private ItemStack createItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(color(name));
            item.setItemMeta(meta);
        }
        return item;
    }

    private String color(String s) {
        return s == null ? "" : s.replace("&", "§");
    }
}
