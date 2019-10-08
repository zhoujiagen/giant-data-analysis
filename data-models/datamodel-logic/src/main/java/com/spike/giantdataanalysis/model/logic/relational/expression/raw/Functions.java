package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.CharsetName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.FullColumnName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.FullId;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DataType.ConvertedDataType;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DdlStatement.IntervalType;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DmlStatement.OrderByExpression;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Literals.Constant;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Literals.DecimalLiteral;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Literals.StringLiteral;

/**
 * Functions
 */
public interface Functions extends PrimitiveExpression {
  /**
   * <pre>
   functionCall
    : specificFunction                                              #specificFunctionCall
    | aggregateWindowedFunction                                     #aggregateFunctionCall
    | scalarFunctionName '(' functionArgs? ')'                      #scalarFunctionCall
    | fullId '(' functionArgs? ')'                                  #udfFunctionCall
    | passwordFunctionClause                                        #passwordFunctionCall
    ;
   * </pre>
   */
  public static interface FunctionCall extends ExpressionAtom, Functions {
  }

  /**
   * <pre>
   specificFunction
    : (
      CURRENT_DATE | CURRENT_TIME | CURRENT_TIMESTAMP
      | CURRENT_USER | LOCALTIME
      )                                                             #simpleFunctionCall
    | CONVERT '(' expression separator=',' convertedDataType ')'    #dataTypeFunctionCall
    | CONVERT '(' expression USING charsetName ')'                  #dataTypeFunctionCall
    | CAST '(' expression AS convertedDataType ')'                  #dataTypeFunctionCall
    | VALUES '(' fullColumnName ')'                                 #valuesFunctionCall
    | CASE expression caseFuncAlternative+
      (ELSE elseArg=functionArg)? END                               #caseFunctionCall
    | CASE caseFuncAlternative+
      (ELSE elseArg=functionArg)? END                               #caseFunctionCall
    | CHAR '(' functionArgs  (USING charsetName)? ')'               #charFunctionCall
    | POSITION
      '('
          (
            positionString=stringLiteral
            | positionExpression=expression
          )
          IN
          (
            inString=stringLiteral
            | inExpression=expression
          )
      ')'                                                           #positionFunctionCall
    | (SUBSTR | SUBSTRING)
      '('
        (
          sourceString=stringLiteral
          | sourceExpression=expression
        ) FROM
        (
          fromDecimal=decimalLiteral
          | fromExpression=expression
        )
        (
          FOR
          (
            forDecimal=decimalLiteral
            | forExpression=expression
          )
        )?
      ')'                                                           #substrFunctionCall
    | TRIM
      '('
        positioinForm=(BOTH | LEADING | TRAILING)
        (
          sourceString=stringLiteral
          | sourceExpression=expression
        )?
        FROM
        (
          fromString=stringLiteral
          | fromExpression=expression
        )
      ')'                                                           #trimFunctionCall
    | TRIM
      '('
        (
          sourceString=stringLiteral
          | sourceExpression=expression
        )
        FROM
        (
          fromString=stringLiteral
          | fromExpression=expression
        )
      ')'                                                           #trimFunctionCall
    | WEIGHT_STRING
      '('
        (stringLiteral | expression)
        (AS stringFormat=(CHAR | BINARY)
        '(' decimalLiteral ')' )?  levelsInWeightString?
      ')'                                                           #weightFunctionCall
    | EXTRACT
      '('
        intervalType
        FROM
        (
          sourceString=stringLiteral
          | sourceExpression=expression
        )
      ')'                                                           #extractFunctionCall
    | GET_FORMAT
      '('
        datetimeFormat=(DATE | TIME | DATETIME)
        ',' stringLiteral
      ')'                                                           #getFormatFunctionCall
    ;
   * </pre>
   */
  public static interface SpecificFunction extends FunctionCall {
  }

  public static class SimpleFunctionCall implements SpecificFunction {
    public static enum Type implements RelationalAlgebraEnum {
      CURRENT_DATE, CURRENT_TIME, CURRENT_TIMESTAMP, CURRENT_USER, LOCALTIME;

      @Override
      public String literal() {
        return name();
      }
    }

    public final SimpleFunctionCall.Type type;

    SimpleFunctionCall(SimpleFunctionCall.Type type) {
      this.type = type;
    }

    @Override
    public String literal() {
      return type.literal();
    }

  }

  public static class DataTypeFunctionCall implements SpecificFunction {
    public static enum Type implements RelationalAlgebraEnum {
      // CONVERT '(' expression separator=',' convertedDataType ')' #dataTypeFunctionCall
      CONVERT_DATATYPE,
      // CONVERT '(' expression USING charsetName ')' #dataTypeFunctionCall
      CONVERT_CHARSET,
      // CAST '(' expression AS convertedDataType ')' #dataTypeFunctionCall
      CAST;

