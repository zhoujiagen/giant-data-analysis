package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
 grantProxy
    : GRANT PROXY ON fromFirst=userName
      TO toFirst=userName (',' toOther+=userName)*
      (WITH GRANT OPTION)?
    ;
 * </pre>
 */
public class GrantProxy implements AdministrationStatement {
}
