package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
 startSlave
    : START SLAVE (threadType (',' threadType)*)?
      (UNTIL untilOption)?
      connectionOption* channelOption?
    ;
 * </pre>
 */
public class StartSlave implements ReplicationStatement {
}
