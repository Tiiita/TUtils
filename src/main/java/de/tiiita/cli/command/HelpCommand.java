package de.tiiita.cli.command;

import de.tiiita.cli.CliCommand;
import de.tiiita.cli.Command;
import de.tiiita.cli.CommandService;
import java.util.concurrent.Callable;

/**
 * This is the default help command. You can just register this in your project
 * to use it. If you want to implement the help command yourself, feel free to
 * use this and modify it. But do not forget to register the right help command,
 * you have to use the right import for the right help command!
 */
@Command(name = "help", description = "Show all commands and their description")
public class HelpCommand extends CliCommand {

  @Override
  public void execute() {
    System.out.println("=== HELP ===");
    for (CliCommand command : CommandService.getCommands()) {
      Command commandAnnotation = command.getClass().getAnnotation(Command.class);
      String commandHelp = commandAnnotation.name() + " - " + commandAnnotation.description();
      System.out.println(commandHelp);
    }
    System.out.println(" ");
  }
}