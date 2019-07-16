package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
 createView
    : CREATE (OR REPLACE)?
      (
        ALGORITHM '=' algType=(UNDEFINED | MERGE | TEMPTABLE)
      )?
      ownerStatement?
      (SQL SECURITY secContext=(DEFINER | INVOKER))?
      VIEW fullId ('(' uidList ')')? AS selectStatement
      (WITH checkOption=(CASCADED | LOCAL)? CHECK OPTION)?
    ;
 * </pre>
 */
public class CreateView implements DdlStatement {
}
