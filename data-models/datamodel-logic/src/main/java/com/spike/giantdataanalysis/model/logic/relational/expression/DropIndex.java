package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
 dropIndex
    : DROP INDEX intimeAction=(ONLINE | OFFLINE)?
      uid ON tableName
      (
        ALGORITHM '='? algType=(DEFAULT | INPLACE | COPY)
        | LOCK '='?
          lockType=(DEFAULT | NONE | SHARED | EXCLUSIVE)
      )*
    ;
 * </pre>
 */
public class DropIndex implements DdlStatement {
}
