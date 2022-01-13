package net.zargum.zlib.command;

import net.zargum.zlib.messages.Messages;
import net.zargum.zlib.utils.JavaUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class HelpCommandArgument<T extends JavaPlugin> extends ServerCommandArgument<T> {

    private final String title;
    private final ChatColor primaryColor, secondaryColor;

    public HelpCommandArgument(ServerCommand<T> command, String title, ChatColor primaryColor, ChatColor secondaryColor) {
        super(command, "help", "Shows helping list of all commands", "[page]", "h");
        title = (title == null) ? command.getName().toUpperCase() + " HELP" : title;
        this.title = title;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        command.pagedHelp = true;
        command.usage = getCommand().getHelpPage(title, primaryColor, secondaryColor, 1);
    }

    public HelpCommandArgument(ServerCommand<T> command, ChatColor primaryColor, ChatColor secondaryColor) {
        this(command, null, primaryColor, secondaryColor);
    }

    public HelpCommandArgument(ServerCommand<T> command, String title) {
        this(command, title, ChatColor.DARK_PURPLE, ChatColor.LIGHT_PURPLE);
    }

    public HelpCommandArgument(ServerCommand<T> command) {
        this(command, null, ChatColor.DARK_PURPLE, ChatColor.LIGHT_PURPLE);
    }

    @Override
    public void execute(CommandSender sender, String argumentName, String[] args) {
        String pageArg = args.length == 0 ? "1" : args[0];
        if (JavaUtils.isInt(pageArg)) {
            int page = Integer.parseInt(pageArg);
            int maxPagesTmp = getCommand().getArguments().size() / ServerCommand.MAX_COMMANDS_PER_PAGE;
            int maxPages = maxPagesTmp * ServerCommand.MAX_COMMANDS_PER_PAGE < getCommand().getArguments().size() ? maxPagesTmp + 1 : maxPagesTmp;
            if (page > 0 && page <= maxPages) {
                sender.sendMessage(getCommand().getHelpPage(title, primaryColor, secondaryColor, page));
                return;
            }
        }
        sender.sendMessage(Messages.INVALID_HELP_PAGE.toString());
    }
}
