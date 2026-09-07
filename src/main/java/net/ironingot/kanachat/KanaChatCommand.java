package net.ironingot.kanachat;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import java.util.Arrays;
import java.util.Map;

public class KanaChatCommand implements CommandExecutor {
    private KanaChat plugin;
    private String pluginName;
    private String pluginVersion;

    public KanaChatCommand(KanaChat plugin){
        this.plugin = plugin;
        this.pluginName = plugin.getDescription().getName();
        this.pluginVersion = plugin.getDescription().getVersion();
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (args.length >= 1 && args[0].equalsIgnoreCase("dict")) {
            return executeDictionaryCommand(sender, args);
        }

        String command;
        String option;

        command = (args.length >= 1) ? args[0].toLowerCase() : "get";
        option  = (args.length >= 2) ? args[1].toLowerCase() : null;

        return executeCommand(sender, command, option);
    }

    private boolean executeDictionaryCommand(CommandSender sender, String[] args) {
        if (args.length >= 2 && args[1].equalsIgnoreCase("list")) {
            Map<String, String> entries = plugin.getDictionary().getEntries();
            if (entries.isEmpty()) {
                sender.sendMessage(ChatColor.GOLD + "KanaChat dictionary is empty.");
            } else {
                for (Map.Entry<String, String> entry : entries.entrySet()) {
                    sender.sendMessage(ChatColor.GOLD + entry.getKey() + " <- " + entry.getValue());
                }
            }
            return true;
        }

        if (!sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "Only operators can modify the dictionary.");
            return true;
        }

        if (args.length >= 3 && args[1].equalsIgnoreCase("remove")) {
            boolean removed = plugin.getDictionary().remove(args[2]);
            sender.sendMessage((removed ? ChatColor.GOLD : ChatColor.RED)
                    + (removed ? "Removed dictionary entry: " : "Dictionary entry not found: ") + args[2]);
            return true;
        }

        if (args.length >= 4
                && (args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("edit"))) {
            String[] readings = new String[args.length - 3];
            System.arraycopy(args, 3, readings, 0, readings.length);
            plugin.getDictionary().set(args[2], readings);
            sender.sendMessage(ChatColor.GOLD + (args[1].equalsIgnoreCase("add")
                    ? "Added" : "Updated") + " dictionary entry: " + args[2]);
            return true;
        }

        sender.sendMessage(ChatColor.GOLD
                + "Usage: /kanachat dict list | add <word> <alphabet...> | edit <word> <alphabet...> | remove <word>");
        return true;
    }

    private boolean executeCommand(CommandSender sender, String command, String option) {
        if (command != null && command.equals("version")) {
            sender.sendMessage(ChatColor.GOLD + this.pluginName + "-" + this.pluginVersion);
            return true;
        }

        if (command != null && command.equals("kanji")) {
            if (option != null && (option.equals("on") || option.equals("true"))) {
                plugin.getConfiguration().setKanjiEnabled(sender.getName(), true);
            }
            if (option != null && (option.equals("off") || option.equals("false"))) {
                plugin.getConfiguration().setKanjiEnabled(sender.getName(), false);
            }

            if (plugin.getConfiguration().isKanjiEnabled(sender.getName())) {
                sender.sendMessage(ChatColor.GOLD + pluginName + " Kanji conversion is enabled.");
            } else {
                sender.sendMessage(ChatColor.GOLD + pluginName + " Kanji conversion is disabled.");
            }
            return true;
        }

        if (command != null) {
            if (command.equals("on") || command.equals("true")) {
                plugin.getConfiguration().setKanaEnabled(sender.getName(), true);
            }
            if (command.equals("off") || command.equals("false")) {
                plugin.getConfiguration().setKanaEnabled(sender.getName(), false);
            }
        }

        if (plugin.getConfiguration().isKanaEnabled(sender.getName())) {
            sender.sendMessage(ChatColor.GOLD + pluginName + " is enabled.");
        } else {
            sender.sendMessage(ChatColor.GOLD + pluginName + " is disabled.");
        }
        return true;
    }

}