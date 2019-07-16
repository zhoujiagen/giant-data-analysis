package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
 xaStartTransaction
    : XA xaStart=(START | BEGIN) xid xaAction=(JOIN | RESUME)?
    ;
 * </pre>
 */
public class XaStartTransaction implements ReplicationStatement {
}
