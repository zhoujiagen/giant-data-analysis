package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.CharsetName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.TableName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Literals.DecimalLiteral;

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

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("LOAD XML ");
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
    if (charsetName != null) {
      sb.append("CHARACTER SET ").append(charsetName.literal()).append(" ");
    }
    if (tag != null) {
      sb.append("ROWS IDENTIFIED BY <").append(tag).append("> ");
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
      sb.append(Joiner.on(", ").join(literals));
      sb.append(")");
    }

    if (CollectionUtils.isNotEmpty(updatedElements)) {
      sb.append("SET ");
      List<String> literals = Lists.newArrayList();
      for (UpdatedElement updatedElement : updatedElements) {
        literals.add(updatedElement.literal());
      }
      sb.append(Joiner.on(", ").join(literals));
    }
    return sb.toString();
  }
}