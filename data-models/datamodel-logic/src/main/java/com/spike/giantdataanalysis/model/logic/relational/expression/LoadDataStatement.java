package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonLists.UidList;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.CharsetName;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.TableName;
import com.spike.giantdataanalysis.model.logic.relational.expression.Literals.DecimalLiteral;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.SelectFieldsInto;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.SelectLinesInto;

/**
 * <pre>
loadDataStatement
  : LOAD DATA
    priority=(LOW_PRIORITY | CONCURRENT)?
    LOCAL? INFILE filename=STRING_LITERAL
    violation=(REPLACE | IGNORE)?
    INTO TABLE tableName
    (PARTITION '(' uidList ')' )?
    (CHARACTER SET charset=charsetName)?
    (
      fieldsFormat=(FIELDS | COLUMNS)
      selectFieldsInto+
    )?
    (
      LINES
        selectLinesInto+
    )?
    (
      IGNORE decimalLiteral linesFormat=(LINES | ROWS)
    )?
    ( '(' assignmentField (',' assignmentField)* ')' )?
    (SET updatedElement (',' updatedElement)*)?
  ;
 * </pre>
 */
public class LoadDataStatement implements DmlStatement {

  public final PriorityEnum priority;
  public final Boolean local;
  public final String filename;
  public final ViolationEnum violation;
  public final TableName tableName;
  public final UidList uidList;
  public final CharsetName charsetName;
  public final FieldsFormatEnum fieldsFormat;
  public final List<SelectFieldsInto> selectFieldsIntos;
  public final List<SelectLinesInto> selectLinesIntos;
  public final DecimalLiteral decimalLiteral;
  public final LinesFormatEnum linesFormat;
  public final List<AssignmentField> assignmentFields;
  public final List<UpdatedElement> updatedElements;

  LoadDataStatement(PriorityEnum priority, Boolean local, String filename, ViolationEnum violation,
      TableName tableName, UidList uidList, CharsetName charsetName, FieldsFormatEnum fieldsFormat,
      List<SelectFieldsInto> selectFieldsIntos, List<SelectLinesInto> selectLinesIntos,
      DecimalLiteral decimalLiteral, LinesFormatEnum linesFormat,
      List<AssignmentField> assignmentFields, List<UpdatedElement> updatedElements) {
    Preconditions.checkArgument(filename != null);
    Preconditions.checkArgument(tableName != null);

    this.priority = priority;
    this.local = local;
    this.filename = filename;
    this.violation = violation;
    this.tableName = tableName;
    this.uidList = uidList;
    this.charsetName = charsetName;
    this.fieldsFormat = fieldsFormat;
    this.selectFieldsIntos = selectFieldsIntos;
    this.selectLinesIntos = selectLinesIntos;
    this.decimalLiteral = decimalLiteral;
    this.linesFormat = linesFormat;
    this.assignmentFields = assignmentFields;
    this.updatedElements = updatedElements;
  }

}