      @Override
      public String literal() {
        return name();
      }
    }

    public final DataTypeFunctionCall.Type type;
    public final Expression expression;
    public final ConvertedDataType convertedDataType;
    public final CharsetName charsetName;

    DataTypeFunctionCall(DataTypeFunctionCall.Type type, Expression expression,
        CharsetName charsetName) {
      Preconditions.checkArgument(Type.CONVERT_CHARSET.equals(type));
      Preconditions.checkArgument(expression != null);
      Preconditions.checkArgument(charsetName != null);

      this.type = type;
      this.expression = expression;
      this.convertedDataType = null;
      this.charsetName = charsetName;
    }

    DataTypeFunctionCall(DataTypeFunctionCall.Type type, Expression expression,
        ConvertedDataType convertedDataType) {
      Preconditions.checkArgument(Type.CONVERT_DATATYPE.equals(type) || Type.CAST.equals(type));
      Preconditions.checkArgument(expression != null);
      Preconditions.checkArgument(convertedDataType != null);

      this.type = type;
      this.expression = expression;
      this.convertedDataType = convertedDataType;
      this.charsetName = null;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();

      if (Type.CONVERT_DATATYPE.equals(type)) {
        sb.append("CONVERT(");
        sb.append(expression.literal());
        sb.append(", ");
        sb.append(convertedDataType.literal());
      }

      if (Type.CONVERT_CHARSET.equals(type)) {
        sb.append("CONVERT(");
        sb.append(expression.literal());
        sb.append(" USING ");
        sb.append(charsetName.literal());
      }

      if (Type.CAST.equals(type)) {
        sb.append("CAST(");
        sb.append(expression.literal());
        sb.append(" AS ");
        sb.append(convertedDataType.literal());
      }

      sb.append(")");
      return sb.toString();
    }

  }

  public static class ValuesFunctionCall implements SpecificFunction {
    public final FullColumnName fullColumnName;

    ValuesFunctionCall(FullColumnName fullColumnName) {
      Preconditions.checkArgument(fullColumnName != null);

      this.fullColumnName = fullColumnName;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("VALUES(");
      sb.append(fullColumnName.literal());
      sb.append(")");
      return sb.toString();
    }

  }

  public static class CaseFunctionCall implements SpecificFunction {
    public final Expression expression;
    public final List<CaseFuncAlternative> caseFuncAlternatives;
    public final FunctionArg functionArg;

    CaseFunctionCall(Expression expression, List<CaseFuncAlternative> caseFuncAlternatives,
        FunctionArg functionArg) {
      Preconditions.checkArgument(expression != null);
      Preconditions.checkArgument(caseFuncAlternatives != null && caseFuncAlternatives.size() > 0);

      this.expression = expression;
      this.caseFuncAlternatives = caseFuncAlternatives;
      this.functionArg = functionArg;
    }

    CaseFunctionCall(List<CaseFuncAlternative> caseFuncAlternatives, FunctionArg functionArg) {
      Preconditions.checkArgument(caseFuncAlternatives != null && caseFuncAlternatives.size() > 0);

      this.expression = null;
      this.caseFuncAlternatives = caseFuncAlternatives;
      this.functionArg = functionArg;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("CASE ");
      if (expression != null) {
        sb.append(expression.literal()).append(" ");
      }
      List<String> literals = Lists.newArrayList();
      for (CaseFuncAlternative caseFuncAlternative : caseFuncAlternatives) {
        literals.add(caseFuncAlternative.literal());
      }
      sb.append(Joiner.on(" ").join(literals)).append(" ");
      if (functionArg != null) {
        sb.append("ELSE ").append(functionArg.literal()).append(" ");
      }

      sb.append("END");
      return sb.toString();
    }

  }

  public static class CharFunctionCall implements SpecificFunction {
    public final FunctionArgs functionArgs;
    public final CharsetName charsetName;

    CharFunctionCall(FunctionArgs functionArgs, CharsetName charsetName) {
      Preconditions.checkArgument(functionArgs != null);

      this.functionArgs = functionArgs;
      this.charsetName = charsetName;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("CHAR(");
      sb.append(functionArgs.literal());
      if (charsetName != null) {
        sb.append(" USING ").append(charsetName.literal());
      }
      sb.append(")");
      return sb.toString();

    }

  }

  public static class PositionFunctionCall implements SpecificFunction {
    public final StringLiteral positionString;
    public final Expression positionExpression;
    public final StringLiteral inString;
    public final Expression inExpression;

