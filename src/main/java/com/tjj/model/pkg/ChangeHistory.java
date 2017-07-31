package com.tjj.model.pkg;

import javax.persistence.*;

@Entity @Table(name = "changehistory")
public class ChangeHistory {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  @Column(name="ch_date",nullable =false)
  private String date;
  @Column(name="ch_programmer",nullable =false)
  private String programmer;
  @Column(name="ch_reason",nullable =false)
  private String reason;

  public ChangeHistory() {
  }

  public ChangeHistory(String date, String programmer, String reason) {
    this.date = date;
    this.programmer = programmer;
    this.reason = reason;

  }

  public String getDate() {
    return date;
  }

  public String getProgrammer() {
    return programmer;
  }

  public String getReason() {
    return reason;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public void setProgrammer(String programmer) {
    this.programmer = programmer;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  @Override public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    ChangeHistory that = (ChangeHistory) o;

    if (id != null ? !id.equals(that.id) : that.id != null)
      return false;
    if (date != null ? !date.equals(that.date) : that.date != null)
      return false;
    if (programmer != null ? !programmer.equals(that.programmer) : that.programmer != null)
      return false;
    return reason != null ? reason.equals(that.reason) : that.reason == null;
  }

  @Override public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (date != null ? date.hashCode() : 0);
    result = 31 * result + (programmer != null ? programmer.hashCode() : 0);
    result = 31 * result + (reason != null ? reason.hashCode() : 0);
    return result;
  }

  @Override public String toString() {
    return "ChangeHistory{" + "id=" + id + ", date='" + date + '\'' + ", programmer='" + programmer
        + '\'' + ", reason='" + reason + '\'' + '}';
  }
}
