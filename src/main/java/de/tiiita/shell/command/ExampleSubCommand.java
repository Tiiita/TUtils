package de.tiiita.shell.command;

import de.tiiita.shell.Argument;
import de.tiiita.shell.ShellCommand;
import de.tiiita.shell.Command;

@Command(name = "sub")
public class ExampleSubCommand extends ShellCommand {

  @Argument(name = "-a", optional = false, valued = true)
  private String argument;

  public ExampleSubCommand() {
    addSub(new ExampleSubSubCommand());
  }

  @Override
  protected void execute() {
    System.out.println("Sub command executed, argument: " + argument);
  }
}
