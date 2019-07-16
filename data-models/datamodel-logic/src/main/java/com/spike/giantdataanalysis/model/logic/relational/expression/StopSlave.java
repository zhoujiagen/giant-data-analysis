package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
 stopSlave
    : STOP SLAVE (threadType (',' threadType)*)?
    ;
 * </pre>
 */
public class StopSlave implements ReplicationStatement {
}
