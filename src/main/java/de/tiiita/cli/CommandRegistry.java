package de.tiiita.cli;

import java.util.ArrayList;
import java.util.List;

public class CommandRegistry {
  private static final List<CallableCommand> commands = new ArrayList<>();
  public static <T extends CallableCommand>void add(T command) {
    commands.add(command);
  }

  static List<CallableCommand> getCommands() {
    return commands;
  }
}
