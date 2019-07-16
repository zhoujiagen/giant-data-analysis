package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
 changeMaster
    : CHANGE MASTER TO
      masterOption (',' masterOption)* channelOption?
    ;
 * </pre>
 */
public class ChangeMaster implements ReplicationStatement {
}
