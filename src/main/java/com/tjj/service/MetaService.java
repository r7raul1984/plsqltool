package com.tjj.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.tjj.metagen.MetaMaker;
import com.tjj.model.pkg.Field;
import com.tjj.model.pkg.PTable;
import com.tjj.model.pkg.Procedure;
import com.tjj.model.pkg.Step;
import net.sf.jsqlparser.JSQLParserException;
import org.apache.commons.lang.StringUtils;

import java.util.*;

@Singleton
public class MetaService implements MetaMaker {

  private VelocityService velocityService;
  private static Map<String, String> suffix2Type = Maps.newHashMap();

  static {
    suffix2Type.put("date_id", "DATE");
    suffix2Type.put("biz_unit", "NUMBER(18)");
    suffix2Type.put("_date", "DATE");
    suffix2Type.put("_time", "DATE");
    suffix2Type.put("_otr", "NUMBER(18,6)");
    suffix2Type.put("_amt", "NUMBER(18,6)");
    suffix2Type.put("_num", "NUMBER(18)");
    suffix2Type.put("_price", "NUMBER(18,6)");
    suffix2Type.put("_cnt", "NUMBER(18,6)");
    suffix2Type.put("_flg", "NUMBER(18)");
    suffix2Type.put("_rate", "NUMBER(18,6)");
    suffix2Type.put("_name", "VARCHAR2(200)");
    suffix2Type.put("_uv", "NUMBER(18)");
    suffix2Type.put("_id", "NUMBER(18)");
    suffix2Type.put("aos", "NUMBER(18,6)");

  }

  @Inject public MetaService(VelocityService velocityService) {
    this.velocityService = velocityService;
  }

  @Override public List<String> genTableDDLForP(Procedure p) throws JSQLParserException {

    List<Step> steps = p.getSteps();
    List<Field> fields = Lists.newArrayList();
    List<PTable> tbls = Lists.newArrayList();
    String currentTable = "";
    for (Step s : steps) {
      if (s.getType() != Step.INSERT) {
        continue;
      }
      String targetTbl = s.getTargetTable();
      if (StringUtils.isNotBlank(currentTable) && !targetTbl.equalsIgnoreCase(currentTable)) {
        genPTable(fields, tbls, currentTable);
        fields = Lists.newArrayList();//reset
      }
      currentTable = targetTbl;
      List<String> cols = s.getTargetTableFieldNames();
      for (String col : cols) {
        Set<String> suffixs = suffix2Type.keySet();
        Field f = new Field(col, "UNKNOW");
        for (String suffix : suffixs) {
          if (col.endsWith(suffix)) {
            f.setType(suffix2Type.get(suffix));
            break;
          }
        }
        //still unknow
        if(f.getType().equals("UNKNOW")){
          for (String suffix : suffixs) {
            if (col.contains(suffix)) {
              f.setType(suffix2Type.get(suffix));
              break;
            }
          }
        }
        fields.add(f);
      }
    }
    if(!fields.isEmpty()){//last step tbl
      genPTable(fields, tbls, currentTable);
    }
    List<String> rs = Lists.newArrayList();

    for (PTable t : tbls) {
      rs.add(velocityService.makeTable(t));
    }
    return rs;
  }

  private void genPTable(List<Field> fields, List<PTable> tbls, String currentTable) {
    List<Field> fs = new ArrayList<>(Sets.newHashSet(fields));
    String schema;
    if (currentTable.startsWith("TMP") || currentTable.startsWith("tmp")) {
      schema = "elt";
    } else {
      schema = "rpt";
      //fs.add(new Field("etl_time", "DATE"));
    }
    Collections.sort(fs);
    tbls.add(new PTable(schema, currentTable, fs));
  }
}
