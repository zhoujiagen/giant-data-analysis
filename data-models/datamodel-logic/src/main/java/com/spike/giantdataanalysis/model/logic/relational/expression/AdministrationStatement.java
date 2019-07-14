package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
administrationStatement
  : alterUser | createUser | dropUser | grantStatement
  | grantProxy | renameUser | revokeStatement
  | revokeProxy | analyzeTable | checkTable
  | checksumTable | optimizeTable | repairTable
  | createUdfunction | installPlugin | uninstallPlugin
  | setStatement | showStatement | binlogStatement
  | cacheIndexStatement | flushStatement | killStatement
  | loadIndexIntoCache | resetStatement
  | shutdownStatement
  ;
 * </pre>
 */
public interface AdministrationStatement extends SqlStatement {
}