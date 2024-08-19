package de.tiiita.cli.command;

import de.tiiita.cli.Argument;
import de.tiiita.cli.CliCommand;
import de.tiiita.cli.Command;

/**
 * This is an example of how your command could look like.
 */

//Set the command name and description
@Command(name = "example", description = "Optional test description")
public class ExampleCommand extends CliCommand {

  //Define an argument that is required and has the name show
  @Argument(name = "-a", optional = false)
  private String showArgument;

  //Define another argument with the name test, this one is optional
  @Argument(name = "-b", optional = true)
  private String testArgument;


  public ExampleCommand() {
    //Add a sub command
    addSub(new ExampleSubCommand());
    addSub(new HelpCommand());
  }

  @Override
  protected void execute() {
    //This just prints a message with the arguments, implement your own
    //logic here as well!

    System.out.println("Test command executed, injected arguments: "
        + showArgument + ", " + testArgument);
  }
}
