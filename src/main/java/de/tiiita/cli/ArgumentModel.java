package de.tiiita.cli;


/**
 * This represents a command argument
 * so you can get information easier.
 */
class ArgumentModel {
  private String name;
  private String value;

  public void setName(String name) {
    this.name = name;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public String getValue() {
    return value;
  }
}
