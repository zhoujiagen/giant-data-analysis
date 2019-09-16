package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

/**
 * 关系代数语句表达式:
 * 
 * <pre>
sqlStatement
    : ddlStatement | dmlStatement | transactionStatement
    | replicationStatement | preparedStatement
    | administrationStatement | utilityStatement
    ;
 * </pre>
 */
public interface SqlStatement extends RelationalAlgebraExpression {
}
