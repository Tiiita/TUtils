package de.tiiita.cli.example;


import de.tiiita.Logger;
import de.tiiita.cli.CallableCommand;
import de.tiiita.cli.CliArgument;
import de.tiiita.cli.CliCommand;

@CliCommand(name = "example", description = "This is an example command")
public class ExampleCommand implements CallableCommand {

  @CliArgument(name = "test", optional = false)
  private String argument;

  @Override
  public void execute() {
    //Command logic here
    Logger.logInfo("Executed test command, argument: " + argument);
     ;
  }
}
