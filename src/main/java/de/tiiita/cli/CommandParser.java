package de.tiiita.cli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import org.jetbrains.annotations.Nullable;


/**
 * This is the command parser, it is used to parse the console input into more usable code.
 */
class CommandParser {

  private final CommandProcessor processor;

  public CommandParser() {
    processor = new CommandProcessor();
  }

  /**
   * This parses the bare console input into a map with 1 entry only.
   *
   * @param input the console input
   * @return the parsed string as a map with 1 entry. The key is the command as a callable and the
   * value is a list of the arguments as models that can be used in code. Null of no command was found.
   */
  @Nullable
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
    System.out.println("No command found");
    return null;
  }

  /**
   * This converts the arguments of the console input string into a list of {@link ArgumentModel}s.
   *
   * @param inputArgs the console input string arguments as string array. the command has to be
   *                  filtered out already so index 0 is the first argument.
   * @return the converted arguments or an empty list of no arguments where given.
   */

  private List<ArgumentModel> getArguments(String[] inputArgs) {
    List<ArgumentModel> arguments = new ArrayList<>();

    for (int i = 0; i < inputArgs.length; i += 2) {
      ArgumentModel argument = new ArgumentModel();
      argument.setName(inputArgs[i]);

      if (i + 1 < inputArgs.length) {
        argument.setValue(inputArgs[i + 1]);
      } else {
        argument.setValue(null);
      }

      arguments.add(argument);
    }

    return arguments;
  }
}

