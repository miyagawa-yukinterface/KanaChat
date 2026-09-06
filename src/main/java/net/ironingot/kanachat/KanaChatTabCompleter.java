package net.ironingot.kanachat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class KanaChatTabCompleter implements TabCompleter {
    private final KanaChat plugin;

    public KanaChatTabCompleter(KanaChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 0) {
            return Collections.emptyList();
        }

        List<String> suggestions;
        if (args.length == 1) {
            suggestions = Arrays.asList("on", "off", "kanji", "version", "dict");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("kanji")) {
            suggestions = Arrays.asList("on", "off");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("dict")) {
            suggestions = Arrays.asList("list", "add", "edit", "remove");
        } else if (args.length == 3 && args[0].equalsIgnoreCase("dict")
                && (args[1].equalsIgnoreCase("edit") || args[1].equalsIgnoreCase("remove"))) {
            suggestions = plugin.getDictionary().getWords();
        } else {
            suggestions = Collections.emptyList();
        }

        String prefix = args[args.length - 1].toLowerCase();
        List<String> matches = new ArrayList<String>();
        for (String suggestion : suggestions) {
            if (suggestion.toLowerCase().startsWith(prefix)) {
                matches.add(suggestion);
            }
        }
        return matches;
    }
}
