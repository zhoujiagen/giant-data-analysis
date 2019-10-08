package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonLists.UidList;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.CharsetName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.TableName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Literals.DecimalLiteral;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SelectStatement.SelectFieldsInto;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SelectStatement.SelectLinesInto;

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

  public final DmlStatement.PriorityEnum priority;
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

  LoadDataStatement(DmlStatement.PriorityEnum priority, Boolean local, String filename,
      ViolationEnum violation, TableName tableName, UidList uidList, CharsetName charsetName,
      FieldsFormatEnum fieldsFormat, List<SelectFieldsInto> selectFieldsIntos,
      List<SelectLinesInto> selectLinesIntos, DecimalLiteral decimalLiteral,
      LinesFormatEnum linesFormat, List<AssignmentField> assignmentFields,
      List<UpdatedElement> updatedElements) {
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

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("LOAD DATA ");
    if (priority != null) {
      sb.append(priority.literal()).append(" ");
    }
    if (Boolean.TRUE.equals(local)) {
      sb.append("LOCAL ");
    }
    sb.append("INFILE ").append(filename).append(" ");
    if (violation != null) {
      sb.append(violation.literal()).append(" ");
    }
    sb.append("INTO TABLE ").append(tableName.literal()).append(" ");
    if (uidList != null) {
      sb.append("PARTITION(").append(uidList.literal()).append(") ");
    }
    if (charsetName != null) {
      sb.append("CHARACTER SET ").append(charsetName.literal()).append(" ");
    }
    if (fieldsFormat != null) {
      sb.append(fieldsFormat.literal()).append(" ");
      List<String> literals = Lists.newArrayList();
      for (SelectFieldsInto selectFieldsInto : selectFieldsIntos) {
        literals.add(selectFieldsInto.literal());
      }
      sb.append(Joiner.on(" ").join(literals));
    }
    if (CollectionUtils.isNotEmpty(selectLinesIntos)) {
      sb.append("LINES ");
      List<String> literals = Lists.newArrayList();
      for (SelectLinesInto selectLinesInto : selectLinesIntos) {
        literals.add(selectLinesInto.literal());
      }
      sb.append(Joiner.on(" ").join(literals));
    }
    if (decimalLiteral != null) {
      sb.append("IGNORE ").append(decimalLiteral.literal()).append(linesFormat.literal())
          .append(" ");
    }
    if (CollectionUtils.isNotEmpty(assignmentFields)) {
      sb.append("(");
      List<String> literals = Lists.newArrayList();
      for (AssignmentField assignmentField : assignmentFields) {
        literals.add(assignmentField.literal());
      }
      sb.append(Joiner.on(" ").join(literals));
      sb.append(")");
    }
    if (CollectionUtils.isNotEmpty(updatedElements)) {
      List<String> literals = Lists.newArrayList();
      for (UpdatedElement updatedElement : updatedElements) {
        literals.add(updatedElement.literal());
      }
      sb.append(Joiner.on(" ").join(literals));
    }

    return sb.toString();
  }
}