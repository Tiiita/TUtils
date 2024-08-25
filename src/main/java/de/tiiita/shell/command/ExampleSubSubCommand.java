package de.tiiita.shell.command;


import de.tiiita.shell.ShellCommand;
import de.tiiita.shell.Command;

@Command(name = "sub2")
public class ExampleSubSubCommand extends ShellCommand {

  @Override
  protected void execute() {
    System.out.println("Sub sub command executed");
  }
}
