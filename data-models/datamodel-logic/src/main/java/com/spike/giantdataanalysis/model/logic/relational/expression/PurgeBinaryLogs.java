package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
 purgeBinaryLogs
    : PURGE purgeFormat=(BINARY | MASTER) LOGS
       (
           TO fileName=STRING_LITERAL
           | BEFORE timeValue=STRING_LITERAL
       )
    ;
 * </pre>
 */
public class PurgeBinaryLogs implements ReplicationStatement {
}
