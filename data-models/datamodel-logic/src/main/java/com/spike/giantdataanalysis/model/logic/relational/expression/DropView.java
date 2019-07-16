package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
 dropView
    : DROP VIEW ifExists?
      fullId (',' fullId)* dropType=(RESTRICT | CASCADE)?
    ;
 * </pre>
 */
public class DropView implements DdlStatement {
}
