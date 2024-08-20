package de.tiiita.cli;

import de.tiiita.Logger;
import de.tiiita.cli.exception.CommandSyntaxException;
import de.tiiita.cli.exception.MissingAnnotationException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import org.jetbrains.annotations.Nullable;

class CommandProcessor {

  /**
   * Injects the parsed arguments into the command instance
   * {@link #injectArguments(CliCommand, List)}. It then calls the super method of callable which
   * executes the command implementation.
   *
   * @param processableInput the data class which contains all information needed to process.
   */
  protected void process(ProcessableInput processableInput)
      throws CommandSyntaxException {

    CliCommand command = processableInput.getCommand();
    List<ArgumentModel> args = processableInput.getParsedArguments();

    for (CliCommand subCommand : command.getSubCommands()) {
      if (isSubCommandInRawInput(processableInput.getRawInput(), subCommand)) {
        subCommand.execute();
        return;
      }
    }

    injectArguments(command, args);
    command.execute();
  }

  private boolean isSubCommandInRawInput(String[] rawInput, CliCommand subCommand) {
    for (String inputArg : rawInput) {
      Command subCommandAnnotation = subCommand.getClass().getAnnotation(Command.class);

      if (inputArg.equalsIgnoreCase(subCommandAnnotation.name())) {
        return true;
      }
    }

    return false;
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
  private void injectArguments(CliCommand command, List<ArgumentModel> parsedArgs)
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
