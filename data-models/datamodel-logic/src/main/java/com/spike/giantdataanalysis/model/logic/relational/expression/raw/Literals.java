package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.CollationName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SimpleIdSets.CharsetNameBaseEnum;

/**
 * Literals
 */
public abstract class Literals implements PrimitiveExpression {

  /**
   * <pre>
   decimalLiteral
    : DECIMAL_LITERAL | ZERO_DECIMAL | ONE_DECIMAL | TWO_DECIMAL
    ;
   * </pre>
   */
  public static class DecimalLiteral extends Literals {
    public static enum Type implements RelationalAlgebraEnum {
      DECIMAL_LITERAL, // [0-9]+
      ZERO_DECIMAL, // 0
      ONE_DECIMAL, // 1
      TWO_DECIMAL; // 2

      @Override
      public String literal() {
        return name();
      }
    }

    public final DecimalLiteral.Type type;
    public final String literal;

    DecimalLiteral(DecimalLiteral.Type type, String literal) {
      Preconditions.checkArgument(type != null);
      Preconditions.checkArgument(literal != null);

      this.type = type;
      this.literal = literal;
    }

    @Override
    public String literal() {
      return literal;
    }
  }

  /**
   * <pre>
  fileSizeLiteral
    : FILESIZE_LITERAL | decimalLiteral;
   * </pre>
   */
  public static class FileSizeLiteral extends Literals {

    public final String filesizeLiteral;
    public final DecimalLiteral decimalLiteral;

    FileSizeLiteral(String filesizeLiteral, DecimalLiteral decimalLiteral) {
      Preconditions.checkArgument(!(filesizeLiteral == null && decimalLiteral == null));
      this.filesizeLiteral = filesizeLiteral;
      this.decimalLiteral = decimalLiteral;
    }

    @Override
    public String literal() {
      if (filesizeLiteral != null) {
        return filesizeLiteral;
      } else {
        return decimalLiteral.literal();
      }
    }

  }

  /**
   * <pre>
   stringLiteral
    : (
        STRING_CHARSET_NAME? STRING_LITERAL
        | START_NATIONAL_STRING_LITERAL
      ) STRING_LITERAL+
    | (
        STRING_CHARSET_NAME? STRING_LITERAL
        | START_NATIONAL_STRING_LITERAL
      ) (COLLATE collationName)?
   * </pre>
   */
  public static class StringLiteral extends Literals {
    public final CharsetNameBaseEnum stringCharsetName;
    public final List<String> stringLiterals;
    public final String startNationalStringLiteral;
    public final CollationName collationName;

    StringLiteral(CharsetNameBaseEnum stringCharsetName, List<String> stringLiterals,
        String startNationalStringLiteral, CollationName collationName) {
      Preconditions.checkArgument(!(startNationalStringLiteral == null //
          && (stringLiterals == null || stringLiterals.size() == 0)));

      this.stringCharsetName = stringCharsetName;
      this.stringLiterals = stringLiterals;
      this.startNationalStringLiteral = startNationalStringLiteral;
      this.collationName = collationName;
    }

    @Override
    public String literal() {
      StringBuilder builder = new StringBuilder();
      if (startNationalStringLiteral != null) {
        builder.append(startNationalStringLiteral);
        builder.append(" ");
        if (CollectionUtils.isNotEmpty(stringLiterals)) {
          builder.append(Joiner.on(" ").join(stringLiterals));
        }
      } else {
        builder.append(Joiner.on(" ").join(stringLiterals));
      }
      return builder.toString();
    }
  }

  /**
   * <pre>
   booleanLiteral
    : TRUE | FALSE;
   * </pre>
   */
  public static class BooleanLiteral extends Literals {
    public final Boolean literal;

    BooleanLiteral(Boolean literal) {
      Preconditions.checkArgument(literal != null);

      this.literal = literal;
    }

    @Override
    public String literal() {
      return literal.toString();
    }
  }

  /**
   * <pre>
  hexadecimalLiteral
    : STRING_CHARSET_NAME? HEXADECIMAL_LITERAL;
   * </pre>
   */
  public static class HexadecimalLiteral extends Literals {
    public final CharsetNameBaseEnum stringCharsetName;
    public final String literal;

    HexadecimalLiteral(CharsetNameBaseEnum stringCharsetName, String literal) {
      Preconditions.checkArgument(literal != null);

      this.stringCharsetName = stringCharsetName;
      this.literal = literal;
    }

    @Override
    public String literal() {
      return literal;
    }

  }

  /**
   * [NOT] NULL [NOT] \\N
   * 
   * <pre>
  nullNotnull
    : NOT? (NULL_LITERAL | NULL_SPEC_LITERAL)
    ;
   * </pre>
   */
  public static class NullNotnull extends Literals {
    public final Boolean not;
    public final String nullLiteral;

    public final String nullSpecLiteral;

    NullNotnull(Boolean not, String nullLiteral, String nullSpecLiteral) {
      Preconditions.checkArgument(!(nullLiteral == null && nullSpecLiteral == null));

      this.not = not;
      this.nullLiteral = nullLiteral;
      this.nullSpecLiteral = nullSpecLiteral;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      if (Boolean.TRUE.equals(not)) {
        sb.append("NOT ");
      }
      if (nullLiteral != null) {
        sb.append(nullLiteral);
      } else {
        sb.append(nullSpecLiteral);
      }
      return sb.toString();
    }
  }

  /**
   * <pre>
   constant
    : stringLiteral 
    | decimalLiteral
    | '-' decimalLiteral
    | hexadecimalLiteral 
    | booleanLiteral
    | REAL_LITERAL 
    | BIT_STRING
    | NOT? nullLiteral=(NULL_LITERAL | NULL_SPEC_LITERAL)
    ;
   * </pre>
   */
  public static class Constant extends Literals implements ExpressionAtom {

    public static enum Type implements RelationalAlgebraEnum {
      STRING_LITERAL, //
      DECIMAL_LITERAL, //
      HEXADECIMAL_LITERAL, //
      BOOLEAN_LITERAL, //
      REAL_LITERAL, //
      BIT_STRING, //
      NULL_LITERAL;

      @Override
      public String literal() {
        return name();
      }
    }

    public final Constant.Type type;
    public final String literal;

    public final Boolean not; // maybe null

    Constant(Constant.Type type, String literal, Boolean not) {
      Preconditions.checkArgument(type != null);
      Preconditions.checkArgument(literal != null);

      this.type = type;
      this.literal = literal;
      this.not = not;
    }

    @Override
    public String literal() {
      StringBuilder builder = new StringBuilder();

      if (Boolean.TRUE.equals(not)) {
        builder.append("NOT ");
      }
      builder.append(literal);
      return builder.toString();
    }

  }
}
