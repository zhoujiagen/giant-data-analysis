package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;

/**
 * 关系代数原始表达式.
 */
public interface PrimitiveExpression extends RelationalAlgebraExpression {

  // uidList: uid (',' uid)*
  public static class UidList implements PrimitiveExpression {
    public final List<Uid> uids;

    UidList(List<Uid> uids) {
      Preconditions.checkArgument(uids != null && uids.size() > 0);

      this.uids = uids;
    }

    @Override
    public String toString() {
      return Joiner.on(", ").join(uids);
    }
  }

  public static class Uid implements PrimitiveExpression {
    public static enum Type {
      SIMPLE_ID, REVERSE_QUOTE_ID, CHARSET_REVERSE_QOUTE_STRING
    }

    public final Uid.Type type;
    public final String literal;

    Uid(Uid.Type type, String literal) {
      Preconditions.checkArgument(type != null);
      Preconditions.checkArgument(literal != null);

      this.type = type;
      this.literal = literal;
    }

    @Override
    public String toString() {
      return literal;
    }
  }

  public static class SimpleId implements PrimitiveExpression {
    public static enum Type {
      ID, //
      CHARSET_NAME_BASE, //
      TRANSACTION_LEVEL_BASE, //
      ENGINE_NAME, //
      PRIVILEGES_BASE, //
      INTERVAL_TYPE_BASE, //
      DATA_TYPE_BASE, //
      KEYWORDS_CAN_BE_ID, //
      FUNCTION_NAME_BASE;
    }

    public final SimpleId.Type type;
    public final String literal;

    SimpleId(SimpleId.Type type, String literal) {
      this.type = type;
      this.literal = literal;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("SimpleId [type=");
      builder.append(type);
      builder.append(", literal=");
      builder.append(literal);
      builder.append("]");
      return builder.toString();
    }

  }

  public static enum CharsetNameBaseEnum {
    ARMSCII8, ASCII, BIG5, CP1250, CP1251, CP1256, CP1257, //
    CP850, CP852, CP866, CP932, DEC8, EUCJPMS, EUCKR, GB2312, //
    GBK, GEOSTD8, GREEK, HEBREW, HP8, KEYBCS2, KOI8R, KOI8U, //
    LATIN1, LATIN2, LATIN5, LATIN7, MACCE, MACROMAN, SJIS, //
    SWE7, TIS620, UCS2, UJIS, UTF16, UTF16LE, UTF32, UTF8, //
    UTF8MB3, UTF8MB4;
  }

  public static enum TransactionLevelBaseEnum {
    REPEATABLE, COMMITTED, UNCOMMITTED, SERIALIZABLE
  }

  public static enum PrivilegesBaseEnum {
    TABLES, ROUTINE, EXECUTE, FILE, PROCESS, //
    RELOAD, SHUTDOWN, SUPER, PRIVILEGES
  }

  public static enum IntervalTypeBaseEnum {
    QUARTER, MONTH, DAY, HOUR, //
    MINUTE, WEEK, SECOND, MICROSECOND;
  }

  public static enum DataTypeBaseEnum {
    DATE, TIME, TIMESTAMP, DATETIME, YEAR, ENUM, TEXT
  }

