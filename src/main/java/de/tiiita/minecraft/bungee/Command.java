package de.tiiita.minecraft.bungee;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

public abstract class Command extends net.md_5.bungee.api.plugin.Command {
    private final String USAGE_MESSAGE;
    private final String NO_PERMS_MESSAGE;
    public Command(String name, String usageMessage, String noPermsMessage) {
        super(name);
        USAGE_MESSAGE = usageMessage;
        NO_PERMS_MESSAGE = noPermsMessage;
    }

    public Command(String name, String usageMessage, String noPermsMessage, String permission, String... aliases) {
        super(name, permission, aliases);
        USAGE_MESSAGE = usageMessage;
        NO_PERMS_MESSAGE = noPermsMessage;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        onExecute(sender, args);
    }

    public abstract void onExecute(CommandSender sender, String[] args);

    public void sendUsage(CommandSender sender) {
        sender.sendMessage(new TextComponent(USAGE_MESSAGE));
    }

    public void sendNoPermissions(CommandSender sender) {
        sender.sendMessage(new TextComponent(NO_PERMS_MESSAGE));
    }
}
