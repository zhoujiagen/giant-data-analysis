package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.CharsetName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.CollationName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Literals.DecimalLiteral;

/**
 * <pre>
dataType
    : typeName=(
      CHAR | VARCHAR | TINYTEXT | TEXT | MEDIUMTEXT | LONGTEXT
       | NCHAR | NVARCHAR
      )
      lengthOneDimension? BINARY?
      ((CHARACTER SET | CHARSET) charsetName)?
      (COLLATE collationName)?                                      #stringDataType
    | NATIONAL typeName=(VARCHAR | CHARACTER)
      lengthOneDimension? BINARY?                                   #nationalStringDataType
    | NCHAR typeName=VARCHAR
      lengthOneDimension? BINARY?                                   #nationalStringDataType
    | NATIONAL typeName=(CHAR | CHARACTER) VARYING
      lengthOneDimension? BINARY?                                   #nationalVaryingStringDataType
    | typeName=(
        TINYINT | SMALLINT | MEDIUMINT | INT | INTEGER | BIGINT
      )
      lengthOneDimension? (SIGNED | UNSIGNED)? ZEROFILL?            #dimensionDataType
    | typeName=REAL
      lengthTwoDimension? (SIGNED | UNSIGNED)? ZEROFILL?            #dimensionDataType
    | typeName=DOUBLE PRECISION?
          lengthTwoDimension? (SIGNED | UNSIGNED)? ZEROFILL?            #dimensionDataType
    | typeName=(DECIMAL | DEC | FIXED | NUMERIC | FLOAT)
      lengthTwoOptionalDimension? (SIGNED | UNSIGNED)? ZEROFILL?    #dimensionDataType
    | typeName=(
        DATE | TINYBLOB | BLOB | MEDIUMBLOB | LONGBLOB
        | BOOL | BOOLEAN | SERIAL
      )                                                             #simpleDataType
    | typeName=(
        BIT | TIME | TIMESTAMP | DATETIME | BINARY
        | VARBINARY | YEAR
      )
      lengthOneDimension?                                           #dimensionDataType
    | typeName=(ENUM | SET)
      collectionOptions BINARY?
      ((CHARACTER SET | CHARSET) charsetName)?                      #collectionDataType
    | typeName=(
        GEOMETRYCOLLECTION | GEOMCOLLECTION | LINESTRING | MULTILINESTRING
        | MULTIPOINT | MULTIPOLYGON | POINT | POLYGON | JSON | GEOMETRY
      )                                                             #spatialDataType
    ;
 * </pre>
 */
public interface DataType extends PrimitiveExpression {

  public static class StringDataType implements DataType {
    public static enum Type implements RelationalAlgebraEnum {
      CHAR, VARCHAR, TINYTEXT, TEXT, MEDIUMTEXT, LONGTEXT, NCHAR, NVARCHAR;

      @Override
      public String literal() {
        return name();
      }
    }

    public final StringDataType.Type dataType;
    public final LengthOneDimension lengthOneDimension;
    public final Boolean binary;
    public final CharsetName charsetName;
    public final CollationName collationName;

    StringDataType(StringDataType.Type dataType, LengthOneDimension lengthOneDimension,
        Boolean binary, CharsetName charsetName, CollationName collationName) {
      Preconditions.checkArgument(dataType != null);

      this.dataType = dataType;
      this.lengthOneDimension = lengthOneDimension;
      this.binary = binary;
      this.charsetName = charsetName;
      this.collationName = collationName;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append(dataType.literal()).append(" ");
      if (lengthOneDimension != null) {
        sb.append(lengthOneDimension.literal()).append(" ");
      }
      if (Boolean.TRUE.equals(binary)) {
        sb.append("BINARY ");
      }
      if (charsetName != null) {
        sb.append("CHARSET ").append(charsetName.literal()).append(" ");
      }
      if (collationName != null) {
        sb.append("COLLATE ").append(collationName.literal());
      }
      return sb.toString();
    }

  }

  public static class NationalStringDataType implements DataType {
    public static enum NType implements RelationalAlgebraEnum {
      NATIONAL, NCHAR;

      @Override
      public String literal() {
        return name();
      }
    }

    public static enum Type implements RelationalAlgebraEnum {
      VARCHAR, CHARACTER;
      @Override
      public String literal() {
        return name();
      }
    }

