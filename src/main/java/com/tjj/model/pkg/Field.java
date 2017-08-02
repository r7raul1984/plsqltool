package com.tjj.model.pkg;

public class Field {
  private String name;
  private String type;

  public Field(String name, String type) {
    this.name = name;
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @Override public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    Field field = (Field) o;

    return name != null ? name.equals(field.name) : field.name == null;
  }

  @Override public int hashCode() {
    return name != null ? name.hashCode() : 0;
  }

  @Override public String toString() {
    return "Field{" + "name='" + name + '\'' + ", type='" + type + '\'' + '}';
  }
}
