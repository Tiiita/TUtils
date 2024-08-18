package de.tiiita.cli;

import de.tiiita.cli.ArgumentModel;
import de.tiiita.cli.CommandProcessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;


/**
 * In this class are any methods to configure the cli.
 */
public class CommandService {

  private static final List<CliCommand> commands = new ArrayList<>();
  private static final CommandParser parser;
  private static final CommandProcessor processor;

  static {
    parser = new CommandParser();
    processor = new CommandProcessor();
  }

  /**
   * Starts listen to the console for input. This blocks the current thread until the scanner found
   * a next line {@link Scanner#nextLine()}. The found line will then be parsed and processed by the
   * {@link CommandProcessor}.
   */
  public static void listen() {
    String input = new Scanner(System.in).nextLine();
    if (input.isBlank()) {
      return;
    }

    Map<CliCommand, List<ArgumentModel>> parsedInput = parser.parse(input);
    if (parsedInput == null) {
      return;
    }

    Entry<CliCommand, List<ArgumentModel>> next = parsedInput.entrySet().iterator().next();
    processor.process(next.getKey(), next.getValue());
  }

  /**
   * Register a new command by giving the instance of the command. This method only adds the command
   * to a list where the processor and parser searches from later.
   *
   * @param command the command instance. This has implement {@link Callable} and needs to have the
   *                {@link Command} annotation, otherwise an exception is being thrown.
   */
  public static void register(@NotNull CliCommand command) {
    commands.add(command);
  }

  /**
   * This generates the usage using reflections and the annotation information.
   *
   * @param command the command instance.
   * @return the usage of the command
   */
  public static String getUsage(CliCommand command) {
    StringBuilder usageBuilder = new StringBuilder();
    usageBuilder.append(command.getClass().getAnnotation(Command.class).name());
    String subCommandPart = command.getSubCommands().isEmpty() ? ""
        : command.getSubCommands()
            .stream()
            .map(sub -> sub.getClass().getAnnotation(Command.class).name())
            .collect(Collectors.toList())
            .toString();

    for (Argument argument : getArgumentsSorted(command)) {

      String argumentPart = argument.optional() ? "[" + argument.name() + "]"
          : "<" + argument.name() + ">";

      usageBuilder.append(" ").append(argumentPart);
    }

    usageBuilder.append(" ").append(subCommandPart);
    return usageBuilder.toString();
  }


  private static List<Argument> getArgumentsSorted(Object command) {
    return Arrays.stream(command.getClass().getDeclaredFields())
        .filter(field -> field.isAnnotationPresent(Argument.class))
        .map(field -> field.getAnnotation(Argument.class))
        .sorted(Comparator.comparing(Argument::optional))
        .collect(Collectors.toList());
  }

  public static List<CliCommand> getCommands() {
    return new ArrayList<>(commands);
  }
}