    PositionFunctionCall(StringLiteral positionString, Expression positionExpression,
        StringLiteral inString, Expression inExpression) {
      Preconditions.checkArgument(!(positionString == null && positionExpression == null));
      Preconditions.checkArgument(!(inString == null && inExpression == null));

      this.positionString = positionString;
      this.positionExpression = positionExpression;
      this.inString = inString;
      this.inExpression = inExpression;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("POSITION(");
      if (positionString != null) {
        sb.append(positionString.literal());
      } else {
        sb.append(positionExpression.literal());
      }
      sb.append(" IN ");
      if (inString != null) {
        sb.append(inString.literal());
      } else {
        sb.append(inExpression.literal());
      }
      sb.append(")");
      return sb.toString();

    }

  }

  public static class SubstrFunctionCall implements SpecificFunction {
    public final StringLiteral sourceString;
    public final Expression sourceExpression;
    public final DecimalLiteral fromDecimal;
    public final Expression fromExpression;
    public final DecimalLiteral forDecimal;
    public final Expression forExpression;

    SubstrFunctionCall(//
        StringLiteral sourceString, Expression sourceExpression, //
        DecimalLiteral fromDecimal, Expression fromExpression, //
        DecimalLiteral forDecimal, Expression forExpression//
    ) {
      Preconditions.checkArgument(!(sourceString == null && sourceExpression == null));
      Preconditions.checkArgument(!(fromDecimal == null && fromExpression == null));

      this.sourceString = sourceString;
      this.sourceExpression = sourceExpression;
      this.fromDecimal = fromDecimal;
      this.fromExpression = fromExpression;
      this.forDecimal = forDecimal;
      this.forExpression = forExpression;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("SUBSTR(");
      if (sourceString != null) {
        sb.append(sourceString.literal());
      } else {
        sb.append(sourceExpression.literal());
      }

      sb.append(" FROM ");
      if (fromDecimal != null) {
        sb.append(fromDecimal.literal());
      } else {
        sb.append(fromExpression.literal());
      }

      if (forDecimal != null || forExpression != null) {
        sb.append(" FOR ");
        if (forDecimal != null) {
          sb.append(forDecimal.literal());
        } else {
          sb.append(forExpression.literal());
        }
      }

      sb.append(")");
      return sb.toString();

    }

  }

  public static class TrimFunctionCall implements SpecificFunction {
    public static enum PositioinFormType implements RelationalAlgebraEnum {
      BOTH, LEADING, TRAILING;
      @Override
      public String literal() {
        return name();
      }
    }

    public final TrimFunctionCall.PositioinFormType positioinForm;
    public final StringLiteral sourceString;
    public final Expression sourceExpression;
    public final StringLiteral fromString;
    public final Expression fromExpression;

    TrimFunctionCall(TrimFunctionCall.PositioinFormType positioinForm, //
        StringLiteral sourceString, Expression sourceExpression, //
        StringLiteral fromString, Expression fromExpression//
    ) {
      Preconditions.checkArgument(!(sourceString == null && sourceExpression == null));
      Preconditions.checkArgument(!(fromString == null && fromExpression == null));

      this.positioinForm = positioinForm;
      this.sourceString = sourceString;
      this.sourceExpression = sourceExpression;
      this.fromString = fromString;
      this.fromExpression = fromExpression;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("TRIM(");
      if (positioinForm == null) {

        if (sourceString != null) {
          sb.append(sourceString.literal());
        } else {
          sb.append(sourceExpression.literal());
        }
      } else {
        sb.append(positioinForm.literal()).append(" ");
        if (sourceString != null) {
          sb.append(sourceString.literal());
        } else if (sourceExpression != null) {
          sb.append(sourceExpression.literal());
        }
      }

      sb.append(" FROM ");
      if (fromString != null) {
        sb.append(fromString.literal());
      } else {
        sb.append(fromExpression.literal());
      }
      sb.append(")");
      return sb.toString();

    }

  }

  public static class WeightFunctionCall implements SpecificFunction {
    public static enum StringFormatType implements RelationalAlgebraEnum {
      CHAR, BINARY;
      @Override
      public String literal() {
        return name();
      }
    };

    public final StringLiteral stringLiteral;
    public final Expression expression;
    public final WeightFunctionCall.StringFormatType stringFormat;
    public final DecimalLiteral decimalLiteral;
    public final LevelsInWeightString levelsInWeightString;

