package com.tjj.metagen;

import com.tjj.model.pkg.Procedure;
import net.sf.jsqlparser.JSQLParserException;

import java.util.List;

public interface MetaMaker {

  List<String> genTableDDLForP(Procedure p) throws JSQLParserException;

}
