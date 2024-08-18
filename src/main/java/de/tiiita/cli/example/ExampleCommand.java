package de.tiiita.cli.example;

import de.tiiita.cli.Argument;
import de.tiiita.cli.Command;
import java.util.concurrent.Callable;

/**
 * This is an example of how your command could look like.
 */


//Set the command name and description
@Command(name = "example", description = "Optional test description")
public class ExampleCommand implements Callable<Void> {


  //Define an argument that is required and has the name show
  @Argument(name = "show", optional = false)
  private String showArgument;

  //Define another argument with the name test, this one is optional
  @Argument(name = "test", optional = true)
  private String testArgument;

  @Override
  public Void call() throws Exception {
    //This just prints a message with the arguments, implement your own
    //logic here as well!

    System.out.println("Test command executed, injected arguments: "
        + showArgument + ", " + testArgument);
    return null;
  }
}
