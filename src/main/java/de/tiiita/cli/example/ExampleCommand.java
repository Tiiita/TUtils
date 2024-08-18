package de.tiiita.cli.example;

import de.tiiita.Logger;
import de.tiiita.cli.Argument;
import de.tiiita.cli.Command;
import java.util.concurrent.Callable;

@Command(name = "example", description = "This is an example command")
public class ExampleCommand implements Callable<Void> {

  @Argument(name = "test", optional = true)
  private String argument;

  @Override
  public Void call() {
    //Command logic here
    Logger.logInfo("Executed test command, argument: " + argument);
    return null;
  }
}
