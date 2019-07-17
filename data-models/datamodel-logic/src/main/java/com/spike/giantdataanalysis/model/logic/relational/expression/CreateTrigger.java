package com.spike.giantdataanalysis.model.logic.relational.expression;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.CompoundStatement.RoutineBody;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.FullId;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.TableName;

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
    BEFORE, AFTER
  }

  public static enum TriggerEventEnum implements RelationalAlgebraEnum {
    INSERT, UPDATE, DELETE
  }

  public static enum TriggerPlaceEnum implements RelationalAlgebraEnum {
    FOLLOWS, PRECEDES
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

}
