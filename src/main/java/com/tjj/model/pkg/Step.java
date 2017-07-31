package com.tjj.model.pkg;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.statement.update.Update;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Entity @javax.persistence.Table(name = "step")
public class Step {
  public static final int INSERT = 1;
  private static final int UPDATE = 2;
  private static final int DELETE = 3;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  @Column(name="step_sql",length = 3500,nullable =false)
  private String sql;
  @Column(name="step_comment")
  private String comment;
  @Column(name="step_enable")
  private boolean enable = Boolean.TRUE;

  public Step() {
  }

  public Step(String sql, String comment) throws JSQLParserException {
    this.sql = removChineseSpace(sql);
    this.comment = comment;
  }

  public String getTargetTable() throws JSQLParserException {
    Statement stmt = CCJSqlParserUtil.parse(sql);
    String targetTable = "";
    if (stmt instanceof Insert) {
      Insert insert = (Insert) stmt;
      targetTable = insert.getTable().getName();
    } else if (stmt instanceof Update) {
      //TODO
    } else {
      Delete delete = (Delete) stmt;
      targetTable = delete.getTable().getName();
      //TODO
    }
    return targetTable;
  }

  public List<String> getSourceTables() throws JSQLParserException {
    List<String> sourceTables = new ArrayList<>();
    Statement stmt = CCJSqlParserUtil.parse(sql);
    if (stmt instanceof Insert) {
      Insert insert = (Insert) stmt;
      SelectBody selectBody = insert.getSelect().getSelectBody();
      extractSourceTableFromSB(sourceTables, selectBody);

    } else if (stmt instanceof Update) {
      //TODO
    } else if (stmt instanceof Delete) {
      //TODO
    }
    return sourceTables;
  }

  public List<String> getTargetTableFieldNames() throws JSQLParserException {
    Statement stmt = CCJSqlParserUtil.parse(sql);
    List<String> result = new ArrayList<>();
    if (stmt instanceof Insert) {
      Insert insert = (Insert) stmt;
      List<net.sf.jsqlparser.schema.Column> cols = insert.getColumns();
      for (net.sf.jsqlparser.schema.Column col:cols) {
        result.add(col.getColumnName());
      }
    }
    return result;
  }

  private void extractSourceTableFromSB(List<String> sourceTables, SelectBody selectBody) {
    if (selectBody instanceof PlainSelect) {
      PlainSelect ps = (PlainSelect) selectBody;
      extractSourceTable(sourceTables, ps);
    } else if (selectBody instanceof SetOperationList) {
      SetOperationList sol = (SetOperationList) selectBody;
      List<SelectBody> sbs = sol.getSelects();
      for (SelectBody sb : sbs) {
        PlainSelect ps = (PlainSelect) sb;
        extractSourceTable(sourceTables, ps);
      }
    }
  }

  private void extractSourceTable(List<String> sourceTables, PlainSelect ps) {
    FromItem fromItem = ps.getFromItem();
    if (fromItem instanceof SubSelect) {
      SubSelect subSelect = (SubSelect) fromItem;
      extractSourceTableFromSB(sourceTables, subSelect.getSelectBody());
    } else {
      Table table = (Table) fromItem;
      if (table.getSchemaName() == null) {
        sourceTables.add(table.getName());
      } else {
        sourceTables.add(table.getSchemaName() + "." + table.getName());
      }
      List<Join> joins = ps.getJoins();
      if (joins == null) {
        return;
      }
      for (Join j : joins) {
        Table jTable = (Table) j.getRightItem();
        if (jTable.getSchemaName() == null) {
          sourceTables.add(jTable.getName());
        } else {
          sourceTables.add(jTable.getSchemaName() + "." + jTable.getName());
        }

      }
    }
  }

  public String getSourceTablesStr() throws JSQLParserException {
    return getSourceTables().toString().toUpperCase();
  }

  public boolean isEnable() {
    return enable;
  }

  public int getType() throws JSQLParserException {
    Statement stmt = CCJSqlParserUtil.parse(sql);
    int type;
    if (stmt instanceof Insert) {
      type = INSERT;
    } else if (stmt instanceof Update) {
      type = UPDATE;
    } else {
      type = DELETE;
    }
    return type;
  }

  private String removChineseSpace(String sql) {
    return sql.replaceAll("[\\u3000]", " ");
  }

  public void enable() {
    this.enable = true;
  }

  public void disable() {
    this.enable = false;
  }

  public String getSql() {
    return sql;
  }

  public String getComment() {
    return comment;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setSql(String sql) {
    this.sql = removChineseSpace(sql);
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public void setEnable(boolean enable) {
    this.enable = enable;
  }
  @Override public String toString() {
    return "Step{" + "id=" + id + ", sql='" + sql + '\'' + ", comment='" + comment + '\''
        + ", enable=" + enable + '}';
  }

}