    WeightFunctionCall(StringLiteral stringLiteral, Expression expression,
        WeightFunctionCall.StringFormatType stringFormat, DecimalLiteral decimalLiteral,
        LevelsInWeightString levelsInWeightString) {
      Preconditions.checkArgument(!(stringLiteral == null && expression == null));
      if (stringFormat != null) {
        Preconditions.checkArgument(decimalLiteral != null);
      }

      this.stringLiteral = stringLiteral;
      this.expression = expression;
      this.stringFormat = stringFormat;
      this.decimalLiteral = decimalLiteral;
      this.levelsInWeightString = levelsInWeightString;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("WEIGHT_STRING(");
      if (stringLiteral != null) {
        sb.append(stringLiteral.literal());
      } else {
        sb.append(expression.literal());
      }
      if (stringFormat != null) {
        sb.append("AS ").append(stringFormat.literal());
        sb.append("(");
        sb.append(decimalLiteral.literal());
        sb.append(")");
      }
      if (levelsInWeightString != null) {
        sb.append(levelsInWeightString.literal());
      }
      sb.append(")");
      return sb.toString();

    }

  }

  public static class ExtractFunctionCall implements SpecificFunction {
    public final IntervalType intervalType;
    public final StringLiteral sourceString;
    public final Expression sourceExpression;

    ExtractFunctionCall(IntervalType intervalType, StringLiteral sourceString,
        Expression sourceExpression) {
      Preconditions.checkArgument(intervalType != null);
      Preconditions.checkArgument(!(sourceString == null && sourceExpression == null));

      this.intervalType = intervalType;
      this.sourceString = sourceString;
      this.sourceExpression = sourceExpression;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("EXTRACT(");
      sb.append(intervalType.literal());
      sb.append("FROM ");
      if (sourceString != null) {
        sb.append(sourceString.literal());
      } else {
        sb.append(sourceExpression.literal());
      }
      sb.append(")");
      return sb.toString();
    }
  }

  public static class GetFormatFunctionCall implements SpecificFunction {
    public static enum DatetimeFormatType implements RelationalAlgebraEnum {
      DATE, TIME, DATETIME;
      @Override
      public String literal() {
        return name();
      }
    }

    public final GetFormatFunctionCall.DatetimeFormatType type;
    public final StringLiteral stringLiteral;

    GetFormatFunctionCall(GetFormatFunctionCall.DatetimeFormatType type,
        StringLiteral stringLiteral) {
      Preconditions.checkArgument(type != null);
      Preconditions.checkArgument(stringLiteral != null);

      this.type = type;
      this.stringLiteral = stringLiteral;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("GET_FORMAT(");
      sb.append(type.literal());
      sb.append(", ");
      sb.append(stringLiteral.literal());
      sb.append(")");
      return sb.toString();
    }

  }

  /**
   * <pre>
  caseFuncAlternative
    : WHEN condition=functionArg
      THEN consequent=functionArg
    ;
   * </pre>
   */
  public static class CaseFuncAlternative implements PrimitiveExpression {
    public final FunctionArg condition;
    public final FunctionArg consequent;

    CaseFuncAlternative(FunctionArg condition, FunctionArg consequent) {
      Preconditions.checkArgument(condition != null);
      Preconditions.checkArgument(consequent != null);

      this.condition = condition;
      this.consequent = consequent;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("WHEN ").append(condition.literal());
      sb.append(" THEN ").append(consequent.literal());
      return sb.toString();
    }

  }

  public static class ScalarFunctionCall implements FunctionCall {

    public final ScalarFunctionNameEnum scalarFunctionName;
    public final FunctionArgs functionArgs;

    ScalarFunctionCall(ScalarFunctionNameEnum scalarFunctionName, FunctionArgs functionArgs) {
      Preconditions.checkArgument(scalarFunctionName != null);

      this.scalarFunctionName = scalarFunctionName;
      this.functionArgs = functionArgs;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append(scalarFunctionName.literal());
      sb.append("(");
      if (functionArgs != null) {
        sb.append(functionArgs.literal());
      }
      sb.append(")");
      return sb.toString();
    }

  }

