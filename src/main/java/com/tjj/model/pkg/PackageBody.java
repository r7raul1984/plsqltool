package com.tjj.model.pkg;

import com.google.common.collect.Lists;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Entity @Table(name = "packagebody")
public class PackageBody {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  @Column(name = "pkg_name", nullable = false)
  private String packageBodyName;
  @Column(name = "pkg_date")
  private String date;
  @Column(name = "pkg_programmer")
  private String programmer;
  @Column(name = "pkg_overview")
  private String overview;
  @OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
  @OrderColumn
  private List<ChangeHistory> changeHistory = new ArrayList<>();
  @OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
  @OrderColumn
  private List<Procedure> procedures = new ArrayList<>();

  public PackageBody() {
  }

  public PackageBody(String packageBodyName, String date, String programmer, String overview,
      List<ChangeHistory> changeHistory, List<Procedure> procedures) {
    for (Procedure p : procedures) {
      if (!p.getPackageBodyName().equalsIgnoreCase(packageBodyName)) {
        throw new IllegalArgumentException(
            p.getProcedureName() + "'s pkg_name(" + p.getPackageBodyName()
                + ") is not equal to " + packageBodyName);
      }
    }

    this.packageBodyName = packageBodyName;
    this.date = date;
    this.programmer = programmer;
    this.overview = overview;
    this.changeHistory = changeHistory;
    this.procedures = procedures;
  }

  public Delcaration getDelcaration(){

    List<String> procedureNames = Lists.newArrayList();
    for (Procedure p : procedures) {
      procedureNames.add(p.getProcedureName());
    }
    return new Delcaration("pkg_mct_bah", procedureNames);
  }

  public String getPackageBodyName() {
    return packageBodyName;
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

  public List<Procedure> getProcedures() {
    return procedures;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public void setProcedures(List<Procedure> procedures) {
    this.procedures = procedures;
  }
}
