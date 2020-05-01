package net.zargum.zlib.command;

import lombok.Getter;
import lombok.Setter;
import net.zargum.zlib.messages.Messages;
import org.bukkit.command.CommandSender;

import java.util.*;

public abstract class ServerCommand {

    @Getter private final String commandName;
    @Getter private final String label;
    @Getter @Setter private final String description;
    @Getter @Setter private String[] aliases = new String[]{};
    @Getter private final String usageMessage;
    @Getter @Setter private int minArgRequired = 0;
    @Getter @Setter private String permission;
    @Getter private boolean requiredPermission = false;
    @Getter private boolean consoleOnly = false;
    @Getter private boolean playerOnly = false;
    @Setter private String usage;
    @Setter private String customUsage;
    @Getter private final Map<Integer, List<String>> tabCompletionList = new HashMap<>();

    public ServerCommand(String commandName, String description, String usage, String aliases) {
        this.commandName = commandName.toLowerCase();
        this.description = description;
        this.usage = usage;
        this.label = commandName;
        usageMessage = Messages.COMMAND_USAGE.toString(commandName, usage);
        if (!aliases.equals("")) this.aliases = aliases.split(", ");
        permission = "command." + commandName;
        if (!usage.isEmpty()) {
            String[] args = usage.split(" ");
            for (String arg : args) if (arg.charAt(0) == '<') minArgRequired++;
        }
    }

    public ServerCommand(String commandName, String description, String usage) {
        this(commandName, description, usage, "");
    }

    public ServerCommand(String commandName, String description) {
        this(commandName, description, "", "");
    }

    public abstract void execute(String label, CommandSender sender, String[] args);

    public void requiredPermission() {
        requiredPermission = true;
    }

    public String getUsage() {
        System.out.println(customUsage);
        return customUsage != null ? customUsage : usageMessage;
    }

    public void playerOnly() {
        playerOnly = true;
        consoleOnly = false;
    }

    public void consoleOnly() {
        consoleOnly = true;
        playerOnly = false;
    }

    public void addToTablistCompletion(int pos, String arg) {
        if (tabCompletionList.containsKey(pos)) tabCompletionList.get(pos).add(arg);
        else tabCompletionList.put(pos, Collections.singletonList(arg));
    }

    public void addToTablistCompletion(int pos, String... args) {
        tabCompletionList.put(pos, Arrays.asList(args));
    }

    public void removeFromTablistCompletion(int pos, String arg) {
        tabCompletionList.get(pos).remove(arg);
    }

    public void removeFromTablistCompletion(int pos, String... args) {
        tabCompletionList.get(pos).removeIf(tabArg -> Arrays.asList(args).contains(tabArg));
    }
}