    public final NationalStringDataType.NType type;
    public final NationalStringDataType.Type dataType;
    public final LengthOneDimension lengthOneDimension;
    public final Boolean binary;

    NationalStringDataType(NationalStringDataType.NType type, NationalStringDataType.Type dataType,
        LengthOneDimension lengthOneDimension, Boolean binary) {
      Preconditions.checkArgument(type != null);
      Preconditions.checkArgument(dataType != null);

      switch (type) {
      case NATIONAL:
        Preconditions.checkArgument(NationalStringDataType.Type.CHARACTER.equals(dataType)
            || NationalStringDataType.Type.VARCHAR.equals(dataType));
        break;
      case NCHAR:
        Preconditions.checkArgument(NationalStringDataType.Type.VARCHAR.equals(dataType));
        break;
      default:
        Preconditions.checkArgument(false);
        break;
      }

      this.type = type;
      this.dataType = dataType;
      this.lengthOneDimension = lengthOneDimension;
      this.binary = binary;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append(type.literal()).append(" ");
      sb.append(dataType.literal()).append(" ");
      if (lengthOneDimension != null) {
        sb.append(lengthOneDimension.literal()).append(" ");
      }
      if (Boolean.TRUE.equals(binary)) {
        sb.append("BINARY");
      }
      return sb.toString();
    }

  }

  public static class NationalVaryingStringDataType implements DataType {
    public static enum Type implements RelationalAlgebraEnum {
      CHAR, CHARACTER;
      @Override
      public String literal() {
        return name();
      }
    }

    public final NationalVaryingStringDataType.Type dataType;
    public final LengthOneDimension lengthOneDimension;
    public final Boolean binary;

    NationalVaryingStringDataType(NationalVaryingStringDataType.Type dataType,
        LengthOneDimension lengthOneDimension, Boolean binary) {
      Preconditions.checkArgument(dataType != null);

      this.dataType = dataType;
      this.lengthOneDimension = lengthOneDimension;
      this.binary = binary;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("NATIONAL ");
      sb.append(dataType.literal()).append(" ");
      sb.append("VARYING ");
      if (lengthOneDimension != null) {
        sb.append(lengthOneDimension.literal()).append(" ");
      }
      if (Boolean.TRUE.equals(binary)) {
        sb.append("BINARY");
      }
      return sb.toString();
    }
  }

  public static class DimensionDataType implements DataType {
    public static enum Type implements RelationalAlgebraEnum {
      TINYINT, SMALLINT, MEDIUMINT, INT, INTEGER, BIGINT, //
      REAL, //
      DOUBLE, //
      DECIMAL, DEC, FIXED, NUMERIC, FLOAT, //
      BIT, TIME, TIMESTAMP, DATETIME, BINARY, VARBINARY, YEAR;

      @Override
      public String literal() {
        return name();
      }
    }

    public final DimensionDataType.Type dataType;
    public final LengthOneDimension lengthOneDimension;
    public final LengthTwoDimension lengthTwoDimension;
    public final LengthTwoOptionalDimension lengthTwoOptionalDimension;
    public final Boolean signed;
    public final Boolean zeroFill;
    public final Boolean precision;

