package de.tiiita.shell.command;

import de.tiiita.shell.ShellCommand;
import de.tiiita.shell.Command;
import de.tiiita.shell.CommandService;

/**
 * This is the default help command. You can just register this in your project
 * to use it. If you want to implement the help command yourself, feel free to
 * use this and modify it. But do not forget to register the right help command,
 * you have to use the right import for the right help command!
 */
@Command(name = "help", description = "Show all commands and their description")
public class HelpCommand extends ShellCommand {

  @Override
  public void execute() {
    System.out.println("=== HELP ===");
    System.out.println("Type the command name to see the syntax!");
    System.out.println(" ");

    for (ShellCommand command : CommandService.getCommands()) {
      Command annotation = command.getClass().getAnnotation(Command.class);
      System.out.println(annotation.name() + " - " + annotation.description());
    }
    System.out.println(" ");
  }
}
