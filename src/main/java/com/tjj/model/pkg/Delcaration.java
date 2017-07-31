package com.tjj.model.pkg;

import java.util.List;

public class Delcaration {
  private String packageName;
  private List<String> procedureNames;

  public Delcaration() {
  }

  public Delcaration(String packageName, List<String> procedureNames) {
    this.packageName = packageName;
    this.procedureNames = procedureNames;
  }

  public String getPackageName() {
    return packageName;
  }

  public List<String> getProcedureNames() {
    return procedureNames;
  }
}
