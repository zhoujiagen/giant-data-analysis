package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CompoundStatement.RoutineBody;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.FullId;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.TableName;

/**
 * <pre>
 createTrigger
    : CREATE ownerStatement?
      TRIGGER thisTrigger=fullId
      triggerTime=(BEFORE | AFTER)
      triggerEvent=(INSERT | UPDATE | DELETE)
      ON tableName FOR EACH ROW
      (triggerPlace=(FOLLOWS | PRECEDES) otherTrigger=fullId)?
      routineBody
    ;
 * </pre>
 */
public class CreateTrigger implements DdlStatement {
  public static enum TriggerTimeEnum implements RelationalAlgebraEnum {
    BEFORE, AFTER;
    @Override
    public String literal() {
      return name();
    }
  }

  public static enum TriggerEventEnum implements RelationalAlgebraEnum {
    INSERT, UPDATE, DELETE;
    @Override
    public String literal() {
      return name();
    }
  }

  public static enum TriggerPlaceEnum implements RelationalAlgebraEnum {
    FOLLOWS, PRECEDES;

    @Override
    public String literal() {
      return name();
    }
  }

  public final OwnerStatement ownerStatement;
  public final FullId thisTrigger;
  public final TriggerTimeEnum triggerTime;
  public final TriggerEventEnum triggerEvent;
  public final TableName tableName;
  public final TriggerPlaceEnum triggerPlace;
  public final FullId otherTrigger;
  public final RoutineBody routineBody;

  CreateTrigger(OwnerStatement ownerStatement, FullId thisTrigger, TriggerTimeEnum triggerTime,
      TriggerEventEnum triggerEvent, TableName tableName, TriggerPlaceEnum triggerPlace,
      FullId otherTrigger, RoutineBody routineBody) {
    Preconditions.checkArgument(thisTrigger != null);
    Preconditions.checkArgument(triggerTime != null);
    Preconditions.checkArgument(triggerEvent != null);
    Preconditions.checkArgument(tableName != null);
    Preconditions.checkArgument(routineBody != null);

    this.ownerStatement = ownerStatement;
    this.thisTrigger = thisTrigger;
    this.triggerTime = triggerTime;
    this.triggerEvent = triggerEvent;
    this.tableName = tableName;
    this.triggerPlace = triggerPlace;
    this.otherTrigger = otherTrigger;
    this.routineBody = routineBody;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("CREATE ").append(" ");
    if (ownerStatement != null) {
      sb.append("TRIGGER ").append(thisTrigger.literal()).append(" ");
    }
    sb.append(triggerTime.literal()).append(" ");
    sb.append(triggerEvent.literal()).append(" ");
    sb.append("ON ").append(tableName.literal()).append(" FOR EACH ROW");
    if (otherTrigger != null) {
      sb.append(triggerPlace.literal()).append(" ").append(otherTrigger.literal()).append(" ");
    }
    sb.append(routineBody.literal()).append(" ");

    return sb.toString();
  }
}
