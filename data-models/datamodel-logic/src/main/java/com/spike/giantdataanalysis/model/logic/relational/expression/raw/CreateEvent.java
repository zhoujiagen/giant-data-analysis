package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonExpressons.IfNotExists;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CompoundStatement.RoutineBody;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.FullId;

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
  public final OwnerStatement ownerStatement;
  public final IfNotExists ifNotExists;
  public final FullId fullId;
  public final ScheduleExpression scheduleExpression;
  public final Boolean onCompletion;
  public final Boolean notPreserve;
  public final EnableTypeEnum enableType;
  public final String comment;
  public final RoutineBody routineBody;

  CreateEvent(OwnerStatement ownerStatement, IfNotExists ifNotExists, FullId fullId,
      ScheduleExpression scheduleExpression, Boolean onCompletion, Boolean notPreserve,
      EnableTypeEnum enableType, String comment, RoutineBody routineBody) {
    Preconditions.checkArgument(fullId != null);
    Preconditions.checkArgument(scheduleExpression != null);
    Preconditions.checkArgument(routineBody != null);

    this.ownerStatement = ownerStatement;
    this.ifNotExists = ifNotExists;
    this.fullId = fullId;
    this.scheduleExpression = scheduleExpression;
    this.onCompletion = onCompletion;
    this.notPreserve = notPreserve;
    this.enableType = enableType;
    this.comment = comment;
    this.routineBody = routineBody;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("CREATE ");
    if (ownerStatement != null) {
      sb.append(ownerStatement.literal()).append(" ");
    }
    sb.append("EVENT ");
    if (ifNotExists != null) {
      sb.append(ifNotExists.literal()).append(" ");
    }
    sb.append(fullId.literal()).append(" ");
    sb.append("ON SCHEDULE ").append(scheduleExpression.literal()).append(" ");
    if (Boolean.TRUE.equals(notPreserve)) {
      sb.append("ON COMPLETION NOT? PRESERVE ");
    } else if (Boolean.FALSE.equals(notPreserve)) {
      sb.append("ON COMPLETION PRESERVE ");
    }
    if (enableType != null) {
      sb.append(enableType.literal()).append(" ");
    }
    if (comment != null) {
      sb.append("COMMENT ").append(comment).append(" ");
    }
    sb.append("DO ").append(routineBody.literal());
    return sb.toString();
  }
}
