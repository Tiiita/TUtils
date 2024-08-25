package de.tiiita.shell;

import java.util.ArrayList;
import java.util.List;

public abstract class ShellCommand {
  private final List<ShellCommand> subCommands = new ArrayList<>();
  protected abstract void execute();

  protected void addSub(ShellCommand subCommand) {
    subCommands.add(subCommand);
  }

  List<ShellCommand> getSubCommands() {
    return new ArrayList<>(subCommands);
  }
}
