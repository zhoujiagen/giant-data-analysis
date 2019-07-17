package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.CharsetName;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.TableName;
import com.spike.giantdataanalysis.model.logic.relational.expression.Literals.DecimalLiteral;

/**
 * <pre>
loadXmlStatement
  : LOAD XML
    priority=(LOW_PRIORITY | CONCURRENT)?
    LOCAL? INFILE filename=STRING_LITERAL
    violation=(REPLACE | IGNORE)?
    INTO TABLE tableName
    (CHARACTER SET charset=charsetName)?
    (ROWS IDENTIFIED BY '<' tag=STRING_LITERAL '>')?
    ( IGNORE decimalLiteral linesFormat=(LINES | ROWS) )?
    ( '(' assignmentField (',' assignmentField)* ')' )?
    (SET updatedElement (',' updatedElement)*)?
  ;
 * </pre>
 */
public class LoadXmlStatement implements DmlStatement {
  public final PriorityEnum priority;
  public final Boolean local;
  public final String filename;
  public final ViolationEnum violation;
  public final TableName tableName;
  public final CharsetName charsetName;
  public final String tag;
  public final DecimalLiteral decimalLiteral;
  public final LinesFormatEnum linesFormat;
  public final List<AssignmentField> assignmentFields;
  public final List<UpdatedElement> updatedElements;

  LoadXmlStatement(PriorityEnum priority, Boolean local, String filename, ViolationEnum violation,
      TableName tableName, CharsetName charsetName, String tag, DecimalLiteral decimalLiteral,
      LinesFormatEnum linesFormat, List<AssignmentField> assignmentFields,
      List<UpdatedElement> updatedElements) {
    Preconditions.checkArgument(filename != null);
    Preconditions.checkArgument(tableName != null);

    this.priority = priority;
    this.local = local;
    this.filename = filename;
    this.violation = violation;
    this.tableName = tableName;
    this.charsetName = charsetName;
    this.tag = tag;
    this.decimalLiteral = decimalLiteral;
    this.linesFormat = linesFormat;
    this.assignmentFields = assignmentFields;
    this.updatedElements = updatedElements;
  }

}