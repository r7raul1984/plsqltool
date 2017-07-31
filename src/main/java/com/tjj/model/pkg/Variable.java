package com.tjj.model.pkg;

import org.apache.commons.lang.StringUtils;

import javax.persistence.*;

@Entity @javax.persistence.Table(name = "variable")
public class Variable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  @Column(name = "var_name", nullable = false)
  private String name;
  @Column(name="var_type")
  private String type;
  @Column(name="var_value")
  private String value;
  @Column(name="var_initlogic")
  private String initLogic;

  public Variable() {
  }

  public Variable(String name, String type, String value, String initLogic) {
    this.name = name;
    this.type = type;
    this.value = value;
    this.initLogic = initLogic;
  }

  public Variable(String name, String type) {
    this(name, type, "", "");
  }

  public Variable(String name, String type, String value) {
    this(name, type, value, "");
  }
  public boolean needInit(){
    return StringUtils.isNotEmpty(initLogic);
  }

  public String getInitLogic() {
    return initLogic;
  }

  public String getName() {
    return name;
  }

  public String getType() {
    return type;
  }

  public String getValue() {
    return value;
  }

  public String getPureValue() {
    return StringUtils.remove(this.getValue(), "'");
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public void setInitLogic(String initLogic) {
    this.initLogic = initLogic;
  }

  @Override public String toString() {
    return "Variable{" + "id=" + id + ", name='" + name + '\'' + ", type='" + type + '\''
        + ", value='" + value + '\'' + ", initLogic='" + initLogic + '\'' + '}';
  }
}
