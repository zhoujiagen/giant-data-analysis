package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
 setStatement
    : SET variableClause ('=' | ':=') expression
      (',' variableClause ('=' | ':=') expression)*                 #setVariable
    | SET (CHARACTER SET | CHARSET) (charsetName | DEFAULT)         #setCharset
    | SET NAMES
        (charsetName (COLLATE collationName)? | DEFAULT)            #setNames
    | setPasswordStatement                                          #setPassword
    | setTransactionStatement                                       #setTransaction
    | setAutocommitStatement                                        #setAutocommit
    | SET fullId ('=' | ':=') expression                            #setNewValueInsideTrigger
    ;
 * </pre>
 */
public interface SetStatement extends AdministrationStatement {

  /**
   * <pre>
  setPasswordStatement
    : SET PASSWORD (FOR userName)?
      '=' ( passwordFunctionClause | STRING_LITERAL)
    ;
   * </pre>
   */
  public static class SetPasswordStatement implements SetStatement {

  }
}
