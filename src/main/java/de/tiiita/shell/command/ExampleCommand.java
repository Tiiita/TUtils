package de.tiiita.shell.command;

import de.tiiita.shell.Argument;
import de.tiiita.shell.ShellCommand;
import de.tiiita.shell.Command;

/**
 * This is an example of how your command could look like.
 */

//Set the command name and description
@Command(name = "example", description = "Optional test description")
public class ExampleCommand extends ShellCommand {

  //Define an argument that is required and has the name show
  @Argument(name = "-a", optional = false, valued = true)
  private String aArgument;

  //Define another argument with the name test, this one is optional
  @Argument(name = "-b", optional = true, valued = false)
  private boolean bArgument;


  public ExampleCommand() {
    //Add a sub command
    addSub(new ExampleSubCommand());
  }

  @Override
  protected void execute() {
    //This just prints a message with the arguments, implement your own
    //logic here as well!

    System.out.println("Test command executed, injected arguments: "
        + aArgument + ", " + bArgument);
  }
}
