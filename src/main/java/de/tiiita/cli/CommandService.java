package de.tiiita.cli;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.concurrent.Callable;
import org.jetbrains.annotations.NotNull;


/**
 * In this class are any methods to configure the cli.
 */
public class CommandService {
  private static final List<Callable<?>> commands = new ArrayList<>();
  private static final CommandParser parser;
  private static final CommandProcessor processor;

  static {
    parser = new CommandParser();
    processor = new CommandProcessor();
  }

  /**
   * Starts listen to the console for input. This blocks the current thread
   * until the scanner found a next line {@link Scanner#nextLine()}.
   * The found line will then be parsed and processed by the {@link CommandProcessor}.
   */
  public static void listen() {
    String input = new Scanner(System.in).nextLine();

    Map<Callable<?>, List<ArgumentModel>> parsedInput = parser.parse(input);
    Entry<Callable<?>, List<ArgumentModel>> next = parsedInput.entrySet().iterator().next();
    processor.process(next.getKey(), next.getValue());
  }

  /**
   * Register a new command by giving the instance of the command.
   * This method only adds the command to a list where the processor and parser
   * searches from later.
   * @param command the command instance. This has implement {@link Callable}
   *                and needs to have the {@link Command} annotation,
   *                otherwise an exception is being thrown.
   */
  public static <T extends Callable<?>> void register(@NotNull T command) {
    commands.add(command);
  }

  /**
   * This generates the usage using reflections and the annotation information.
   * @param command the command instance.
   * @return the usage of the command
   */
  public static <T extends Callable<?>> String getUsage(T command) {
    StringBuilder usageBuilder = new StringBuilder();
    Argument[] arguments = command.getClass().getAnnotationsByType(Argument.class);

    usageBuilder.append(command.getClass().getAnnotation(Command.class).name());
    for (Argument argument : arguments) {
      String argumentPart = argument.optional() ? "[" + argument.name() + "]"
          : "<" + argument.name() + ">";

      usageBuilder.append(" ").append(argumentPart);
    }

    return usageBuilder.toString();
  }

  static List<Callable<?>> getCommands() {
    return new ArrayList<>(commands);
  }
}