    DimensionDataType(DimensionDataType.Type dataType, LengthOneDimension lengthOneDimension,
        LengthTwoDimension lengthTwoDimension,
        LengthTwoOptionalDimension lengthTwoOptionalDimension, Boolean signed, Boolean zeroFill,
        Boolean precision) {
      Preconditions.checkArgument(dataType != null);

      switch (dataType) {
      case TINYINT:
      case SMALLINT:
      case MEDIUMINT:
      case INT:
      case INTEGER:
      case BIGINT:
        Preconditions.checkArgument(lengthTwoDimension == null);
        Preconditions.checkArgument(lengthTwoOptionalDimension == null);
        Preconditions.checkArgument(precision == null);
        break;

      case REAL:
        Preconditions.checkArgument(lengthOneDimension == null);
        Preconditions.checkArgument(lengthTwoOptionalDimension == null);
        Preconditions.checkArgument(precision == null);
        break;

      case DOUBLE:
        Preconditions.checkArgument(lengthOneDimension == null);
        Preconditions.checkArgument(lengthTwoOptionalDimension == null);
        break;

      case DECIMAL:
      case DEC:
      case FIXED:
      case NUMERIC:
      case FLOAT:
        Preconditions.checkArgument(lengthOneDimension == null);
        Preconditions.checkArgument(lengthTwoDimension == null);
        Preconditions.checkArgument(precision == null);
        break;

      case BIT:
      case TIME:
      case TIMESTAMP:
      case DATETIME:
      case BINARY:
      case VARBINARY:
      case YEAR:
        Preconditions.checkArgument(lengthTwoDimension == null);
        Preconditions.checkArgument(lengthTwoOptionalDimension == null);
        Preconditions.checkArgument(signed == null);
        Preconditions.checkArgument(zeroFill == null);
        Preconditions.checkArgument(precision == null);
        break;

      default:
        Preconditions.checkArgument(false);
        break;
      }

      this.dataType = dataType;
      this.lengthOneDimension = lengthOneDimension;
      this.lengthTwoDimension = lengthTwoDimension;
      this.lengthTwoOptionalDimension = lengthTwoOptionalDimension;
      this.signed = signed;
      this.zeroFill = zeroFill;
      this.precision = precision;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();

      switch (dataType) {
      case TINYINT:
      case SMALLINT:
      case MEDIUMINT:
      case INT:
      case INTEGER:
      case BIGINT:
        sb.append(dataType.literal()).append(" ");
        if (lengthOneDimension != null) {
          sb.append(lengthOneDimension.literal()).append(" ");
        }
        if (Boolean.TRUE.equals(signed)) {
          sb.append("SIGNED ");
        } else if (Boolean.FALSE.equals(signed)) {
          sb.append("UNSIGNED ");
        }
        if (Boolean.TRUE.equals(zeroFill)) {
          sb.append("ZEROFILL ");
        }
        break;

      case REAL:
        sb.append(dataType.literal()).append(" ");
        if (lengthTwoDimension != null) {
          sb.append(lengthTwoDimension.literal()).append(" ");
        }
        if (Boolean.TRUE.equals(signed)) {
          sb.append("SIGNED ");
        } else if (Boolean.FALSE.equals(signed)) {
          sb.append("UNSIGNED ");
        }
        if (Boolean.TRUE.equals(zeroFill)) {
          sb.append("ZEROFILL ");
        }
        break;

      case DOUBLE:
        sb.append(dataType.literal()).append(" ");
        if (Boolean.TRUE.equals(precision)) {
          sb.append("PRECISION ");
        }
        if (lengthTwoDimension != null) {
          sb.append(lengthTwoDimension.literal()).append(" ");
        }
        if (Boolean.TRUE.equals(signed)) {
          sb.append("SIGNED ");
        } else if (Boolean.FALSE.equals(signed)) {
          sb.append("UNSIGNED ");
        }
        if (Boolean.TRUE.equals(zeroFill)) {
          sb.append("ZEROFILL ");
        }
        break;

      case DECIMAL:
      case DEC:
      case FIXED:
      case NUMERIC:
      case FLOAT:
        sb.append(dataType.literal()).append(" ");
        if (lengthTwoOptionalDimension != null) {
          sb.append(lengthTwoOptionalDimension.literal()).append(" ");
        }
        if (Boolean.TRUE.equals(signed)) {
          sb.append("SIGNED ");
        } else if (Boolean.FALSE.equals(signed)) {
          sb.append("UNSIGNED ");
        }
        if (Boolean.TRUE.equals(zeroFill)) {
          sb.append("ZEROFILL ");
        }
        break;

      case BIT:
      case TIME:
      case TIMESTAMP:
      case DATETIME:
      case BINARY:
      case VARBINARY:
      case YEAR:
        sb.append(dataType.literal()).append(" ");
        if (lengthOneDimension != null) {
          sb.append(lengthOneDimension.literal()).append(" ");
        }
        break;

      default:
        break;
      }

      return sb.toString();
    }

  }

  public static class SimpleDataType implements DataType {
    public static enum Type implements RelationalAlgebraEnum {
      DATE, TINYBLOB, BLOB, MEDIUMBLOB, LONGBLOB, BOOL, BOOLEAN, SERIAL;

      @Override
      public String literal() {
        return name();
      }
    }

    public final SimpleDataType.Type dataType;

    SimpleDataType(SimpleDataType.Type dataType) {
      Preconditions.checkArgument(dataType != null);
      this.dataType = dataType;
    }

