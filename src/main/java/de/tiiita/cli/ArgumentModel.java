package de.tiiita.cli;


import java.util.Optional;

/**
 * This represents a command argument so you can get information easier.
 */
class ArgumentModel {

  private String name;

  //Optional with value null means the given value of a valued arg is null
  //An Empty optional means non-valued argument parsed
  private String value;
  private boolean valued;

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public boolean isValued() {
    return valued;
  }

  public void setValued(boolean valued) {
    this.valued = valued;
  }

  @Override
  public String toString() {
    return "name: " + name + ", value: " + value + ", valued_arg: " + valued;
  }
}
