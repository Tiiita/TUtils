package de.tiiita.cli;

import java.util.concurrent.Callable;

@Command(name = "help", description = "Show all commands and their description")
public class HelpCommand implements Callable<Void> {

  @Override
  public Void call() {
    System.out.println("=== HELP ===");
    for (Callable<?> command : CommandService.getCommands()) {
      Command commandAnnotation = command.getClass().getAnnotation(Command.class);
      String commandHelp = commandAnnotation.name() + " - " + commandAnnotation.description();
      System.out.println(commandHelp);
    }
    System.out.println(" ");
    return null;
  }
}
