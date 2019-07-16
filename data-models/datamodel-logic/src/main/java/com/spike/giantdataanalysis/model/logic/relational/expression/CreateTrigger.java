package com.spike.giantdataanalysis.model.logic.relational.expression;

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
}