  public static enum KeywordsCanBeIdEnum {
    ACCOUNT, ACTION, AFTER, AGGREGATE, ALGORITHM, ANY, AT, AUTHORS, AUTOCOMMIT, AUTOEXTEND_SIZE,
    AUTO_INCREMENT, AVG_ROW_LENGTH, BEGIN, BINLOG, BIT, BLOCK, BOOL, BOOLEAN, BTREE, CACHE,
    CASCADED, CHAIN, CHANGED, CHANNEL, CHECKSUM, PAGE_CHECKSUM, CIPHER, CLIENT, CLOSE, COALESCE,
    CODE, COLUMNS, COLUMN_FORMAT, COMMENT, COMMIT, COMPACT, COMPLETION, COMPRESSED, COMPRESSION,
    CONCURRENT, CONNECTION, CONSISTENT, CONTAINS, CONTEXT, CONTRIBUTORS, COPY, CPU, DATA, DATAFILE,
    DEALLOCATE, DEFAULT_AUTH, DEFINER, DELAY_KEY_WRITE, DES_KEY_FILE, DIRECTORY, DISABLE, DISCARD,
    DISK, DO, DUMPFILE, DUPLICATE, DYNAMIC, ENABLE, ENCRYPTION, END, ENDS, ENGINE, ENGINES, ERROR,
    ERRORS, ESCAPE, EVEN, EVENT, EVENTS, EVERY, EXCHANGE, EXCLUSIVE, EXPIRE, EXPORT, EXTENDED,
    EXTENT_SIZE, FAST, FAULTS, FIELDS, FILE_BLOCK_SIZE, FILTER, FIRST, FIXED, FLUSH, FOLLOWS, FOUND,
    FULL, FUNCTION, GENERAL, GLOBAL, GRANTS, GROUP_REPLICATION, HANDLER, HASH, HELP, HOST, HOSTS,
    IDENTIFIED, IGNORE_SERVER_IDS, IMPORT, INDEXES, INITIAL_SIZE, INPLACE, INSERT_METHOD, INSTALL,
    INSTANCE, INTERNAL, INVOKER, IO, IO_THREAD, IPC, ISOLATION, ISSUER, JSON, KEY_BLOCK_SIZE,
    LANGUAGE, LAST, LEAVES, LESS, LEVEL, LIST, LOCAL, LOGFILE, LOGS, MASTER, MASTER_AUTO_POSITION,
    MASTER_CONNECT_RETRY, MASTER_DELAY, MASTER_HEARTBEAT_PERIOD, MASTER_HOST, MASTER_LOG_FILE,
    MASTER_LOG_POS, MASTER_PASSWORD, MASTER_PORT, MASTER_RETRY_COUNT, MASTER_SSL, MASTER_SSL_CA,
    MASTER_SSL_CAPATH, MASTER_SSL_CERT, MASTER_SSL_CIPHER, MASTER_SSL_CRL, MASTER_SSL_CRLPATH,
    MASTER_SSL_KEY, MASTER_TLS_VERSION, MASTER_USER, MAX_CONNECTIONS_PER_HOUR, MAX_QUERIES_PER_HOUR,
    MAX_ROWS, MAX_SIZE, MAX_UPDATES_PER_HOUR, MAX_USER_CONNECTIONS, MEDIUM, MEMORY, MERGE, MID,
    MIGRATE, MIN_ROWS, MODE, MODIFY, MUTEX, MYSQL, NAME, NAMES, NCHAR, NEVER, NEXT, NO, NODEGROUP,
    NONE, OFFLINE, OFFSET, OJ, OLD_PASSWORD, ONE, ONLINE, ONLY, OPEN, OPTIMIZER_COSTS, OPTIONS,
    OWNER, PACK_KEYS, PAGE, PARSER, PARTIAL, PARTITIONING, PARTITIONS, PASSWORD, PHASE, PLUGINS,
    PLUGIN_DIR, PLUGIN, PORT, PRECEDES, PREPARE, PRESERVE, PREV, PROCESSLIST, PROFILE, PROFILES,
    PROXY, QUERY, QUICK, REBUILD, RECOVER, REDO_BUFFER_SIZE, REDUNDANT, RELAY, RELAYLOG,
    RELAY_LOG_FILE, RELAY_LOG_POS, REMOVE, REORGANIZE, REPAIR, REPLICATE_DO_DB, REPLICATE_DO_TABLE,
    REPLICATE_IGNORE_DB, REPLICATE_IGNORE_TABLE, REPLICATE_REWRITE_DB, REPLICATE_WILD_DO_TABLE,
    REPLICATE_WILD_IGNORE_TABLE, REPLICATION, RESET, RESUME, RETURNS, ROLLBACK, ROLLUP, ROTATE, ROW,
    ROWS, ROW_FORMAT, SAVEPOINT, SCHEDULE, SECURITY, SERIAL, SERVER, SESSION, SHARE, SHARED, SIGNED,
    SIMPLE, SLAVE, SLOW, SNAPSHOT, SOCKET, SOME, SONAME, SOUNDS, SOURCE, SQL_AFTER_GTIDS,
    SQL_AFTER_MTS_GAPS, SQL_BEFORE_GTIDS, SQL_BUFFER_RESULT, SQL_CACHE, SQL_NO_CACHE, SQL_THREAD,
    START, STARTS, STATS_AUTO_RECALC, STATS_PERSISTENT, STATS_SAMPLE_PAGES, STATUS, STOP, STORAGE,
    STRING, SUBJECT, SUBPARTITION, SUBPARTITIONS, SUSPEND, SWAPS, SWITCHES, TABLESPACE, TEMPORARY,
    TEMPTABLE, THAN, TRADITIONAL, TRANSACTION, TRIGGERS, TRUNCATE, UNDEFINED, UNDOFILE,
    UNDO_BUFFER_SIZE, UNINSTALL, UNKNOWN, UNTIL, UPGRADE, USER, USE_FRM, USER_RESOURCES, VALIDATION,
    VALUE, VARIABLES, VIEW, WAIT, WARNINGS, WITHOUT, WORK, WRAPPER, X509, XA, XML;
  }

