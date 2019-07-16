package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
 revokeProxy
    : REVOKE PROXY ON onUser=userName
      FROM fromFirst=userName (',' fromOther+=userName)*
    ;
 * </pre>
 */
public class RevokeProxy implements AdministrationStatement {
}
