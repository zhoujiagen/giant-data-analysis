package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraExpressionAtom.Constant;

/**
 * 关系代数原始表达式.
 */
public interface RelationalAlgebraPrimitiveExpression {

  public static class FullColumnName implements RelationalAlgebraPrimitiveExpression {
    final Uid uid;
    final List<DottedId> dottedIds;

    FullColumnName(Uid uid, List<DottedId> dottedIds) {
      this.uid = uid;
      this.dottedIds = dottedIds;
    }
  }

  // uidList: uid (',' uid)*
  public static class UidList implements RelationalAlgebraPrimitiveExpression {
    final List<Uid> uids;

    UidList(List<Uid> uids) {
      Preconditions.checkArgument(uids != null && uids.size() > 0);

      this.uids = uids;
    }
  }

  public static class Uid implements RelationalAlgebraPrimitiveExpression {
    public static enum UidType {
      SIMPLE_ID, REVERSE_QUOTE_ID, CHARSET_REVERSE_QOUTE_STRING
    }

    final UidType type;
    final String literal;

    Uid(UidType type, String literal) {
      this.type = type;
      this.literal = literal;
    }
  }

  public static class SimpleId implements RelationalAlgebraPrimitiveExpression {
    public static enum SimpleIdType {
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

    final SimpleIdType type;
    final String literal;

