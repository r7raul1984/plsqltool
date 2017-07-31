package com.tjj.service;

import com.google.inject.Singleton;
import com.tjj.model.pkg.*;
import com.tjj.templategen.TemplateMaker;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.StringWriter;
import java.util.List;

@Singleton
public class VelocityService implements TemplateMaker{

  private static final VelocityEngine ve = new VelocityEngine();
  static{
    ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
    ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
    ve.setProperty(Velocity.ENCODING_DEFAULT, "UTF8");
    ve.setProperty(Velocity.INPUT_ENCODING, "UTF8");
    ve.setProperty(Velocity.OUTPUT_ENCODING, "UTF8");
    ve.init();
  }


  public String makeChangeHistory(List<ChangeHistory> histories){
    Template t = ve.getTemplate("changehistory1.vm");
    VelocityContext ctx = new VelocityContext();
    ctx.put("historylist", histories);
    StringWriter sw = new StringWriter();
    t.merge(ctx, sw);
    return sw.toString();
  }

  @Override public String makeProcedure(List<Procedure> procedures) {
    Template t = ve.getTemplate("procedure.vm");
    VelocityContext ctx = new VelocityContext();
    ctx.put("procedures", procedures);
    ctx.put("strutil", new StringUtils());
    StringWriter sw = new StringWriter();
    t.merge(ctx, sw);
    return sw.toString();
  }

  @Override public String makeDelcaration(Delcaration delcaration) {
    Template t = ve.getTemplate("delcare.vm");
    VelocityContext ctx = new VelocityContext();
    ctx.put("delcaration", delcaration);
    StringWriter sw = new StringWriter();
    t.merge(ctx, sw);
    return sw.toString();
  }

  @Override public String makePackageBody(PackageBody packageBody) {
    Template t = ve.getTemplate("packagebody.vm");
    VelocityContext ctx = new VelocityContext();
    ctx.put("pb", packageBody);
    ctx.put("procedures", packageBody.getProcedures());
    ctx.put("strutil", new StringUtils());
    StringWriter sw = new StringWriter();
    t.merge(ctx, sw);
    return sw.toString();
  }

  @Override public String makeTable(PTable table) {
    Template t = ve.getTemplate("createtable.vm");
    VelocityContext ctx = new VelocityContext();
    ctx.put("t", table);
    ctx.put("strutil", new StringUtils());
    StringWriter sw = new StringWriter();
    t.merge(ctx, sw);
    return sw.toString();
  }

}
