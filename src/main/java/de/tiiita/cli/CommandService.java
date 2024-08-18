package de.tiiita.cli;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.concurrent.Callable;

public class CommandService {

  private static final List<Callable<?>> commands = new ArrayList<>();
  private static final CommandParser parser;
  private static final CommandProcessor processor;

  static {
    parser = new CommandParser();
    processor = new CommandProcessor();
  }

  public static <T extends Callable<?>> void register(T command) {
    commands.add(command);
  }

  public static void listen() {
    String input = new Scanner(System.in).nextLine();

    Map<Callable<?>, List<ArgumentModel>> parsedInput = parser.parse(input);
    Entry<Callable<?>, List<ArgumentModel>> next = parsedInput.entrySet().iterator().next();
    processor.process(next.getKey(), next.getValue());
  }

  static List<Callable<?>> getCommands() {
    return commands;
  }
}
