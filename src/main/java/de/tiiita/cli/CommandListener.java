package de.tiiita.cli;

import de.tiiita.Logger;
import de.tiiita.ThreadUtils;
import de.tiiita.cli.exception.NoCommandFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import org.w3c.dom.ls.LSException;

public class CommandListener {

  private static final CliCommandProcessor processor;

  static {
    processor = new CliCommandProcessor();
  }


  public static void listen() {
    String input = new Scanner(System.in).nextLine();

    Map<CallableCommand, List<Argument>> parsedInput = parse(input);
    Entry<CallableCommand, List<Argument>> next = parsedInput.entrySet().iterator().next();
    processor.process(next.getKey(), next.getValue());
  }


  private static Map<CallableCommand, List<Argument>> parse(String input) {
    String[] inputSplit = input.split(" ");
    for (CallableCommand command : CommandRegistry.getCommands()) {
      processor.validateAnnotations(command);

      if (command.getClass().getAnnotation(CliCommand.class).name()
          .equalsIgnoreCase(inputSplit[0])) {

        String[] args = Arrays.copyOfRange(inputSplit, 1, inputSplit.length);
        return Map.of(command, getArguments(args));

      }
    }
    throw new NoCommandFoundException("No command registered with name: " + input);
  }

  private static List<Argument> getArguments(String[] inputArgs) {
    List<Argument> arguments = new ArrayList<>();
    for (int i = 0; i < inputArgs.length; i++) {
      Argument argument = new Argument();
      if (i % 2 == 0) {
        argument.setName(inputArgs[i]);
      } else argument.setValue(inputArgs[i]);

      arguments.add(argument);
    }
    return arguments;
  }
}

