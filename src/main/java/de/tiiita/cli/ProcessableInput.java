package de.tiiita.cli;

import java.util.List;

public class ProcessableInput {
  private final List<ArgumentModel> parsedArguments;
  private final String[] rawInput;
  private final CliCommand command;

  public ProcessableInput(List<ArgumentModel> parsedArguments, String[] rawInput,
      CliCommand command) {
    this.parsedArguments = parsedArguments;
    this.rawInput = rawInput;
    this.command = command;
  }

  public List<ArgumentModel> getParsedArguments() {
    return parsedArguments;
  }

  public String[] getRawInput() {
    return rawInput;
  }

  public CliCommand getCommand() {
    return command;
  }
}
