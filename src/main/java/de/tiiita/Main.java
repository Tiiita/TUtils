package de.tiiita;

import de.tiiita.cli.CommandService;
import de.tiiita.cli.command.ExampleCommand;
import de.tiiita.cli.exception.CommandNotFoundException;
import de.tiiita.cli.exception.CommandSyntaxException;

public class Main {

  public static void main(String[] args) {
    CommandService.register(new ExampleCommand());

    while (true) {
      try {

        CommandService.listen();


      } catch (CommandNotFoundException e) {
        Logger.logWarning("Command not found");
      } catch (CommandSyntaxException e) {
        Logger.logWarning("Wrong syntax, use: " + e.getMessage());
      }
    }

  }

}
