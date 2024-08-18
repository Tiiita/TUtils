package de.tiiita;

import de.tiiita.cli.CommandParser;
import de.tiiita.cli.CommandService;
import de.tiiita.cli.example.ExampleCommand;

public class Main {

  public static void main(String[] args) {
    CommandService.register(new ExampleCommand());

    while (true) {
      CommandService.listen();
    }
  }
}
