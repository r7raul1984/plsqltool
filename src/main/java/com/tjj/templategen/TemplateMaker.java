package com.tjj.templategen;

import com.tjj.model.pkg.*;

import java.util.List;

public interface TemplateMaker {

  String makeChangeHistory(List<ChangeHistory> histories);

  String makeProcedure(List<Procedure> procedures);

  String makeDelcaration(Delcaration delcaration);

  String makePackageBody(PackageBody packageBody);

  String makeTable(PTable table);

}
