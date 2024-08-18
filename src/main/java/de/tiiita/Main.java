package de.tiiita;

import de.tiiita.cli.CommandService;
import de.tiiita.cli.HelpCommand;

public class Main {

  public static void main(String[] args) {
    CommandService.register(new HelpCommand());
  }

}
