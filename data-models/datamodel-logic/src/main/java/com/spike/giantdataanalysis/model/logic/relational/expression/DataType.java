package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.CharsetName;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.CollationName;
import com.spike.giantdataanalysis.model.logic.relational.expression.Literals.DecimalLiteral;
import com.spike.giantdataanalysis.model.logic.relational.expression.SimpleIdSets.CharsetNameBaseEnum;

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
      CHAR, VARCHAR, TINYTEXT, TEXT, MEDIUMTEXT, LONGTEXT, NCHAR, NVARCHAR
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

  }

  public static class NationalStringDataType implements DataType {
    public static enum NType implements RelationalAlgebraEnum {
      NATIONAL, NCHAR
    }

    public static enum Type implements RelationalAlgebraEnum {
      VARCHAR, CHARACTER
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

  }

  public static class NationalVaryingStringDataType implements DataType {
    public static enum Type implements RelationalAlgebraEnum {
      CHAR, CHARACTER
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
  }

  public static class DimensionDataType implements DataType {
    public static enum Type implements RelationalAlgebraEnum {
      TINYINT, SMALLINT, MEDIUMINT, INT, INTEGER, BIGINT, //
      REAL, //
      DOUBLE, //
      DECIMAL, DEC, FIXED, NUMERIC, FLOAT, //
      BIT, TIME, TIMESTAMP, DATETIME, BINARY, VARBINARY, YEAR;
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

  }

  public static class SimpleDataType implements DataType {
    public static enum Type implements RelationalAlgebraEnum {
      DATE, TINYBLOB, BLOB, MEDIUMBLOB, LONGBLOB, BOOL, BOOLEAN, SERIAL
    }

    public final SimpleDataType.Type dataType;

    SimpleDataType(SimpleDataType.Type dataType) {
      Preconditions.checkArgument(dataType != null);
      this.dataType = dataType;
    }
  }

  public static class CollectionDataType implements DataType {
    public static enum Type implements RelationalAlgebraEnum {
      ENUM, SET
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
  }

  public static class SpatialDataType implements DataType {
    public static enum Type implements RelationalAlgebraEnum {
      GEOMETRYCOLLECTION, GEOMCOLLECTION, LINESTRING, MULTILINESTRING, MULTIPOINT, MULTIPOLYGON,
      POINT, POLYGON, JSON, GEOMETRY
    }

    public final SpatialDataType.Type dataType;

    SpatialDataType(SpatialDataType.Type dataType) {
      Preconditions.checkArgument(dataType != null);
      this.dataType = dataType;
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
      INTEGER
    }

    public final ConvertedDataType.Type type;
    public final LengthOneDimension lengthOneDimension;
    public final CharsetNameBaseEnum charsetName;
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
        CharsetNameBaseEnum charsetName) {
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
    ConvertedDataType(ConvertedDataType.Type type, boolean signed) {
      Preconditions.checkArgument(Type.DECIMAL.equals(type));

      this.type = type;
      this.lengthOneDimension = null;
      this.charsetName = null;
      this.lengthTwoDimension = null;
      this.signed = signed;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("ConvertedDataType [type=");
      builder.append(type);
      builder.append(", lengthOneDimension=");
      builder.append(lengthOneDimension);
      builder.append(", charsetName=");
      builder.append(charsetName);
      builder.append(", lengthTwoDimension=");
      builder.append(lengthTwoDimension);
      builder.append(", signed=");
      builder.append(signed);
      builder.append("]");
      return builder.toString();
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
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("(");
      builder.append(decimalLiteral);
      builder.append(")");
      return builder.toString();
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
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("(");
      builder.append(first);
      builder.append(",");
      builder.append(second);
      builder.append(")");
      return builder.toString();
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
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("(");
      builder.append(first);
      if (second != null) {
        builder.append(", ");
        builder.append(second);
      }
      builder.append(")");
      return builder.toString();
    }
  }
}
