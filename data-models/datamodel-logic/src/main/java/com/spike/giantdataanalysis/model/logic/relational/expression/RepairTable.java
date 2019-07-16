package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
 repairTable
    : REPAIR actionOption=(NO_WRITE_TO_BINLOG | LOCAL)?
      TABLE tables
      QUICK? EXTENDED? USE_FRM?
    ;
 * </pre>
 */
public class RepairTable implements AdministrationStatement {
}
