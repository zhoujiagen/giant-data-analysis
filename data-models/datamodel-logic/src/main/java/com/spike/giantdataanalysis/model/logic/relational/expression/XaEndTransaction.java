package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
 xaEndTransaction
    : XA END xid (SUSPEND (FOR MIGRATE)?)?
    ;
 * </pre>
 */
public class XaEndTransaction implements ReplicationStatement {
}
