package de.tiiita.shell;

import de.tiiita.shell.exception.CommandNotFoundException;
import de.tiiita.shell.exception.CommandSyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;


/**
 * In this class are any methods to configure the cli.
 */
public class CommandService {

  private static final List<ShellCommand> commands = new ArrayList<>();
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
   * @throws CommandNotFoundException if the command could not be found by the console input parser.
   * you should handle this exception by printing a not found message to the console for example.
   * @throws CommandSyntaxException if the command was found but the syntax were wrong, this exception
   * gets thrown. The exception message holds the right usage you can print or whatever.
   */
  public static void listen() throws CommandNotFoundException, CommandSyntaxException {
    String input = new Scanner(System.in).nextLine();
    if (input.isBlank()) {
      return;
    }

    processor.process(parser.parse(input));
  }

  /**
   * Register a new command by giving the instance of the command. This method only adds the command
   * to a list where the processor and parser searches from later.
   *
   * @param command the command instance. This has implement {@link Callable} and needs to have the
   *                {@link Command} annotation, otherwise an exception is being thrown.
   */
  public static void register(@NotNull ShellCommand command) {
    commands.add(command);
  }

  /**
   * This generates the usage using reflections and the annotation information.
   *
   * @param command the command instance.
   * @return the usage of the command
   */
  public static String getUsage(ShellCommand command) {
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


  static List<Argument> getArgumentsSorted(Object command) {
    return Arrays.stream(command.getClass().getDeclaredFields())
        .filter(field -> field.isAnnotationPresent(Argument.class))
        .map(field -> field.getAnnotation(Argument.class))
        .sorted(Comparator.comparing(Argument::optional))
        .collect(Collectors.toList());
  }

   static CommandParser getParser() {
    return parser;
  }
  public static List<ShellCommand> getCommands() {
    return new ArrayList<>(commands);
  }
}
