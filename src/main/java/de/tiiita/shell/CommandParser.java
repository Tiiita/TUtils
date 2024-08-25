package de.tiiita.shell;

import de.tiiita.Collections;
import de.tiiita.Logger;
import de.tiiita.shell.exception.CommandNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jetbrains.annotations.NotNull;


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
   * value is a list of the arguments as models that can be used in code. Null of no command was
   * found.
   * @throws CommandNotFoundException if the command could not be found by the given input, you need
   *                                  to handle this exception to for example show your own not
   *                                  found message.
   */
  @NotNull
  ProcessableInput parse(String input) throws CommandNotFoundException {
    String[] inputSplit = input.split(" ");
    for (ShellCommand command : CommandService.getCommands()) {
      processor.validateAnnotations(command);

      if (command.getClass().getAnnotation(Command.class).name()
          .equalsIgnoreCase(inputSplit[0])) {

        //found right command
        String[] args = Arrays.copyOfRange(inputSplit, 1, inputSplit.length);

        List<ArgumentModel> parsedArguments = parseArguments(Arrays.asList(args),
            CommandService.getArgumentsSorted(command));

        return new ProcessableInput(parsedArguments, inputSplit, command);
      }
    }

    throw new CommandNotFoundException("No command found from parsed input: " + input);
  }

  /**
   * This converts the arguments of the console input string into a list of {@link ArgumentModel}s.
   *
   * @param inputArgs the console input string arguments as string array. the command has to be
   *                  filtered out already so index 0 is the first argument.
   * @return the converted arguments or an empty list of no arguments where given.
   */

  private List<ArgumentModel> parseArguments(List<String> inputArgs, List<Argument> needed) {
    List<ArgumentModel> arguments = new ArrayList<>();
    //Search in the input after needed arguments.

    needed.forEach(neededArg -> {
      for (int i = 0; i < inputArgs.size(); i++) {

        String currentInputArg = inputArgs.get(i);

        String nextInputArg = Collections.getOrNull(inputArgs, i + 1);

        if (!currentInputArg.equalsIgnoreCase(neededArg.name())) {
          continue;
        }

        ArgumentModel argumentModel = new ArgumentModel();
        argumentModel.setName(currentInputArg);

        if (neededArg.valued()) {
          argumentModel.setValue(nextInputArg);
        } else {
          argumentModel.setValued(true);
        }

        arguments.add(argumentModel);
      }
    });

    return arguments;

  }
}