    @Override
    public String literal() {
      return dataType.literal();
    }
  }

  public static class CollectionDataType implements DataType {
    public static enum Type implements RelationalAlgebraEnum {
      ENUM, SET;

      @Override
      public String literal() {
        return name();
      }
    }

    public final CollectionDataType.Type dataType;
    public final CollectionOptions collectionOptions;
    public final Boolean binary;
    public final CharsetName charsetName;

    CollectionDataType(CollectionDataType.Type dataType, CollectionOptions collectionOptions,
        Boolean binary, CharsetName charsetName) {
      Preconditions.checkArgument(dataType != null);
      Preconditions.checkArgument(collectionOptions != null);

      this.dataType = dataType;
      this.collectionOptions = collectionOptions;
      this.binary = binary;
      this.charsetName = charsetName;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append(dataType.literal()).append(" ");
      sb.append(collectionOptions.literal()).append(" ");
      if (Boolean.TRUE.equals(binary)) {
        sb.append("BINARY ");
      }
      if (charsetName != null) {
        sb.append("CHARSET ").append(charsetName.literal());
      }
      return sb.toString();
    }
  }

  public static class SpatialDataType implements DataType {
    public static enum Type implements RelationalAlgebraEnum {
      GEOMETRYCOLLECTION, GEOMCOLLECTION, LINESTRING, MULTILINESTRING, MULTIPOINT, MULTIPOLYGON,
      POINT, POLYGON, JSON, GEOMETRY;

      @Override
      public String literal() {
        return name();
      }
    }

    public final SpatialDataType.Type dataType;

    SpatialDataType(SpatialDataType.Type dataType) {
      Preconditions.checkArgument(dataType != null);

      this.dataType = dataType;
    }

    @Override
    public String literal() {
      return dataType.literal();
    }
  }

  /**
   * <pre>
  collectionOptions
    : '(' STRING_LITERAL (',' STRING_LITERAL)* ')'
    ;
   * </pre>
   */
  public static class CollectionOptions implements DataType {
    public final List<String> stringLiterals;

    CollectionOptions(List<String> stringLiterals) {
      Preconditions.checkArgument(stringLiterals != null && stringLiterals.size() > 0);

      this.stringLiterals = stringLiterals;
    }

    @Override
    public String literal() {
      return Joiner.on(", ").join(stringLiterals);
    }
  }

  /**
   * <pre>
  convertedDataType
    : typeName=(BINARY| NCHAR) lengthOneDimension?
    | typeName=CHAR lengthOneDimension? (CHARACTER SET charsetName)?
    | typeName=(DATE | DATETIME | TIME)
    | typeName=DECIMAL lengthTwoDimension?
    | (SIGNED | UNSIGNED) INTEGER?
    ;
   * </pre>
   */
  public static class ConvertedDataType implements DataType {
    public static enum Type implements RelationalAlgebraEnum {
      // typeName=(BINARY| NCHAR) lengthOneDimension?
      BINARY, NCHAR, //
      // typeName=CHAR lengthOneDimension? (CHARACTER SET charsetName)?
      CHAR, //
      // typeName=(DATE | DATETIME | TIME)
      DATE, DATETIME, TIME, //
      // typeName=DECIMAL lengthTwoDimension?
      DECIMAL, //
      // (SIGNED | UNSIGNED) INTEGER?
      INTEGER;

      @Override
      public String literal() {
        return name();
      }
    }

    public final ConvertedDataType.Type type;
    public final LengthOneDimension lengthOneDimension;
    public final CharsetName charsetName;
    public final LengthTwoDimension lengthTwoDimension;
    public final Boolean signed;

    /** Type.BINARY, Type.NCHAR */
    ConvertedDataType(ConvertedDataType.Type type, LengthOneDimension lengthOneDimension) {
      Preconditions.checkArgument(Type.BINARY.equals(type) || Type.NCHAR.equals(type));

      this.type = type;
      this.lengthOneDimension = lengthOneDimension;
      this.charsetName = null;
      this.lengthTwoDimension = null;
      this.signed = null;
    }