  /**
   * <pre>
  scalarFunctionName
    : functionNameBase
    | ASCII | CURDATE | CURRENT_DATE | CURRENT_TIME
    | CURRENT_TIMESTAMP | CURTIME | DATE_ADD | DATE_SUB
    | IF | INSERT | LOCALTIME | LOCALTIMESTAMP | MID | NOW
    | REPLACE | SUBSTR | SUBSTRING | SYSDATE | TRIM
    | UTC_DATE | UTC_TIME | UTC_TIMESTAMP
    ;
   * </pre>
   */
  public static enum ScalarFunctionNameEnum implements RelationalAlgebraEnum {
    ABS, ACOS, ADDDATE, ADDTIME, AES_DECRYPT, AES_ENCRYPT, AREA, ASBINARY, ASIN, ASTEXT, ASWKB,
    ASWKT, ASYMMETRIC_DECRYPT, ASYMMETRIC_DERIVE, ASYMMETRIC_ENCRYPT, ASYMMETRIC_SIGN,
    ASYMMETRIC_VERIFY, ATAN, ATAN2, BENCHMARK, BIN, BIT_COUNT, BIT_LENGTH, BUFFER, CEIL, CEILING,
    CENTROID, CHARACTER_LENGTH, CHARSET, CHAR_LENGTH, COERCIBILITY, COLLATION, COMPRESS, CONCAT,
    CONCAT_WS, CONNECTION_ID, CONV, CONVERT_TZ, COS, COT, COUNT, CRC32, CREATE_ASYMMETRIC_PRIV_KEY,
    CREATE_ASYMMETRIC_PUB_KEY, CREATE_DH_PARAMETERS, CREATE_DIGEST, CROSSES, DATABASE, DATE,
    DATEDIFF, DATE_FORMAT, DAY, DAYNAME, DAYOFMONTH, DAYOFWEEK, DAYOFYEAR, DECODE, DEGREES,
    DES_DECRYPT, DES_ENCRYPT, DIMENSION, DISJOINT, ELT, ENCODE, ENCRYPT, ENDPOINT, ENVELOPE, EQUALS,
    EXP, EXPORT_SET, EXTERIORRING, EXTRACTVALUE, FIELD, FIND_IN_SET, FLOOR, FORMAT, FOUND_ROWS,
    FROM_BASE64, FROM_DAYS, FROM_UNIXTIME, GEOMCOLLFROMTEXT, GEOMCOLLFROMWKB, GEOMETRYCOLLECTION,
    GEOMETRYCOLLECTIONFROMTEXT, GEOMETRYCOLLECTIONFROMWKB, GEOMETRYFROMTEXT, GEOMETRYFROMWKB,
    GEOMETRYN, GEOMETRYTYPE, GEOMFROMTEXT, GEOMFROMWKB, GET_FORMAT, GET_LOCK, GLENGTH, GREATEST,
    GTID_SUBSET, GTID_SUBTRACT, HEX, HOUR, IFNULL, INET6_ATON, INET6_NTOA, INET_ATON, INET_NTOA,
    INSTR, INTERIORRINGN, INTERSECTS, ISCLOSED, ISEMPTY, ISNULL, ISSIMPLE, IS_FREE_LOCK, IS_IPV4,
    IS_IPV4_COMPAT, IS_IPV4_MAPPED, IS_IPV6, IS_USED_LOCK, LAST_INSERT_ID, LCASE, LEAST, LEFT,
    LENGTH, LINEFROMTEXT, LINEFROMWKB, LINESTRING, LINESTRINGFROMTEXT, LINESTRINGFROMWKB, LN,
    LOAD_FILE, LOCATE, LOG, LOG10, LOG2, LOWER, LPAD, LTRIM, MAKEDATE, MAKETIME, MAKE_SET,
    MASTER_POS_WAIT, MBRCONTAINS, MBRDISJOINT, MBREQUAL, MBRINTERSECTS, MBROVERLAPS, MBRTOUCHES,
    MBRWITHIN, MD5, MICROSECOND, MINUTE, MLINEFROMTEXT, MLINEFROMWKB, MONTH, MONTHNAME,
    MPOINTFROMTEXT, MPOINTFROMWKB, MPOLYFROMTEXT, MPOLYFROMWKB, MULTILINESTRING,
    MULTILINESTRINGFROMTEXT, MULTILINESTRINGFROMWKB, MULTIPOINT, MULTIPOINTFROMTEXT,
    MULTIPOINTFROMWKB, MULTIPOLYGON, MULTIPOLYGONFROMTEXT, MULTIPOLYGONFROMWKB, NAME_CONST, NULLIF,
    NUMGEOMETRIES, NUMINTERIORRINGS, NUMPOINTS, OCT, OCTET_LENGTH, ORD, OVERLAPS, PERIOD_ADD,
    PERIOD_DIFF, PI, POINT, POINTFROMTEXT, POINTFROMWKB, POINTN, POLYFROMTEXT, POLYFROMWKB, POLYGON,
    POLYGONFROMTEXT, POLYGONFROMWKB, POSITION, POW, POWER, QUARTER, QUOTE, RADIANS, RAND,
    RANDOM_BYTES, RELEASE_LOCK, REVERSE, RIGHT, ROUND, ROW_COUNT, RPAD, RTRIM, SECOND, SEC_TO_TIME,
    SESSION_USER, SHA, SHA1, SHA2, SIGN, SIN, SLEEP, SOUNDEX, SQL_THREAD_WAIT_AFTER_GTIDS, SQRT,
    SRID, STARTPOINT, STRCMP, STR_TO_DATE, ST_AREA, ST_ASBINARY, ST_ASTEXT, ST_ASWKB, ST_ASWKT,
    ST_BUFFER, ST_CENTROID, ST_CONTAINS, ST_CROSSES, ST_DIFFERENCE, ST_DIMENSION, ST_DISJOINT,
    ST_DISTANCE, ST_ENDPOINT, ST_ENVELOPE, ST_EQUALS, ST_EXTERIORRING, ST_GEOMCOLLFROMTEXT,
    ST_GEOMCOLLFROMTXT, ST_GEOMCOLLFROMWKB, ST_GEOMETRYCOLLECTIONFROMTEXT,
    ST_GEOMETRYCOLLECTIONFROMWKB, ST_GEOMETRYFROMTEXT, ST_GEOMETRYFROMWKB, ST_GEOMETRYN,
    ST_GEOMETRYTYPE, ST_GEOMFROMTEXT, ST_GEOMFROMWKB, ST_INTERIORRINGN, ST_INTERSECTION,
    ST_INTERSECTS, ST_ISCLOSED, ST_ISEMPTY, ST_ISSIMPLE, ST_LINEFROMTEXT, ST_LINEFROMWKB,
    ST_LINESTRINGFROMTEXT, ST_LINESTRINGFROMWKB, ST_NUMGEOMETRIES, ST_NUMINTERIORRING,
    ST_NUMINTERIORRINGS, ST_NUMPOINTS, ST_OVERLAPS, ST_POINTFROMTEXT, ST_POINTFROMWKB, ST_POINTN,
    ST_POLYFROMTEXT, ST_POLYFROMWKB, ST_POLYGONFROMTEXT, ST_POLYGONFROMWKB, ST_SRID, ST_STARTPOINT,
    ST_SYMDIFFERENCE, ST_TOUCHES, ST_UNION, ST_WITHIN, ST_X, ST_Y, SUBDATE, SUBSTRING_INDEX,
    SUBTIME, SYSTEM_USER, TAN, TIME, TIMEDIFF, TIMESTAMP, TIMESTAMPADD, TIMESTAMPDIFF, TIME_FORMAT,
    TIME_TO_SEC, TOUCHES, TO_BASE64, TO_DAYS, TO_SECONDS, UCASE, UNCOMPRESS, UNCOMPRESSED_LENGTH,
    UNHEX, UNIX_TIMESTAMP, UPDATEXML, UPPER, UUID, UUID_SHORT, VALIDATE_PASSWORD_STRENGTH, VERSION,
    WAIT_UNTIL_SQL_THREAD_AFTER_GTIDS, WEEK, WEEKDAY, WEEKOFYEAR, WEIGHT_STRING, WITHIN, YEAR,
    YEARWEEK, Y_FUNCTION, X_FUNCTION, //

