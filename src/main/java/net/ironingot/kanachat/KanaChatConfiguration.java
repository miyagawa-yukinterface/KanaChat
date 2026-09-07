package net.ironingot.kanachat;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

public class KanaChatConfiguration {
    private final JavaPlugin plugin;

    public KanaChatConfiguration(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void migrateLegacyConfiguration() {
        ConfigurationSection legacy = plugin.getConfig().getConfigurationSection("KanaChat");
        if (legacy == null) {
            return;
        }

        for (Map.Entry<String, Object> entry : legacy.getValues(false).entrySet()) {
            if (!plugin.getConfig().contains(entry.getKey())) {
                plugin.getConfig().set(entry.getKey(), entry.getValue());
            }
        }
        plugin.getConfig().set("KanaChat", null);
        plugin.saveConfig();
    }

    public boolean isKanaEnabled(String playerName) {
        return plugin.getConfig().getBoolean("user." + playerName + ".kanachat", true);
    }

    public void setKanaEnabled(String playerName, boolean enabled) {
        plugin.getConfig().set("user." + playerName + ".kanachat", enabled);
        plugin.saveConfig();
    }

    public boolean isKanjiEnabled(String playerName) {
        return plugin.getConfig().getBoolean("user." + playerName + ".kanji", true);
    }

    public void setKanjiEnabled(String playerName, boolean enabled) {
        plugin.getConfig().set("user." + playerName + ".kanji", enabled);
        plugin.saveConfig();
    }

}
