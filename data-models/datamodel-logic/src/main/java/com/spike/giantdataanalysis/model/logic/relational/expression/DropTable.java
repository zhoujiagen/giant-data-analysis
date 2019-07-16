package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
 dropTable
    : DROP TEMPORARY? TABLE ifExists?
      tables dropType=(RESTRICT | CASCADE)?
    ;
 * </pre>
 */
public class DropTable implements DdlStatement {
}