    ASCII, CURDATE, CURRENT_DATE, CURRENT_TIME, CURRENT_TIMESTAMP, CURTIME, DATE_ADD, DATE_SUB, IF,
    INSERT, LOCALTIME, LOCALTIMESTAMP, MID, NOW, REPLACE, SUBSTR, SUBSTRING, SYSDATE, TRIM,
    UTC_DATE, UTC_TIME, UTC_TIMESTAMP;

    @Override
    public String literal() {
      return name();
    }
  }

  public static class UdfFunctionCall implements FunctionCall {
    public final FullId fullId;
    public final FunctionArgs functionArgs;

    UdfFunctionCall(FullId fullId, FunctionArgs functionArgs) {
      Preconditions.checkArgument(fullId != null);

      this.fullId = fullId;
      this.functionArgs = functionArgs;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append(fullId.literal());
      sb.append("(");
      if (functionArgs != null) {
        sb.append(functionArgs.literal());
      }
      sb.append(")");
      return sb.toString();
    }

  }

  /**
   * <pre>
   passwordFunctionClause
    : functionName=(PASSWORD | OLD_PASSWORD) '(' functionArg ')'
    ;
   * </pre>
   */
  public static class PasswordFunctionClause implements FunctionCall {
    public static enum Type implements RelationalAlgebraEnum {
      PASSWORD, OLD_PASSWORD;

      @Override
      public String literal() {
        return name();
      }
    }

    public final PasswordFunctionClause.Type functionName;
    public final FunctionArg functionArg;

    PasswordFunctionClause(PasswordFunctionClause.Type functionName, FunctionArg functionArg) {
      Preconditions.checkArgument(functionName != null);
      Preconditions.checkArgument(functionArg != null);

      this.functionName = functionName;
      this.functionArg = functionArg;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append(functionName.literal());
      sb.append("(");
      sb.append(functionArg.literal());
      sb.append(")");
      return sb.toString();
    }

  }