  public static enum FunctionNameBaseEnum {
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
    YEARWEEK, Y_FUNCTION, X_FUNCTION;
  }

  // dottedId : DOT_ID | '.' uid
  public static class DottedId implements PrimitiveExpression {
    public final String dotId;
    public final Uid uid;

    DottedId(String dotId, Uid uid) {
      Preconditions.checkArgument(!(dotId == null && uid == null));

      this.dotId = dotId;
      this.uid = uid;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("DottedId [dotId=");
      builder.append(dotId);
      builder.append(", uid=");
      builder.append(uid);
      builder.append("]");
      return builder.toString();
    }

  }

  // charsetName: BINARY | charsetNameBase | STRING_LITERAL | CHARSET_REVERSE_QOUTE_STRING
  public static class CharsetName implements PrimitiveExpression {
    public static enum Type {
      BINARY, CHARSET_NAME_BASE, STRING_LITERAL, CHARSET_REVERSE_QOUTE_STRING
    }

    public final CharsetName.Type type;
    public final String value;

    CharsetName(CharsetName.Type type, String value) {
      Preconditions.checkArgument(type != null);
      Preconditions.checkArgument(value != null);

      this.type = type;
      this.value = value;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("CharsetName [type=");
      builder.append(type);
      builder.append(", value=");
      builder.append(value);
      builder.append("]");
      return builder.toString();
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
  public static class ConvertedDataType implements PrimitiveExpression {
    public static enum Type {
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

  // lengthOneDimension : '(' decimalLiteral ')'
  public static class LengthOneDimension implements PrimitiveExpression {
    public final DecimalLiteral decimalLiteral;

    LengthOneDimension(DecimalLiteral decimalLiteral) {
      Preconditions.checkArgument(decimalLiteral != null);

      this.decimalLiteral = decimalLiteral;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("LengthOneDimension [decimalLiteral=");
      builder.append(decimalLiteral);
      builder.append("]");
      return builder.toString();
    }

  }

  // lengthTwoDimension : '(' decimalLiteral ',' decimalLiteral ')'
  public static class LengthTwoDimension implements PrimitiveExpression {
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
      builder.append("LengthTwoDimension [first=");
      builder.append(first);
      builder.append(", second=");
      builder.append(second);
      builder.append("]");
      return builder.toString();
    }
  }

  // lengthTwoOptionalDimension : '(' decimalLiteral (',' decimalLiteral)? ')'
  public static class LengthTwoOptionalDimension implements PrimitiveExpression {
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
      builder.append("LengthTwoOptionalDimension [first=");
      builder.append(first);
      builder.append(", second=");
      builder.append(second);
      builder.append("]");
      return builder.toString();
    }
  }

  public static abstract class RelationAlgebraLiteral implements PrimitiveExpression {
    public abstract String literal();

    @Override
    public String toString() {
      return literal();
    }
  }

  // decimalLiteral : DECIMAL_LITERAL | ZERO_DECIMAL | ONE_DECIMAL | TWO_DECIMAL
  public static class DecimalLiteral extends RelationAlgebraLiteral {
    public static enum Type {
      DECIMAL_LITERAL, // [0-9]+
      ZERO_DECIMAL, // 0
      ONE_DECIMAL, // 1
      TWO_DECIMAL; // 2
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
  public static class StringLiteral extends RelationAlgebraLiteral {
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

  // collationName: uid | STRING_LITERAL
  public static class CollationName implements PrimitiveExpression {
    public final Uid uid;
    public final String stringLiteral;

    CollationName(Uid uid, String stringLiteral) {
      Preconditions.checkArgument(!(uid == null && stringLiteral == null));

      this.uid = uid;
      this.stringLiteral = stringLiteral;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("CollationName [uid=");
      builder.append(uid);
      builder.append(", stringLiteral=");
      builder.append(stringLiteral);
      builder.append("]");
      return builder.toString();
    }

  }

  // hexadecimalLiteral: STRING_CHARSET_NAME? HEXADECIMAL_LITERAL;
  public static class HexadecimalLiteral extends RelationAlgebraLiteral {
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

    @Override
    public String toString() {
      return literal();
    }
  }

  // booleanLiteral: TRUE | FALSE;
  public static class BooleanLiteral extends RelationAlgebraLiteral {
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

  // nullLiteral=(NULL_LITERAL | NULL_SPEC_LITERAL)
  public static class NullLiteral extends RelationAlgebraLiteral {
    public final String literal;

    NullLiteral(String literal) {
      Preconditions.checkArgument(literal != null);

      this.literal = literal;
    }

    @Override
    public String literal() {
      return literal;
    }

  }

  // levelsInWeightString
  public static interface LevelsInWeightString extends PrimitiveExpression {
  }

  // LEVEL levelInWeightListElement (',' levelInWeightListElement)* #levelWeightList
  public static class LevelWeightList implements LevelsInWeightString {
    public final List<LevelInWeightListElement> levelInWeightListElements;

    LevelWeightList(List<LevelInWeightListElement> levelInWeightListElements) {
      Preconditions.checkArgument(levelInWeightListElements != null //
          && levelInWeightListElements.size() > 0);
      this.levelInWeightListElements = levelInWeightListElements;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("LevelWeightList [levelInWeightListElements=");
      builder.append(levelInWeightListElements);
      builder.append("]");
      return builder.toString();
    }

  }

  // LEVEL firstLevel=decimalLiteral '-' lastLevel=decimalLiteral #levelWeightRange
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
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("LevelWeightRange [firstLevel=");
      builder.append(firstLevel);
      builder.append(", lastLevel=");
      builder.append(lastLevel);
      builder.append("]");
      return builder.toString();
    }
  }

  // levelInWeightListElement: decimalLiteral orderType=(ASC | DESC | REVERSE)?
  public static class LevelInWeightListElement implements PrimitiveExpression {
    public static enum OrderType {
      ASC, DESC, REVERSE
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
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("LevelInWeightListElement [decimalLiteral=");
      builder.append(decimalLiteral);
      builder.append(", orderType=");
      builder.append(orderType);
      builder.append("]");
      return builder.toString();
    }

  }

  /**
   * <pre>
   intervalType
    : intervalTypeBase
    | YEAR | YEAR_MONTH | DAY_HOUR | DAY_MINUTE
    | DAY_SECOND | HOUR_MINUTE | HOUR_SECOND | MINUTE_SECOND
    | SECOND_MICROSECOND | MINUTE_MICROSECOND
    | HOUR_MICROSECOND | DAY_MICROSECOND
    ;
   * </pre>
   */
  public static class IntervalType implements PrimitiveExpression {
    public static enum Type {
      INTERVAL_TYPE_BASE, YEAR, YEAR_MONTH, DAY_HOUR, DAY_MINUTE, DAY_SECOND, HOUR_MINUTE,
      HOUR_SECOND, MINUTE_SECOND, SECOND_MICROSECOND, MINUTE_MICROSECOND, HOUR_MICROSECOND,
      DAY_MICROSECOND
    }

    public final IntervalType.Type type;
    public final IntervalTypeBaseEnum intervalTypeBase;

    IntervalType(IntervalType.Type type, IntervalTypeBaseEnum intervalTypeBase) {
      this.type = type;
      if (Type.INTERVAL_TYPE_BASE.equals(type)) {
        Preconditions.checkArgument(intervalTypeBase != null);
        this.intervalTypeBase = intervalTypeBase;
      } else {
        this.intervalTypeBase = null;
      }
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("IntervalType [type=");
      builder.append(type);
      builder.append(", intervalTypeBase=");
      builder.append(intervalTypeBase);
      builder.append("]");
      return builder.toString();
    }

  }

  // orderByExpression : expression order=(ASC | DESC)?
  public static class OrderByExpression implements PrimitiveExpression {
    public static enum OrderType {
      ASC, DESC
    }

    public final Expression expression;
    public final OrderByExpression.OrderType order;

    OrderByExpression(Expression expression, OrderByExpression.OrderType order) {
      Preconditions.checkArgument(expression != null);

      this.expression = expression;
      this.order = order;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("OrderByExpression [expression=");
      builder.append(expression);
      builder.append(", order=");
      builder.append(order);
      builder.append("]");
      return builder.toString();
    }

  }

  // fullId: uid (DOT_ID | '.' uid)?
  public static class FullId implements PrimitiveExpression {
    public final List<Uid> uids;
    public final String dotId;

    FullId(List<Uid> uids, String dotId) {
      Preconditions.checkArgument(uids != null && uids.size() > 0);

      this.uids = uids;
      this.dotId = dotId;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append(uids.get(0));
      if (dotId != null) {
        builder.append(dotId);
      }
      if (uids.size() > 1) {
        builder.append(".");
        builder.append(uids.get(1));
      }
      return builder.toString();
    }

  }

}
