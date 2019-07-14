package com.spike.giantdataanalysis.model.logic.relational.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAttributeTypeEnum;

/**
 * 常量属性.
 */
public class RelationalConstantAttribute extends RelationalAttribute {

  RelationalConstantAttribute(String name, RelationalAttributeTypeEnum dataType) {
    super(name, dataType, true);
  }

  @Override
  public int compareTo(RelationalAttribute o) {
    if (o == null) {
      return 1;
    } else {
      return compareToName(o.name());
    }
  }

  @SuppressWarnings("deprecation")
  private int compareToName(String otherName) {
    switch (dataType) {
    case DECIMAL:
    case NEWDECIMAL:
      return new BigDecimal(name).compareTo(new BigDecimal(otherName));
    case TINY:
    case SHORT:
      return new Short(name).compareTo(new Short(otherName));
    case LONG:
      return new Long(name).compareTo(new Long(otherName));
    case FLOAT:
      return new Float(name).compareTo(new Float(otherName));
    case DOUBLE:
      return new Double(name).compareTo(new Double(otherName));
    case NULL:
      return 0;
    case TIMESTAMP:
    case DATE:
    case TIME:
    case DATETIME:
    case YEAR:
    case NEWDATE:
    case TIMESTAMP2:
    case DATETIME2:
    case TIME2:
      return new Date(name).compareTo(new Date(otherName));
    case LONGLONG:
      return new BigInteger(name).compareTo(new BigInteger(otherName));
    case INT24:
      return new Integer(name).compareTo(new Integer(otherName));
    case VARCHAR:
      return name.compareTo(otherName);
    case BIT:
      return new Boolean(name).compareTo(new Boolean(otherName));
    case JSON:
      return name.compareTo(otherName);
    case ENUM:
      return name.compareTo(otherName);
    case SET:
      return name.compareTo(otherName);
    case TINY_BLOB:
    case MEDIUM_BLOB:
    case LONG_BLOB:
    case BLOB:
      return name.compareTo(otherName);
    case VAR_STRING:
    case STRING:
    case GEOMETRY:
      return name.compareTo(otherName);
    default:
      return 0;
    }
  }

}
