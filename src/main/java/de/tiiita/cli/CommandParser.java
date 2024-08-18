package de.tiiita.cli;

import de.tiiita.cli.exception.NoCommandFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

class CommandParser {


  private final CommandProcessor processor;

  public CommandParser() {
    processor = new CommandProcessor();
  }

  Map<Callable<?>, List<ArgumentModel>> parse(String input) {
    String[] inputSplit = input.split(" ");
    for (Callable<?> command : CommandService.getCommands()) {
      processor.validateAnnotations(command);

      if (command.getClass().getAnnotation(Command.class).name()
          .equalsIgnoreCase(inputSplit[0])) {

        String[] args = Arrays.copyOfRange(inputSplit, 1, inputSplit.length);
        return Map.of(command, getArguments(args));

      }
    }
    throw new NoCommandFoundException("No command registered with name: " + input);
  }

  private List<ArgumentModel> getArguments(String[] inputArgs) {
    List<ArgumentModel> arguments = new ArrayList<>();
    for (int i = 0; i < inputArgs.length; i++) {
      ArgumentModel argument = new ArgumentModel();
      if (i % 2 == 0) {
        argument.setName(inputArgs[i]);
      } else {
        argument.setValue(inputArgs[i]);
      }

      arguments.add(argument);
    }
    return arguments;
  }
}

