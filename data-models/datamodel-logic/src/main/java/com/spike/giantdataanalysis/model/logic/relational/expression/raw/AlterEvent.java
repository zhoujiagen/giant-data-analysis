package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CompoundStatement.RoutineBody;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.FullId;

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

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("ALTER ");
    if (ownerStatement != null) {
      sb.append(ownerStatement.literal()).append(" ");
    }
    sb.append("EVENT ").append(fullId.literal()).append(" ");
    if (scheduleExpression != null) {
      sb.append("ON SCHEDULE ").append(scheduleExpression.literal()).append(" ");
    }
    if (Boolean.TRUE.equals(notPreserve)) {
      sb.append("ON COMPLETION NOT PRESERVE ");
    } else if (Boolean.FALSE.equals(notPreserve)) {
      sb.append("ON COMPLETION PRESERVE ");
    }

    if (fullId != null) {
      sb.append("RENAME TO ").append(fullId.literal()).append(" ");
    }
    if (enableType != null) {
      sb.append(enableType.literal()).append(" ");
    }
    if (comment != null) {
      sb.append("COMMENT ").append(comment).append(" ");
    }
    if (routineBody != null) {
      sb.append("DO ").append(routineBody.literal());
    }

    return sb.toString();
  }
}
