package de.tiiita.cli;

import de.tiiita.cli.exception.MissingAnnotationException;
import de.tiiita.cli.exception.NoArgumentFoundException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.Callable;
import org.jetbrains.annotations.Nullable;

class CommandProcessor {

  void process(Object command, List<ArgumentModel> args) {
    injectArguments(command, args);
    try {
      ((Callable<?>) command).call();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void injectArguments(Object command, List<ArgumentModel> args) {
    for (Field declaredField : command.getClass().getDeclaredFields()) {
      if (!declaredField.isAnnotationPresent(Argument.class)) {
        continue;
      }

      Argument argumentAnnotation = declaredField.getAnnotation(Argument.class);

      declaredField.setAccessible(true);
      try {
        ArgumentModel argByName = getArgByName(argumentAnnotation.name(), args);
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
  private ArgumentModel getArgByName(String name, List<ArgumentModel> args) {
    for (ArgumentModel arg : args) {
      if (arg.getName().equalsIgnoreCase(name)) {
        return arg;
      }
    }

    return null;
  }

  public void validateAnnotations(Object command) {
    if (!command.getClass().isAnnotationPresent(Command.class)) {
      throw new MissingAnnotationException("Command class has to have CliCommand.class annotation");
    }
  }
}
