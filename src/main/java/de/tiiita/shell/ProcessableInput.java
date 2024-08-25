package de.tiiita.shell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.print.DocFlavor.STRING;

public class ProcessableInput {

  private final List<ArgumentModel> parsedArguments;
  private final String[] rawInput;
  private final ShellCommand command;

   ProcessableInput(List<ArgumentModel> parsedArguments, String[] rawInput,
      ShellCommand command) {
    this.parsedArguments = parsedArguments;
    this.rawInput = rawInput;
    this.command = command;
  }

  List<ArgumentModel> getParsedArguments() {
    return parsedArguments;
  }

  String[] getRawInput() {
    return rawInput;
  }

  ShellCommand getCommand() {
    return command;
  }

  @Override
  public String toString() {
    return "args: " + parsedArguments + ", raw-input: " + Arrays.toString(rawInput);

  }
}