    /** Type.CHAR */
    ConvertedDataType(ConvertedDataType.Type type, LengthOneDimension lengthOneDimension,
        CharsetName charsetName) {
      Preconditions.checkArgument(Type.CHAR.equals(type));

      this.type = type;
      this.lengthOneDimension = lengthOneDimension;
      this.charsetName = charsetName;
      this.lengthTwoDimension = null;
      this.signed = null;
    }

    /** Type.DATE,Type.DATETIME,Type.TIME */
    ConvertedDataType(ConvertedDataType.Type type) {
      Preconditions.checkArgument(
        Type.DATE.equals(type) || Type.DATETIME.equals(type) || Type.TIME.equals(type));

      this.type = type;
      this.lengthOneDimension = null;
      this.charsetName = null;
      this.lengthTwoDimension = null;
      this.signed = null;
    }

    /** Type.DECIMAL */
    ConvertedDataType(ConvertedDataType.Type type, LengthTwoDimension lengthTwoDimension) {
      Preconditions.checkArgument(Type.DECIMAL.equals(type));

      this.type = type;
      this.lengthOneDimension = null;
      this.charsetName = null;
      this.lengthTwoDimension = lengthTwoDimension;
      this.signed = null;
    }

    /** Type.INTEGER */
    ConvertedDataType(boolean signed, ConvertedDataType.Type type) {
      if (type != null) {
        Preconditions.checkArgument(Type.INTEGER.equals(type));
      }

      this.type = type;
      this.lengthOneDimension = null;
      this.charsetName = null;
      this.lengthTwoDimension = null;
      this.signed = signed;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();

      switch (type) {
      case BINARY:
      case NCHAR:
        sb.append(type.literal());
        if (lengthOneDimension != null) {
          sb.append(" ").append(lengthOneDimension.literal());
        }
        break;

      case CHAR:
        sb.append(type.literal());
        if (lengthOneDimension != null) {
          sb.append(" ").append(lengthOneDimension.literal());
        }
        if (charsetName != null) {
          sb.append(" CHARACTER SET ").append(charsetName.literal());
        }
        break;

      case DATE:
      case DATETIME:
      case TIME:
        sb.append(type.literal());
        break;

      case DECIMAL:
        sb.append(type.literal());
        if (lengthTwoDimension != null) {
          sb.append(" ").append(lengthTwoDimension.literal());
        }
        break;

      case INTEGER:
        if (Boolean.TRUE.equals(signed)) {
          sb.append("SIGNED");
        } else {
          sb.append("UNSIGNED");
        }
        if (type != null) {
          sb.append(" ").append(type.literal());
        }
        break;

      default:
        break;
      }

      return sb.toString();

    }

  }

  /**
   * <pre>
  lengthOneDimension : '(' decimalLiteral ')'
   * </pre>
   */
  public static class LengthOneDimension implements DataType {
    public final DecimalLiteral decimalLiteral;

    LengthOneDimension(DecimalLiteral decimalLiteral) {
      Preconditions.checkArgument(decimalLiteral != null);

      this.decimalLiteral = decimalLiteral;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("(");
      sb.append(decimalLiteral.literal());
      sb.append(")");
      return sb.toString();
    }

  }

  /**
   * <pre>
  lengthTwoDimension : '(' decimalLiteral ',' decimalLiteral ')'
   * </pre>
   */
  public static class LengthTwoDimension implements DataType {
    public final DecimalLiteral first;
    public final DecimalLiteral second;

    public LengthTwoDimension(DecimalLiteral first, DecimalLiteral second) {
      Preconditions.checkArgument(first != null);
      Preconditions.checkArgument(second != null);

      this.first = first;
      this.second = second;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("(");
      sb.append(first.literal());
      sb.append(", ");
      sb.append(second.literal());
      sb.append(")");
      return sb.toString();
    }
  }

  /**
   * <pre>
  lengthTwoOptionalDimension : '(' decimalLiteral (',' decimalLiteral)? ')'
   * </pre>
   */
  public static class LengthTwoOptionalDimension implements DataType {
    public final DecimalLiteral first;
    public final DecimalLiteral second; // may be null

    public LengthTwoOptionalDimension(DecimalLiteral first, DecimalLiteral second) {
      Preconditions.checkArgument(first != null);

      this.first = first;
      this.second = second;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("(");
      sb.append(first.literal());
      if (second != null) {
        sb.append(", ");
        sb.append(second.literal);
      }
      sb.append(")");
      return sb.toString();
    }
  }
}
