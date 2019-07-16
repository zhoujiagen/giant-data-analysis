package com.spike.giantdataanalysis.model.logic.relational.expression;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonExpressons.IfNotExists;
import com.spike.giantdataanalysis.model.logic.relational.expression.CompoundStatement.RoutineBody;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.FullId;

/**
 * <pre>
 createEvent
    : CREATE ownerStatement? EVENT ifNotExists? fullId
      ON SCHEDULE scheduleExpression
      (ON COMPLETION NOT? PRESERVE)? enableType?
      (COMMENT STRING_LITERAL)?
      DO routineBody
    ;
 * </pre>
 */
public class CreateEvent implements DdlStatement {
  public final OwnerStatement onOwnerStatement;
  public final IfNotExists ifNotExists;
  public final FullId fullId;
  public final ScheduleExpression scheduleExpression;
  public final Boolean onCompletion;
  public final Boolean notPreserve;
  public final EnableTypeEnum enableType;
  public final String comment;
  public final RoutineBody routineBody;

  CreateEvent(OwnerStatement onOwnerStatement, IfNotExists ifNotExists, FullId fullId,
      ScheduleExpression scheduleExpression, Boolean onCompletion, Boolean notPreserve,
      EnableTypeEnum enableType, String comment, RoutineBody routineBody) {
    Preconditions.checkArgument(fullId != null);
    Preconditions.checkArgument(scheduleExpression != null);
    Preconditions.checkArgument(routineBody != null);

    this.onOwnerStatement = onOwnerStatement;
    this.ifNotExists = ifNotExists;
    this.fullId = fullId;
    this.scheduleExpression = scheduleExpression;
    this.onCompletion = onCompletion;
    this.notPreserve = notPreserve;
    this.enableType = enableType;
    this.comment = comment;
    this.routineBody = routineBody;
  }

}
