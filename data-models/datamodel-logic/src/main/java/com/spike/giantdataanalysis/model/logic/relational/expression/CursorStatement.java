package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
 cursorStatement
    : CLOSE uid                                                     #CloseCursor
    | FETCH (NEXT? FROM)? uid INTO uidList                          #FetchCursor
    | OPEN uid                                                      #OpenCursor
    ;
 * </pre>
 */
public class CursorStatement implements CompoundStatement {
}