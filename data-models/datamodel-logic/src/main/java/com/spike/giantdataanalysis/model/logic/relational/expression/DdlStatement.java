package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
ddlStatement
  : createDatabase | createEvent | createIndex
  | createLogfileGroup | createProcedure | createFunction
  | createServer | createTable | createTablespaceInnodb
  | createTablespaceNdb | createTrigger | createView
  | alterDatabase | alterEvent | alterFunction
  | alterInstance | alterLogfileGroup | alterProcedure
  | alterServer | alterTable | alterTablespace | alterView
  | dropDatabase | dropEvent | dropIndex
  | dropLogfileGroup | dropProcedure | dropFunction
  | dropServer | dropTable | dropTablespace
  | dropTrigger | dropView
  | renameTable | truncateTable
  ;
 * </pre>
 */
public interface DdlStatement extends SqlStatement {
}