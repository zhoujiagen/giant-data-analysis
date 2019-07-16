package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
 createTablespaceInnodb
    : CREATE TABLESPACE uid
      ADD DATAFILE datafile=STRING_LITERAL
      (FILE_BLOCK_SIZE '=' fileBlockSize=fileSizeLiteral)?
      (ENGINE '='? engineName)?
    ;
 * </pre>
 */
public class CreateTablespaceInnodb implements DdlStatement {
}
