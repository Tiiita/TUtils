package de.tiiita.shell;

import de.tiiita.Collections;
import de.tiiita.StringUtils;
import de.tiiita.shell.exception.CommandNotFoundException;
import de.tiiita.shell.exception.CommandSyntaxException;
import de.tiiita.shell.exception.MissingAnnotationException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.jetbrains.annotations.Nullable;

class CommandProcessor {

  private final Map<Field, Object> fieldValueMap = new HashMap<>();

  /**
   * This method can process the parsed input. If the command has sub commands it will recursive
   * process the sub command as normal command. This also works with sub commands of sub commands.
   * Using valued and non-valued you can make any kind of command hierarchy.
   * <p></p>
   * This will also inject the value of the user input into the argument fields.
   *
   * @param input the data class which contains all information needed to process.
   */
  void process(ProcessableInput input)
      throws CommandSyntaxException, CommandNotFoundException {

    ShellCommand command = input.getCommand();
    List<ArgumentModel> args = input.getParsedArguments();
    String[] rawInput = input.getRawInput();

    for (ShellCommand subCommand : command.getSubCommands()) {
      if (isSubCommandInRawInput(rawInput, subCommand)) {
        //Recursive move through sub command hierarchy until list
        // end and run the sub command as normal
        rawInput = Collections.removeFromArray(rawInput, 0);
        process(new ProcessableInput(args, rawInput, subCommand));
      }
    }

    inject(command, args);
    command.execute();
    uninject(command);
  }

  private boolean isSubCommandInRawInput(String[] rawInput, ShellCommand subCommand) {
    for (String inputArg : rawInput) {
      Command subCommandAnnotation = subCommand.getClass().getAnnotation(Command.class);

      if (inputArg.equalsIgnoreCase(subCommandAnnotation.name())) {
        return true;
      }
    }

    return false;
  }


  /**
   * This resets the value of each field that it was before injection.
   * @param command the command, must be the same that  {@link #inject(ShellCommand, List)} used.
   */
  private void uninject(ShellCommand command) {
    for (Entry<Field, Object> entry : fieldValueMap.entrySet()) {
      try {
        entry.getKey().set(command, entry.getValue());
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    }

    fieldValueMap.clear();
  }

  /**
   * This gets the argument models and the command and injects the argument values into every field
   * in the command instance where the {@link Argument} annotation was found. If there is no
   * argument found but the argument is required {@link Argument#optional()} the usage will be
   * print. If the value of the argument is null it will not inject anything into the declared
   * variable, which means the default value will stay.
   *
   * @param command    the command instance
   * @param parsedArgs the arguments models parsed by the parser.
   * @throws CommandSyntaxException if the command syntax was wrong. The exception message holds the
   *                                right usage message, you can print when handling.
   */
  private void inject(ShellCommand command, List<ArgumentModel> parsedArgs)
      throws CommandSyntaxException {

    for (Field declaredField : command.getClass().getDeclaredFields()) {
      if (!declaredField.isAnnotationPresent(Argument.class)) {
        continue;
      }

      Argument argumentAnnotation = declaredField.getAnnotation(Argument.class);
      declaredField.setAccessible(true);
      try {
        ArgumentModel givenArgument = getArgByName(argumentAnnotation.name(), parsedArgs);

        //Is null if we pass in an argument that is not defined,
        //or we do not pass in an argument that is needed.
        if (!argumentAnnotation.optional() && givenArgument == null) {
          //Example cmd: '-a' is missing
          throw new CommandSyntaxException(CommandService.getUsage(command));
        }

        if (givenArgument == null) {
          return;
        }

        fieldValueMap.put(declaredField, declaredField.get(command));
        if (argumentAnnotation.valued()) {
          declaredField.set(command, givenArgument.getValue());
          continue;
        }

        declaredField.setBoolean(command, givenArgument.isValued());

      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    }
  }

  /**
   * This is just a simple method to get an argument by its name.
   *
   * @param name the name the method should search for.
   * @param args the arguments the method should search in.
   * @return the found argument model or null if no name matched with the given args.
   */
  @Nullable
  private ArgumentModel getArgByName(String name, List<ArgumentModel> args) {
    for (ArgumentModel arg : args) {
      if (arg.getName().equalsIgnoreCase(name)) {
        return arg;
      }
    }

    return null;
  }

  /**
   * This simple method validates the command instance has the command annotation, so we do not get
   * unexplainable errors in the later process.
   *
   * @param command the command instance.
   */
  void validateAnnotations(Object command) {
    if (!command.getClass().isAnnotationPresent(Command.class)) {
      throw new MissingAnnotationException("Command class has to have CliCommand.class annotation");
    }
  }
}
