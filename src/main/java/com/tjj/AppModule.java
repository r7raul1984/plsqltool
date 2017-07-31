package com.tjj;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.tjj.metagen.MetaMaker;
import com.tjj.service.MetaService;
import com.tjj.service.VelocityService;
import com.tjj.templategen.TemplateMaker;

public class AppModule extends AbstractModule {

  @Override protected void configure() {
    bind(TemplateMaker.class).to(VelocityService.class).in(Singleton.class);
    bind(MetaMaker.class).to(MetaService.class).in(Singleton.class);
  }
}