  /**
   * <pre>
   levelsInWeightString
    : LEVEL levelInWeightListElement
      (',' levelInWeightListElement)*                               #levelWeightList
    | LEVEL
      firstLevel=decimalLiteral '-' lastLevel=decimalLiteral        #levelWeightRange
    ;
   * </pre>
   */
  public static interface LevelsInWeightString extends Functions {
  }

  public static class LevelWeightList implements LevelsInWeightString {
    public final List<LevelInWeightListElement> levelInWeightListElements;

    LevelWeightList(List<LevelInWeightListElement> levelInWeightListElements) {
      Preconditions.checkArgument(levelInWeightListElements != null //
          && levelInWeightListElements.size() > 0);
      this.levelInWeightListElements = levelInWeightListElements;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("LEVEL ");

      List<String> literals = Lists.newArrayList();
      for (LevelInWeightListElement levelInWeightListElement : levelInWeightListElements) {
        literals.add(levelInWeightListElement.literal());
      }
      sb.append(Joiner.on(", ").join(literals));

      return sb.toString();
    }

  }

  public static class LevelWeightRange implements LevelsInWeightString {
    public final DecimalLiteral firstLevel;
    public final DecimalLiteral lastLevel;

    LevelWeightRange(DecimalLiteral firstLevel, DecimalLiteral lastLevel) {
      Preconditions.checkArgument(firstLevel != null);
      Preconditions.checkArgument(lastLevel != null);

      this.firstLevel = firstLevel;
      this.lastLevel = lastLevel;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("LEVEL ");
      sb.append(firstLevel.literal());
      sb.append(" - ");
      sb.append(lastLevel.literal());
      return sb.toString();
    }
  }

  /**
   * <pre>
   levelInWeightListElement
    : decimalLiteral orderType=(ASC | DESC | REVERSE)?
    ;
   * </pre>
   */
  public static class LevelInWeightListElement implements Functions {
    public static enum OrderType implements RelationalAlgebraEnum {
      ASC, DESC, REVERSE;

      @Override
      public String literal() {
        return name();
      }
    }

    public final DecimalLiteral decimalLiteral;
    public final LevelInWeightListElement.OrderType orderType;

    LevelInWeightListElement(DecimalLiteral decimalLiteral,
        LevelInWeightListElement.OrderType orderType) {
      Preconditions.checkArgument(decimalLiteral != null);

      this.decimalLiteral = decimalLiteral;
      this.orderType = orderType;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append(decimalLiteral.literal());
      if (orderType != null) {
        sb.append(" ").append(orderType.literal());
      }
      return sb.toString();
    }

  }

  /**
   * <pre>
  aggregateWindowedFunction
    : (AVG | MAX | MIN | SUM)
      '(' aggregator=(ALL | DISTINCT)? functionArg ')'
    | COUNT '(' (starArg='*' | aggregator=ALL? functionArg) ')'
    | COUNT '(' aggregator=DISTINCT functionArgs ')'
    | (
        BIT_AND | BIT_OR | BIT_XOR | STD | STDDEV | STDDEV_POP
        | STDDEV_SAMP | VAR_POP | VAR_SAMP | VARIANCE
      ) '(' aggregator=ALL? functionArg ')'
    | GROUP_CONCAT '('
        aggregator=DISTINCT? functionArgs
        (ORDER BY
          orderByExpression (',' orderByExpression)*
        )? (SEPARATOR separator=STRING_LITERAL)?
      ')'
    ;
   * </pre>
   */
  public static class AggregateWindowedFunction implements FunctionCall {
    public static enum Type implements RelationalAlgebraEnum {
      AVG, MAX, MIN, SUM, //
      COUNT, COUNT_DISTINCT, //
      BIT_AND, BIT_OR, BIT_XOR, STD, STDDEV, STDDEV_POP, STDDEV_SAMP, VAR_POP, VAR_SAMP, VARIANCE, //
      GROUP_CONCAT;

      @Override
      public String literal() {
        return name();
      }
    }

    public static enum AggregatorEnum implements RelationalAlgebraEnum {
      ALL, DISTINCT;

      @Override
      public String literal() {
        return name();
      }
    }

    public final AggregateWindowedFunction.Type type;
    public final AggregateWindowedFunction.AggregatorEnum aggregator;
    public final FunctionArg functionArg;
    public final FunctionArgs functionArgs;
    public final List<OrderByExpression> orderByExpressions;
    public final String separator;

