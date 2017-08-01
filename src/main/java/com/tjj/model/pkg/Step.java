package com.tjj.model.pkg;

import com.google.common.collect.Lists;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.StatementVisitorAdapter;
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
  public static final String COMB_TYPE_ID = "comb_type_id";
  @Transient
  private static List<String> combTypeTbls = Lists.newArrayList();

  static{
    combTypeTbls.add("bic.cust_crm_info_d");
  }

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

  private void collectTableName(Table table, PlainSelect plainSelect, List<String> sourceTables) {


    String tblName = table.getName();
    String schemaName = table.getSchemaName() == null ? "" : table.getSchemaName() + ".";
    String tblNameWithSchema = schemaName + tblName;
    for (String combTypeTbl : combTypeTbls) {
      if (combTypeTbl.equalsIgnoreCase(tblNameWithSchema)) {
        List<String> combTypeIds = Lists.newArrayList();
        findCombTypeId(plainSelect.getWhere(), combTypeIds);
        for (String combTypeId : combTypeIds) {
          sourceTables.add(tblNameWithSchema + "_" + combTypeId);
        }
        return;
      }
    }
    sourceTables.add(tblNameWithSchema);
  }

  private void extractSourceTable(final List<String> sourceTables, SelectBody selectBody) {
    selectBody.accept(new SelectVisitorAdapter(){

      @Override
      public void visit(SetOperationList setOpList) {
        List<SelectBody> sbs = setOpList.getSelects();
        for (SelectBody sb : sbs) {
          extractSourceTable(sourceTables, sb);
        }
      }

      @Override
      public void visit(final PlainSelect plainSelect) {
        plainSelect.getFromItem().accept(new FromItemVisitorAdapter(){
          @Override
          public void visit(Table table) {
            collectTableName(table, plainSelect, sourceTables);
          }
          @Override
          public void visit(SubSelect subSelect) {
            extractSourceTable(sourceTables,subSelect.getSelectBody());
          }
        });

        List<Join> joins = plainSelect.getJoins();
        if (joins == null) {
          return;
        }

        for (Join join : joins) {
          join.getRightItem().accept(new FromItemVisitorAdapter() {
            @Override
            public void visit(Table table) {
              collectTableName(table, plainSelect, sourceTables);
            }

            @Override
            public void visit(SubSelect subSelect) {
              extractSourceTable(sourceTables,subSelect.getSelectBody());
            }
          });

        }
      }

    });
  }

  public List<String> getSourceTables() throws JSQLParserException {
    final List<String> sourceTables = new ArrayList<>();
    Statement stmt = CCJSqlParserUtil.parse(sql);
    stmt.accept(new StatementVisitorAdapter() {

      @Override public void visit(Insert insert) {
        SelectBody selectBody = insert.getSelect().getSelectBody();
        extractSourceTable(sourceTables, selectBody);
      }
      @Override
      public void visit(Delete delete) {

      }

      @Override
      public void visit(Update update) {

      }
    });
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


  private void findCombTypeId(Expression where, final List<String> combTypeIds) {
    where.accept(new ExpressionVisitorAdapter() {
      @Override
      public void visit(final InExpression expr) {
        super.visit(expr);
        expr.getLeftExpression().accept(new ExpressionVisitorAdapter(){
          @Override
          public void visit(net.sf.jsqlparser.schema.Column column) {
            if(column.getColumnName().equalsIgnoreCase(COMB_TYPE_ID)){
              ItemsList itemsList = expr.getRightItemsList();
              itemsList.accept(new ItemsListVisitorAdapter(){
                @Override public void visit(ExpressionList expressionList) {
                  for (Expression expression: expressionList.getExpressions()) {
                    expression.accept(new ExpressionVisitorAdapter(){
                      @Override
                      public void visit(LongValue value) {
                        combTypeIds.add(value.getStringValue());
                      }
                      @Override
                      public void visit(StringValue value) {
                        combTypeIds.add(value.getValue());
                      }
                    });
                  }
                }
              });
            }
          }
        });
      }
      @Override
      public void visit(final EqualsTo expr) {
        super.visit(expr);
        expr.getLeftExpression().accept(new ExpressionVisitorAdapter(){
          @Override
          public void visit(net.sf.jsqlparser.schema.Column column) {
            if(column.getColumnName().equalsIgnoreCase(COMB_TYPE_ID)){
              expr.getRightExpression().accept(new ExpressionVisitorAdapter(){
                @Override
                public void visit(LongValue value) {
                  combTypeIds.add(value.getStringValue());
                }
                @Override
                public void visit(StringValue value) {
                  combTypeIds.add(value.getValue());

                }

              });
            }
          }
        });
      }
    });
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
