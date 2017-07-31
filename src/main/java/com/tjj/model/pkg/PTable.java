package com.tjj.model.pkg;

import java.util.ArrayList;
import java.util.List;

public class PTable {

  private String schema;
  private String name;
  private List<Field> fields = new ArrayList<>();

  public PTable(String schema, String name, List<Field> fields) {
    this.schema = schema;
    this.name = name;
    this.fields = fields;
  }

  public String getSchema() {
    return schema;
  }

  public String getName() {
    return name;
  }

  public List<Field> getFields() {
    return fields;
  }
}
