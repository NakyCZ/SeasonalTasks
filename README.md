# 🌱 SeasonalTasks

A powerful and configurable **Minecraft Prison Plugin** for **seasonal progression**, written in Java for Spigot/Paper 1.21+.

---

## 📦 Features

✅ Configurable task types (e.g., blocks mined, ranks, etc.)  
✅ Fully customizable rewards (commands, permissions, items, etc.)  
✅ Persistent player progress (YAML or MySQL)  
✅ GUI menu (`/seasonaltasks`) showing tasks, progress, and status  
✅ Admin commands for viewing progress and reloading  
✅ Seasonal reset ready  
✅ Async-safe and optimized for large servers  
✅ PlaceholderAPI support (optional)

---

## 🛠️ Configuration

### `tasks.yml`

```yaml
tasks:
  mine_blocks:
    name: "Mine Master"
    type: "BLOCKS_MINED"
    target: 5000000
    reward:
      type: command
      value: "give %player% diamond 64"
    message: "&aYou completed the seasonal task: &eMine Master!"
