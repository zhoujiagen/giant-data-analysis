package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
 createTable
    : CREATE TEMPORARY? TABLE ifNotExists?
       tableName
       (
         LIKE tableName
         | '(' LIKE parenthesisTable=tableName ')'
       )                                                            #copyCreateTable
    | CREATE TEMPORARY? TABLE ifNotExists?
       tableName createDefinitions?
       ( tableOption (','? tableOption)* )?
       partitionDefinitions? keyViolate=(IGNORE | REPLACE)?
       AS? selectStatement                                          #queryCreateTable
    | CREATE TEMPORARY? TABLE ifNotExists?
       tableName createDefinitions
       ( tableOption (','? tableOption)* )?
       partitionDefinitions?                                        #columnCreateTable
    ;
 * </pre>
 */
public class CreateTable implements DdlStatement {
}
