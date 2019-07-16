package com.spike.giantdataanalysis.model.logic.relational.expression;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.CompoundStatement.RoutineBody;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.FullId;

/**
 * <pre>
 alterEvent
    : ALTER ownerStatement?
      EVENT fullId
      (ON SCHEDULE scheduleExpression)?
      (ON COMPLETION NOT? PRESERVE)?
      (RENAME TO fullId)? enableType?
      (COMMENT STRING_LITERAL)?
      (DO routineBody)?
    ;
 * </pre>
 */
public class AlterEvent implements DdlStatement {
  public final OwnerStatement ownerStatement;
  public final FullId fullId;
  public final ScheduleExpression scheduleExpression;
  public final Boolean notPreserve;
  public final FullId renameToFullId;
  public final EnableTypeEnum enableType;
  public final String comment;
  public final RoutineBody routineBody;

  AlterEvent(OwnerStatement ownerStatement, FullId fullId, ScheduleExpression scheduleExpression,
      Boolean notPreserve, FullId renameToFullId, EnableTypeEnum enableType, String comment,
      RoutineBody routineBody) {
    Preconditions.checkArgument(fullId != null);

    this.ownerStatement = ownerStatement;
    this.fullId = fullId;
    this.scheduleExpression = scheduleExpression;
    this.notPreserve = notPreserve;
    this.renameToFullId = renameToFullId;
    this.enableType = enableType;
    this.comment = comment;
    this.routineBody = routineBody;
  }

}