    AggregateWindowedFunction(AggregateWindowedFunction.Type type,
        AggregateWindowedFunction.AggregatorEnum aggregator, FunctionArg functionArg,
        FunctionArgs functionArgs, List<OrderByExpression> orderByExpressions, String separator) {
      Preconditions.checkArgument(type != null);

      switch (type) {
      case AVG:
      case MAX:
      case MIN:
      case SUM:
        Preconditions.checkArgument(functionArg != null);
        break;
      case COUNT:
        Preconditions.checkArgument(functionArg != null);
        break;
      case COUNT_DISTINCT:
        Preconditions.checkArgument(functionArgs != null);
        break;
      case BIT_AND:
      case BIT_OR:
      case BIT_XOR:
      case STD:
      case STDDEV:
      case STDDEV_POP:
      case STDDEV_SAMP:
      case VAR_POP:
        Preconditions.checkArgument(functionArg != null);
        break;
      case GROUP_CONCAT:
        Preconditions.checkArgument(functionArgs != null);
        break;
      default:
        Preconditions.checkArgument(false);
        break;
      }

      this.type = type;
      this.aggregator = aggregator;
      this.functionArg = functionArg;
      this.functionArgs = functionArgs;
      this.orderByExpressions = orderByExpressions;
      this.separator = separator;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();

      switch (type) {
      case AVG:
      case MAX:
      case MIN:
      case SUM:
        sb.append(type.literal());
        sb.append("(");
        if (aggregator != null) {
          sb.append(aggregator.literal());
        }
        sb.append(functionArg.literal());
        sb.append(")");

        break;
      case COUNT:
        sb.append(type.literal());
        sb.append("(");
        if (functionArg == null) {
          sb.append("*");
        } else {
          sb.append(functionArg.literal());
        }
        sb.append(")");
        break;
      case COUNT_DISTINCT:
        sb.append("COUNT ( DISTINCT ");
        sb.append(functionArgs.literal());
        sb.append(")");
        break;
      case BIT_AND:
      case BIT_OR:
      case BIT_XOR:
      case STD:
      case STDDEV:
      case STDDEV_POP:
      case STDDEV_SAMP:
      case VAR_POP:
        sb.append(type.literal());
        sb.append("(");
        sb.append(functionArg.literal());
        sb.append(")");
        Preconditions.checkArgument(functionArg != null);
        break;
      case GROUP_CONCAT:
        sb.append(type.literal());
        sb.append("(");
        if (aggregator != null) {
          sb.append(aggregator.literal()).append(" ");
        }
        sb.append(functionArgs.literal());
        if (CollectionUtils.isNotEmpty(orderByExpressions)) {
          sb.append("ORDER BY ");
          List<String> literals = Lists.newArrayList();
          for (OrderByExpression e : orderByExpressions) {
            literals.add(e.literal());
          }
          sb.append(Joiner.on(", ").join(literals));
        }
        sb.append(")");
        break;
      default:
        break;
      }

      return sb.toString();
    }

  }

  /**
   * <pre>
   functionArgs
    : (constant | fullColumnName | functionCall | expression)
    (
      ','
      (constant | fullColumnName | functionCall | expression)
    )*
    ;
   * </pre>
   */
  public static class FunctionArgs implements SpecificFunction, Functions {
    public final List<FunctionArg> functionArgs;

    FunctionArgs(List<FunctionArg> functionArgs) {
      Preconditions.checkArgument(functionArgs != null && functionArgs.size() > 0);

      this.functionArgs = functionArgs;
    }

    @Override
    public String literal() {
      List<String> literals = Lists.newArrayList();
      for (FunctionArg functionArg : functionArgs) {
        literals.add(functionArg.literal());
      }
      return Joiner.on(", ").join(literals);
    }

  }

  /**
   * <pre>
   functionArg
    : constant | fullColumnName | functionCall | expression
    ;
   * </pre>
   */
  public static class FunctionArg implements Functions {
    public static enum Type implements RelationalAlgebraEnum {
      CONSTANT, FULL_COLUMN_NAME, FUNCTION_CALL, EXPRESSION;

      @Override
      public String literal() {
        return name();
      }
    }

    public final FunctionArg.Type type;
    public Constant constant;
    public FullColumnName fullColumnName;
    public FunctionCall functionCall;
    public Expression expression;

    FunctionArg(FunctionArg.Type type, Object value) {
      Preconditions.checkArgument(type != null);

      this.type = type;

      switch (type) {
      case CONSTANT:
        constant = (Constant) value;
        break;
      case FULL_COLUMN_NAME:
        fullColumnName = (FullColumnName) value;
        break;
      case FUNCTION_CALL:
        functionCall = (FunctionCall) value;
        break;
      case EXPRESSION:
        expression = (Expression) value;
        break;
      default:
        Preconditions.checkArgument(false);
        break;
      }
    }

    @Override
    public String literal() {
      if (constant != null) {
        return constant.literal();
      } else if (fullColumnName != null) {
        return fullColumnName.literal();
      } else if (functionCall != null) {
        return functionCall.literal();
      } else {
        return expression.literal();
      }
    }

  }
}
