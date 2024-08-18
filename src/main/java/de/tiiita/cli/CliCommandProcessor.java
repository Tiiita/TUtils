package de.tiiita.cli;

import de.tiiita.cli.exception.MissingAnnotationException;
import de.tiiita.cli.exception.NoArgumentFoundException;
import java.lang.reflect.Field;
import java.util.List;
import org.jetbrains.annotations.Nullable;

public class CliCommandProcessor {

  void process(Object command, List<Argument> args) {
    injectArguments(command, args);
    ((CallableCommand) command).execute();
  }

  private void injectArguments(Object command, List<Argument> args) {
    for (Field declaredField : command.getClass().getDeclaredFields()) {
      if (!declaredField.isAnnotationPresent(CliArgument.class)) {
        continue;
      }

      CliArgument argumentAnnotation = declaredField.getAnnotation(CliArgument.class);

      declaredField.setAccessible(true);
      try {
        Argument argByName = getArgByName(argumentAnnotation.name(), args);
        if (!argumentAnnotation.optional() && argByName == null) {
          throw new NoArgumentFoundException(
              "Command needs argument: " + argumentAnnotation.name());
        }

        if (argByName != null && argByName.getValue() != null) {
          declaredField.set(this, argByName.getValue());
        }

      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    }

  }


  @Nullable
  private Argument getArgByName(String name, List<Argument> args) {
    for (Argument arg : args) {
      if (arg.getName().equalsIgnoreCase(name)) {
        return arg;
      }
    }

    return null;
  }

  public void validateAnnotations(Object command) {
    if (!command.getClass().isAnnotationPresent(CliCommand.class)) {
      throw new MissingAnnotationException("Command class has to have CliCommand.class annotation");
    }
  }
}
