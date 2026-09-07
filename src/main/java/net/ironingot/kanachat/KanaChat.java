package net.ironingot.kanachat;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.PluginCommand;

import java.util.Arrays;
import java.util.logging.Logger;

import net.ironingot.kanachat.listener.AsyncChatDecorateListener;

public class KanaChat extends JavaPlugin {
    public static final Logger logger = Logger.getLogger("Minecraft");
    private KanaChatConfiguration configuration;
    private KanaChatDictionary dictionary;

    public void onEnable() {
        saveDefaultConfig();
        configuration = new KanaChatConfiguration(this);
        configuration.migrateLegacyConfiguration();
        dictionary = new KanaChatDictionary(this);

        PluginCommand command = getCommand("kanachat");
        if (command == null) {
            throw new IllegalStateException("The kanachat command is not declared in plugin.yml");
        }
        command.setAliases(Arrays.asList("japanize", "kc"));
        KanaChatCommand commandHandler = new KanaChatCommand(this);
        command.setExecutor(commandHandler);
        command.setTabCompleter(new KanaChatTabCompleter(this));

        // getServer().getPluginManager().registerEvents(new AsyncPlayerChatListener(this), this);
        getServer().getPluginManager().registerEvents(new AsyncChatDecorateListener(this), this);

        logger.info(getDescription().getName() + "-" + getDescription().getVersion() + " is enabled");
    }

    public void onDisable() {
        logger.info(getDescription().getName() + " is disabled");
    }

    public KanaChatConfiguration getConfiguration() {
        return configuration;
    }

    public KanaChatDictionary getDictionary() {
        return dictionary;
    }
}
