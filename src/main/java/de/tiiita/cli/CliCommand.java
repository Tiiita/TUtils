package de.tiiita.cli;

import java.util.ArrayList;
import java.util.List;

public abstract class CliCommand {
  private final List<CliCommand> subCommands = new ArrayList<>();
  protected abstract void execute();

  protected void addSub(CliCommand subCommand) {
    subCommands.add(subCommand);
  }

  List<CliCommand> getSubCommands() {
    return new ArrayList<>(subCommands);
  }
}
