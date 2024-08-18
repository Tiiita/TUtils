package de.tiiita.cli.command;

import de.tiiita.cli.CliCommand;
import de.tiiita.cli.Command;


@Command(name = "sub")
public class ExampleSubCommand extends CliCommand {

  @Override
  protected void execute() {
    System.out.println("Sub command executed");
  }
}