    SimpleId(SimpleIdType type, String literal) {
      this.type = type;
      this.literal = literal;
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

  public static class DottedId implements RelationalAlgebraPrimitiveExpression {
    final String literal;

    DottedId(String literal) {
      this.literal = literal;
    }
  }

  public static interface FunctionCall extends RelationalAlgebraPrimitiveExpression {
  }

  // specificFunction #specificFunctionCall
  public static interface SpecificFunctionCall extends FunctionCall {
  }

  /// ( CURRENT_DATE | CURRENT_TIME | CURRENT_TIMESTAMP | CURRENT_USER | LOCALTIME)
  /// #simpleFunctionCall
  public static class SimpleFunctionCall implements SpecificFunctionCall {
    public static enum Type {
      CURRENT_DATE, CURRENT_TIME, CURRENT_TIMESTAMP, CURRENT_USER, LOCALTIME
    }

    final Type type;

    SimpleFunctionCall(Type type) {
      this.type = type;
    }
  }

  /// CONVERT '(' expression separator=',' convertedDataType ')' #dataTypeFunctionCall
  public static class DataTypeFunctionCall implements SpecificFunctionCall {
    public static enum Type {
      // CONVERT '(' expression separator=',' convertedDataType ')' #dataTypeFunctionCall
      CONVERT_DATATYPE,
      // CONVERT '(' expression USING charsetName ')' #dataTypeFunctionCall
      CONVERT_CHARSET,
      // CAST '(' expression AS convertedDataType ')' #dataTypeFunctionCall
      CAST
    }

    final Type type;
    final RelationalAlgebraConditionalExpression expression;
    final ConvertedDataType convertedDataType;
    final CharsetName charsetName;

    DataTypeFunctionCall(Type type, RelationalAlgebraConditionalExpression expression,
        CharsetName charsetName) {
      Preconditions.checkArgument(Type.CONVERT_CHARSET.equals(type));
      Preconditions.checkArgument(expression != null);
      Preconditions.checkArgument(charsetName != null);

      this.type = type;
      this.expression = expression;
      this.convertedDataType = null;
      this.charsetName = charsetName;
    }

    DataTypeFunctionCall(Type type, RelationalAlgebraConditionalExpression expression,
        ConvertedDataType convertedDataType) {
      Preconditions.checkArgument(Type.CONVERT_DATATYPE.equals(type) || Type.CAST.equals(type));
      Preconditions.checkArgument(expression != null);
      Preconditions.checkArgument(convertedDataType != null);

      this.type = type;
      this.expression = expression;
      this.convertedDataType = convertedDataType;
      this.charsetName = null;
    }
  }

  // charsetName: BINARY | charsetNameBase | STRING_LITERAL | CHARSET_REVERSE_QOUTE_STRING
  public static class CharsetName implements RelationalAlgebraPrimitiveExpression {
    public static enum Type {
      BINARY, CHARSET_NAME_BASE, STRING_LITERAL, CHARSET_REVERSE_QOUTE_STRING
    }

    final Type type;
    final String value;

    CharsetName(Type type, String value) {
      Preconditions.checkArgument(type != null);
      Preconditions.checkArgument(value != null);

      this.type = type;
      this.value = value;
    }
  }

  // convertedDataType
  public static class ConvertedDataType implements RelationalAlgebraPrimitiveExpression {
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

    final Type type;
    final LengthOneDimension lengthOneDimension;
    final CharsetNameBaseEnum charsetName;
    final LengthTwoDimension lengthTwoDimension;
    final Boolean signed;

    /** Type.BINARY, Type.NCHAR */
    ConvertedDataType(Type type, LengthOneDimension lengthOneDimension) {
      Preconditions.checkArgument(Type.BINARY.equals(type) || Type.NCHAR.equals(type));

      this.type = type;
      this.lengthOneDimension = lengthOneDimension;
      this.charsetName = null;
      this.lengthTwoDimension = null;
      this.signed = null;
    }

    /** Type.CHAR */
    ConvertedDataType(Type type, LengthOneDimension lengthOneDimension,
        CharsetNameBaseEnum charsetName) {
      Preconditions.checkArgument(Type.CHAR.equals(type));

      this.type = type;
      this.lengthOneDimension = lengthOneDimension;
      this.charsetName = charsetName;
      this.lengthTwoDimension = null;
      this.signed = null;
    }

    /** Type.DATE,Type.DATETIME,Type.TIME */
    ConvertedDataType(Type type) {
      Preconditions.checkArgument(
        Type.DATE.equals(type) || Type.DATETIME.equals(type) || Type.TIME.equals(type));

      this.type = type;
      this.lengthOneDimension = null;
      this.charsetName = null;
      this.lengthTwoDimension = null;
      this.signed = null;
    }

    /** Type.DECIMAL */
    ConvertedDataType(Type type, LengthTwoDimension lengthTwoDimension) {
      Preconditions.checkArgument(Type.DECIMAL.equals(type));

      this.type = type;
      this.lengthOneDimension = null;
      this.charsetName = null;
      this.lengthTwoDimension = lengthTwoDimension;
      this.signed = null;
    }

    /** Type.INTEGER */
    ConvertedDataType(Type type, boolean signed) {
      Preconditions.checkArgument(Type.DECIMAL.equals(type));

      this.type = type;
      this.lengthOneDimension = null;
      this.charsetName = null;
      this.lengthTwoDimension = null;
      this.signed = signed;
    }
  }

  // lengthOneDimension
  public static class LengthOneDimension implements RelationalAlgebraPrimitiveExpression {
    final DecimalLiteral decimalLiteral;

    LengthOneDimension(DecimalLiteral decimalLiteral) {
      this.decimalLiteral = decimalLiteral;
    }
  }

  // lengthTwoDimension
  public static class LengthTwoDimension implements RelationalAlgebraPrimitiveExpression {
    final DecimalLiteral first;
    final DecimalLiteral second;

    public LengthTwoDimension(DecimalLiteral first, DecimalLiteral second) {
      this.first = first;
      this.second = second;
    }
  }

  // lengthTwoOptionalDimension
  public static class LengthTwoOptionalDimension implements RelationalAlgebraPrimitiveExpression {
    final DecimalLiteral first;
    final DecimalLiteral second; // may be null

    public LengthTwoOptionalDimension(DecimalLiteral first, DecimalLiteral second) {
      this.first = first;
      this.second = second;
    }
  }

  public static class DecimalLiteral implements RelationalAlgebraPrimitiveExpression {
    public static enum Type {
      DECIMAL_LITERAL, // [0-9]+
      ZERO_DECIMAL, // 0
      ONE_DECIMAL, // 1
      TWO_DECIMAL; // 2
    }

    final Type type;
    final String literal;

    DecimalLiteral(Type type, String literal) {
      this.type = type;
      this.literal = literal;
    }
  }

  /// VALUES '(' fullColumnName ')' #valuesFunctionCall
  public static class ValuesFunctionCall implements SpecificFunctionCall {
    final FullColumnName fullColumnName;

    ValuesFunctionCall(FullColumnName fullColumnName) {
      Preconditions.checkArgument(fullColumnName != null);

      this.fullColumnName = fullColumnName;
    }
  }

  /// caseFunctionCall:
  // CASE expression caseFuncAlternative+ (ELSE elseArg=functionArg)? END
  // CASE caseFuncAlternative+ (ELSE elseArg=functionArg)? END
  public static class CaseFunctionCall implements SpecificFunctionCall {
    RelationalAlgebraConditionalExpression expression;
    final List<CaseFuncAlternative> caseFuncAlternatives;
    FunctionArg functionArg;

    CaseFunctionCall(RelationalAlgebraConditionalExpression expression,
        List<CaseFuncAlternative> caseFuncAlternatives, FunctionArg functionArg) {
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
  }

  // caseFuncAlternative: WHEN condition=functionArg THEN consequent=functionArg
  public static class CaseFuncAlternative implements RelationalAlgebraPrimitiveExpression {
    final FunctionArg condition;
    final FunctionArg consequent;

    CaseFuncAlternative(FunctionArg condition, FunctionArg consequent) {
      super();
      this.condition = condition;
      this.consequent = consequent;
    }
  }

  // functionArg: constant | fullColumnName | functionCall | expression
  public static class FunctionArg implements RelationalAlgebraPrimitiveExpression {
    public static enum Type {
      CONSTANT, FULL_COLUMN_NAME, FUNCTION_CALL, EXPRESSION
    }

    final Type type;
    Constant constant;
    FullColumnName fullColumnName;
    FunctionCall functionCall;
    RelationalAlgebraConditionalExpression expression;

    FunctionArg(Type type, Object value) {
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
        expression = (RelationalAlgebraConditionalExpression) value;
        break;
      default:
        throw new UnsupportedOperationException();
      }
    }

  }

  /// CHAR '(' functionArgs (USING charsetName)? ')' #charFunctionCall
  public static class CharFunctionCall implements SpecificFunctionCall {
    final FunctionArgs functionArgs;
    final CharsetName charsetName;

    CharFunctionCall(FunctionArgs functionArgs, CharsetName charsetName) {
      Preconditions.checkArgument(functionArgs != null);

      this.functionArgs = functionArgs;
      this.charsetName = charsetName;
    }
  }

  public static class FunctionArgs implements SpecificFunctionCall {
    final List<FunctionArg> functionArgs;

    FunctionArgs(List<FunctionArg> functionArgs) {
      Preconditions.checkArgument(functionArgs != null && functionArgs.size() > 0);

      this.functionArgs = functionArgs;
    }
  }

  /**
   * <pre>
   POSITION
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
      ')'                         #positionFunctionCall
   * </pre>
   */
  public static class PositionFunctionCall implements SpecificFunctionCall {
    final String positionString;
    final RelationalAlgebraConditionalExpression positionExpression;
    final String inString;
    final RelationalAlgebraConditionalExpression inExpression;

    PositionFunctionCall(String positionString,
        RelationalAlgebraConditionalExpression positionExpression, String inString,
        RelationalAlgebraConditionalExpression inExpression) {
      Preconditions.checkArgument(!(positionString == null && positionExpression == null));
      Preconditions.checkArgument(!(inString == null && inExpression == null));

      this.positionString = positionString;
      this.positionExpression = positionExpression;
      this.inString = inString;
      this.inExpression = inExpression;
    }
  }

  /**
   * <pre>
   (SUBSTR | SUBSTRING)
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
      ')'                      #substrFunctionCall
   * </pre>
   */
  public static class SubstrFunctionCall implements SpecificFunctionCall {
    final String sourceString;
    final RelationalAlgebraConditionalExpression sourceExpression;
    final DecimalLiteral fromDecimal;
    final RelationalAlgebraConditionalExpression fromExpression;
    final DecimalLiteral forDecimal;
    final RelationalAlgebraConditionalExpression forExpression;

    SubstrFunctionCall(//
        String sourceString, RelationalAlgebraConditionalExpression sourceExpression, //
        DecimalLiteral fromDecimal, RelationalAlgebraConditionalExpression fromExpression, //
        DecimalLiteral forDecimal, RelationalAlgebraConditionalExpression forExpression//
    ) {
      Preconditions.checkArgument(!(sourceString == null && sourceExpression == null));
      Preconditions.checkArgument(!(fromDecimal == null && fromExpression == null));
      Preconditions.checkArgument(!(forDecimal == null && forExpression == null));

      this.sourceString = sourceString;
      this.sourceExpression = sourceExpression;
      this.fromDecimal = fromDecimal;
      this.fromExpression = fromExpression;
      this.forDecimal = forDecimal;
      this.forExpression = forExpression;
    }

  }

  /// trimFunctionCall
  /**
   * <pre>
   TRIM
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
      ')'
   * </pre>
   */
  public static class TrimFunctionCall implements SpecificFunctionCall {
    public static enum PositioinFormType {
      BOTH, LEADING, TRAILING
    }

    final PositioinFormType type;
    final StringLiteral sourceString;
    final RelationalAlgebraConditionalExpression sourceExpression;
    final StringLiteral fromString;
    final RelationalAlgebraConditionalExpression fromExpression;

    TrimFunctionCall(PositioinFormType type, //
        StringLiteral sourceString, RelationalAlgebraConditionalExpression sourceExpression, //
        StringLiteral fromString, RelationalAlgebraConditionalExpression fromExpression//
    ) {
      Preconditions.checkArgument(type != null);
      Preconditions.checkArgument(!(sourceString == null && sourceExpression == null));
      Preconditions.checkArgument(!(fromString == null && fromExpression == null));

      this.type = type;
      this.sourceString = sourceString;
      this.sourceExpression = sourceExpression;
      this.fromString = fromString;
      this.fromExpression = fromExpression;
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
  public static class StringLiteral implements RelationalAlgebraPrimitiveExpression {
    final CharsetNameBaseEnum stringCharsetName;
    final String stringLiteral;
    final String startNationalStringLiteral;
    final List<String> stringLiterals;
    final CollationName collationName;

    StringLiteral(CharsetNameBaseEnum stringCharsetName, String stringLiteral,
        String startNationalStringLiteral, List<String> stringLiterals,
        CollationName collationName) {
      this.stringCharsetName = stringCharsetName;
      this.stringLiteral = stringLiteral;
      this.startNationalStringLiteral = startNationalStringLiteral;
      this.stringLiterals = stringLiterals;
      this.collationName = collationName;
    }
  }

  // collationName: uid | STRING_LITERAL
  public static class CollationName implements RelationalAlgebraPrimitiveExpression {
    final Uid uid;
    final String stringLiteral;

    CollationName(Uid uid, String stringLiteral) {
      Preconditions.checkArgument(!(uid == null && stringLiteral == null));

      this.uid = uid;
      this.stringLiteral = stringLiteral;
    }

  }

  /**
   * <pre>
  WEIGHT_STRING
      '('
        (stringLiteral | expression)
        (AS stringFormat=(CHAR | BINARY)
        '(' decimalLiteral ')' )?  levelsInWeightString?
      ')'                           #weightFunctionCall
   * </pre>
   */
  public static class WeightFunctionCall implements SpecificFunctionCall {
    public static enum StringFormatType {
      CHAR, BINARY
    };

    final StringLiteral stringLiteral;
    final RelationalAlgebraConditionalExpression expression;
    final StringFormatType type;
    final DecimalLiteral decimalLiteral;
    final LevelsInWeightString levelsInWeightString;

    WeightFunctionCall(StringLiteral stringLiteral,
        RelationalAlgebraConditionalExpression expression, StringFormatType type,
        DecimalLiteral decimalLiteral, LevelsInWeightString levelsInWeightString) {
      this.stringLiteral = stringLiteral;
      this.expression = expression;
      this.type = type;
      this.decimalLiteral = decimalLiteral;
      this.levelsInWeightString = levelsInWeightString;
    }
  }

  // levelsInWeightString
  public static interface LevelsInWeightString extends RelationalAlgebraPrimitiveExpression {
  }

  // LEVEL levelInWeightListElement (',' levelInWeightListElement)* #levelWeightList
  public static class LevelWeightList implements LevelsInWeightString {
    final List<LevelInWeightListElement> levelInWeightListElements;

    LevelWeightList(List<LevelInWeightListElement> levelInWeightListElements) {
      Preconditions.checkArgument(levelInWeightListElements != null //
          && levelInWeightListElements.size() > 0);
      this.levelInWeightListElements = levelInWeightListElements;
    }
  }

  // LEVEL firstLevel=decimalLiteral '-' lastLevel=decimalLiteral #levelWeightRange
  public static class LevelWeightRange implements LevelsInWeightString {
    final DecimalLiteral firstLevel;
    final DecimalLiteral lastLevel;

    LevelWeightRange(DecimalLiteral firstLevel, DecimalLiteral lastLevel) {
      Preconditions.checkArgument(firstLevel != null);
      Preconditions.checkArgument(lastLevel != null);

      this.firstLevel = firstLevel;
      this.lastLevel = lastLevel;
    }
  }

  // levelInWeightListElement: decimalLiteral orderType=(ASC | DESC | REVERSE)?
  public static class LevelInWeightListElement implements RelationalAlgebraPrimitiveExpression {
    public static enum OrderType {
      ASC, DESC, REVERSE
    }

    final DecimalLiteral decimalLiteral;
    final OrderType orderType;

    LevelInWeightListElement(DecimalLiteral decimalLiteral, OrderType orderType) {
      Preconditions.checkArgument(decimalLiteral != null);

      this.decimalLiteral = decimalLiteral;
      this.orderType = orderType;
    }
  }

  /**
   * <pre>
   EXTRACT
      '('
        intervalType
        FROM
        (
          sourceString=stringLiteral
          | sourceExpression=expression
        )
      ')'                    #extractFunctionCall
   * </pre>
   */
  public static class ExtractFunctionCall implements SpecificFunctionCall {
    final IntervalType intervalType;
    final StringLiteral sourceString;
    final RelationalAlgebraConditionalExpression sourceExpression;

    ExtractFunctionCall(IntervalType intervalType, StringLiteral sourceString,
        RelationalAlgebraConditionalExpression sourceExpression) {
      Preconditions.checkArgument(intervalType != null);
      Preconditions.checkArgument(!(sourceString == null && sourceExpression == null));

      this.intervalType = intervalType;
      this.sourceString = sourceString;
      this.sourceExpression = sourceExpression;
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
  public static class IntervalType implements RelationalAlgebraPrimitiveExpression {
    public static enum Type {
      INTERVAL_TYPE_BASE, YEAR, YEAR_MONTH, DAY_HOUR, DAY_MINUTE, DAY_SECOND, HOUR_MINUTE,
      HOUR_SECOND, MINUTE_SECOND, SECOND_MICROSECOND, MINUTE_MICROSECOND, HOUR_MICROSECOND,
      DAY_MICROSECOND
    }

    final Type type;
    final IntervalTypeBaseEnum intervalTypeBase;

    IntervalType(Type type, IntervalTypeBaseEnum intervalTypeBase) {
      this.type = type;
      if (Type.INTERVAL_TYPE_BASE.equals(type)) {
        Preconditions.checkArgument(intervalTypeBase != null);
        this.intervalTypeBase = intervalTypeBase;
      } else {
        this.intervalTypeBase = null;
      }
    }
  }

  /**
   * <pre>
  GET_FORMAT
      '('
        datetimeFormat=(DATE | TIME | DATETIME)
        ',' stringLiteral
      ')'      #getFormatFunctionCall
   * </pre>
   */
  public static class GetFormatFunctionCall implements SpecificFunctionCall {
    public static enum DatetimeFormatType {
      DATE, TIME, DATETIME
    }

    final DatetimeFormatType type;
    final StringLiteral stringLiteral;

    GetFormatFunctionCall(DatetimeFormatType type, StringLiteral stringLiteral) {
      Preconditions.checkArgument(type != null);
      Preconditions.checkArgument(stringLiteral != null);

      this.type = type;
      this.stringLiteral = stringLiteral;
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
    public static enum Type {
      AVG, MAX, MIN, SUM, //
      COUNT, COUNT_DISTINCT, //
      BIT_AND, BIT_OR, BIT_XOR, STD, STDDEV, STDDEV_POP, STDDEV_SAMP, VAR_POP, VAR_SAMP, VARIANCE, //
      GROUP_CONCAT;
    }

    final Type type;
    final AggregatorEnum aggregator;
    final FunctionArg functionArg;
    final FunctionArgs functionArgs;
    final List<OrderByExpression> orderByExpression;
    final String separator;

    AggregateWindowedFunction(Type type, AggregatorEnum aggregator, FunctionArg functionArg,
        FunctionArgs functionArgs, List<OrderByExpression> orderByExpression, String separator) {
      Preconditions.checkArgument(type != null);
      this.type = type;
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
      default:
        throw new UnsupportedOperationException();
      }

      this.aggregator = aggregator;
      this.functionArg = functionArg;
      this.functionArgs = functionArgs;
      this.orderByExpression = orderByExpression;
      this.separator = separator;
    }

  }

  // orderByExpression : expression order=(ASC | DESC)?
  public static class OrderByExpression implements RelationalAlgebraPrimitiveExpression {
    public static enum OrderType {
      ASC, DESC
    }

    final RelationalAlgebraConditionalExpression expression;
    final OrderType order;

    OrderByExpression(RelationalAlgebraConditionalExpression expression, OrderType order) {
      Preconditions.checkArgument(expression != null);

      this.expression = expression;
      this.order = order;
    }
  }

  public static enum AggregatorEnum {
    ALL, DISTINCT
  }

  // scalarFunctionName '(' functionArgs? ')' #scalarFunctionCall
  public static class ScalarFunctionCall implements FunctionCall {
    public static enum Type {
      FUNCTION_NAME_BASE, //
      ASCII, CURDATE, CURRENT_DATE, CURRENT_TIME, CURRENT_TIMESTAMP, CURTIME, DATE_ADD, DATE_SUB,
      IF, INSERT, LOCALTIME, LOCALTIMESTAMP, MID, NOW, REPLACE, SUBSTR, SUBSTRING, SYSDATE, TRIM,
      UTC_DATE, UTC_TIME, UTC_TIMESTAMP
    }

    final Type type;
    final FunctionNameBaseEnum functionNameBase;
    final FunctionArgs functionArgs;

    ScalarFunctionCall(Type type, FunctionNameBaseEnum functionNameBase,
        FunctionArgs functionArgs) {
      Preconditions.checkArgument(type != null);
      this.type = type;

      if (Type.FUNCTION_NAME_BASE.equals(type)) {
        Preconditions.checkArgument(functionNameBase != null);
        this.functionNameBase = functionNameBase;
      } else {
        this.functionNameBase = null;
      }
      this.functionArgs = functionArgs;
    }
  }

  // fullId '(' functionArgs? ')' #udfFunctionCall
  public static class UdfFunctionCall implements FunctionCall {
    final FullId fullId;
    final FunctionArgs functionArgs;

    UdfFunctionCall(FullId fullId, FunctionArgs functionArgs) {
      this.fullId = fullId;
      this.functionArgs = functionArgs;
    }
  }

  public static class FullId implements RelationalAlgebraPrimitiveExpression {
    final Uid uid;
    final String dotId;
    final Uid secondUid;

    FullId(Uid uid, String dotId, Uid secondUid) {
      this.uid = uid;
      this.dotId = dotId;
      this.secondUid = secondUid;
    }
  }

  // passwordFunctionClause : functionName=(PASSWORD | OLD_PASSWORD) '(' functionArg ')'
  public static class PasswordFunctionCall implements FunctionCall {
    public static enum Type {
      PASSWORD, OLD_PASSWORD
    }

    final Type type;
    final FunctionArg functionArg;

    PasswordFunctionCall(Type type, FunctionArg functionArg) {
      this.type = type;
      this.functionArg = functionArg;
    }
  }
}
