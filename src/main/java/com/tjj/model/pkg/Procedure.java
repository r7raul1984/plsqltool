package com.tjj.model.pkg;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "pkg_procedure")
public class Procedure {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  @Column(name="p_name",nullable =false)
  private String procedureName;
  @Column(name="p_bodyname",nullable =false)
  private String packageBodyName;
  @Column(name="p_date")
  private String date;
  @Column(name="p_programmer")
  private String programmer;
  @Column(name="p_overview")
  private String overview;
  @OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
  @OrderColumn
  private List<ChangeHistory> changeHistory = new ArrayList<>();
  @OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
  @OrderColumn
  private List<Variable> vars = new ArrayList<>();
  @ElementCollection(fetch=FetchType.EAGER)
  @CollectionTable(name = "procedure_tmptables")
  @OrderColumn
  private List<String> tmpTables = new ArrayList<>();
  @OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
  @OrderColumn
  private List<Step> steps = new ArrayList<>();
  @Column(name="p_targettable",nullable =false)
  private String targetTable;

  public Procedure() {
  }

  public Procedure(String procedureName, String packageBodyName, String date, String programmer,
      String overview, List<ChangeHistory> changeHistory, List<Variable> vars,
      List<String> tmpTables, String targetTable) {
    this.procedureName = procedureName;
    this.packageBodyName = packageBodyName;
    this.date = date;
    this.programmer = programmer;
    this.overview = overview;
    this.changeHistory = changeHistory;
    this.vars = vars;
    this.targetTable = targetTable;
    this.tmpTables = tmpTables;
    vars.add(new Variable("v_sp_name", "VARCHAR2(100)",
        "c_package_name || '." + this.getProcedureName().toUpperCase() + "'"));
    vars.addAll(getTableVars());
  }

  public List<Step> getSteps() {
    return steps;
  }

  public List<Variable> getTableVars() {

    List<Variable> vars = new ArrayList<>();
    int i = 1;
    for (String tmp : tmpTables) {
      vars.add(new Variable("v_tmp_table" + i, "VARCHAR2(50)", "'" + tmp.toUpperCase() + "'"));
      i++;
    }
    vars.add(new Variable("v_target_table", "VARCHAR2(50)", "'" + targetTable.toUpperCase() + "'"));
    return vars;
  }

  public List<Variable> getTmpTableVars() {
    List<Variable> vars = new ArrayList<>();
    int i = 1;
    for (String tmp : tmpTables) {
      vars.add(new Variable("v_tmp_table" + i, "VARCHAR2(50)", "'" + tmp.toUpperCase() + "'"));
      i++;
    }
    return vars;
  }
  public void addSteps(List<Step> psteps){
    steps.addAll(psteps);
  }
  public List<String> getTmpTables() {
    return tmpTables;
  }

  public String getTargetTable() {
    return targetTable;
  }

  public String getPackageBodyName() {
    return packageBodyName;
  }

  public String getProcedureName() {
    return procedureName;
  }

  public String getDate() {
    return date;
  }

  public String getProgrammer() {
    return programmer;
  }

  public String getOverview() {
    return overview;
  }

  public List<ChangeHistory> getChangeHistory() {
    return changeHistory;
  }

  public List<Variable> getVars() {
    return vars;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setProcedureName(String procedureName) {
    this.procedureName = procedureName;
  }

  public void setPackageBodyName(String packageBodyName) {
    this.packageBodyName = packageBodyName;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public void setProgrammer(String programmer) {
    this.programmer = programmer;
  }

  public void setOverview(String overview) {
    this.overview = overview;
  }

  public void setChangeHistory(List<ChangeHistory> changeHistory) {
    this.changeHistory = changeHistory;
  }

  public void setVars(List<Variable> vars) {
    this.vars = vars;
  }

  public void setTmpTables(List<String> tmpTables) {
    this.tmpTables = tmpTables;
  }

  public void setSteps(List<Step> steps) {
    this.steps = steps;
  }

  public void setTargetTable(String targetTable) {
    this.targetTable = targetTable;
  }

}
