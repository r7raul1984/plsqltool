package com.tjj;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import com.tjj.metagen.MetaMaker;
import com.tjj.model.pkg.PackageBody;
import com.tjj.model.pkg.Procedure;
import com.tjj.model.pkg.Step;
import com.tjj.repository.PackageBodyRepository;
import com.tjj.service.MetaService;
import com.tjj.service.PackageBodyService;
import com.tjj.service.VelocityService;
import com.tjj.templategen.TemplateMaker;
import net.sf.jsqlparser.JSQLParserException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public class PlsqlGen {

  private static final String TEST_PERSISTENCE_UNIT_NAME = "localmysql";
  private static final String REPOSITORIES_BASE_PACKAGE_NAME = PackageBodyRepository.class
      .getPackage().getName();


  public static void main(String args[]) throws SQLException, IOException, JSQLParserException {

    Injector injector =  Guice.createInjector(Stage.PRODUCTION,
        new RepoModule(TEST_PERSISTENCE_UNIT_NAME, REPOSITORIES_BASE_PACKAGE_NAME));
    PackageBodyService packageBodyService = injector.getInstance(PackageBodyService.class);
    TemplateMaker tm = Guice.createInjector(new AppModule())
        .getInstance(VelocityService.class);
    MetaMaker mm = injector.getInstance(MetaService.class);
    if (args.length < 2) {
      System.out.println("ddl: make create table ddl script");
      System.out.println("delcare: make plsql delcare script");
      System.out.println("plsql: make plsql script");
      System.out.println("source: find all source table for procedure");
      return;
    }
    String cmd = args[0];
    Long id = Long.valueOf(args[1]);
    PackageBody pb = packageBodyService.get(id);
    if (cmd.equals("ddl")) {

      List<String> rs = Lists.newArrayList();
      List<Procedure> procedures = pb.getProcedures();
      for (Procedure p : procedures) {
        rs.addAll(mm.genTableDDLForP(p));
      }
      for (String r : rs) {
        System.out.println(r);
      }

    } else if (cmd.equals("plsql")) {
      System.out.println(tm.makePackageBody(pb));
    } else if (cmd.equals("declcare")) {
      System.out.println(tm.makeDelcaration(pb.getDelcaration()));
    } else if (cmd.equals("source")) {
      List<Procedure> procedures = pb.getProcedures();
      Set<String> result = Sets.newHashSet();
      for (Procedure p : procedures) {
        List<Step> steps = p.getSteps();
        for (Step step : steps) {
          result.addAll(step.getSourceTables());
        }
        System.out
            .println(p.getPackageBodyName() + "." + p.getProcedureName() + ":" + result.toString());
      }
    }

  }
}
