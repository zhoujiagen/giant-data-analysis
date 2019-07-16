package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
utilityStatement
    : simpleDescribeStatement | fullDescribeStatement
    | helpStatement | useStatement
    ;
 * </pre>
 */
public interface UtilityStatement extends SqlStatement {

  /**
   * <pre>
   describeObjectClause
    : (
        selectStatement | deleteStatement | insertStatement
        | replaceStatement | updateStatement
      )                                                             #describeStatements
    | FOR CONNECTION uid                                            #describeConnection
    ;
   * </pre>
   */
  public static class DescribeObjectClause implements PrimitiveExpression {

  }

}