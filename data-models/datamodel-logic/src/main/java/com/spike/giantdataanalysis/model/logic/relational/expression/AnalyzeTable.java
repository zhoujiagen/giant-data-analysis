package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
 analyzeTable
    : ANALYZE actionOption=(NO_WRITE_TO_BINLOG | LOCAL)?
       TABLE tables
    ;
 * </pre>
 */
public class AnalyzeTable implements AdministrationStatement {
}
