package de.tiiita;

import de.tiiita.cli.CommandListener;
import de.tiiita.cli.CommandRegistry;
import de.tiiita.cli.example.ExampleCommand;
import de.tiiita.minecraft.bungee.Command;

public class Main {

  public static void main(String[] args) {
    CommandRegistry.add(new ExampleCommand());

    while (true) {
    CommandListener.listen();
    }
  }
}
