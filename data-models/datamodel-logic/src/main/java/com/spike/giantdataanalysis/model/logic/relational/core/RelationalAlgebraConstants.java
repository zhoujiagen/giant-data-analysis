package com.spike.giantdataanalysis.model.logic.relational.core;

/**
 * 关系代数常量.
 */
public interface RelationalAlgebraConstants {

  // ---------------------------------------------------------------------------
  // SKIP
  // ---------------------------------------------------------------------------
  String SPACE = "[ \t\r\n]+";
  String SPEC_MYSQL_COMMENT = "/*!' .+? '*/";
  String COMMENT_INPUT = "/*' .*? '*/";
  String LINE_COMMENT = "( ('-- ' | '#') ~[\r\n]* ('\r'? '\n' | EOF)  | '--' ('\r'? '\n' | EOF) )";

  // ---------------------------------------------------------------------------
  // Fragments for Literal primitives
  // ---------------------------------------------------------------------------
  String CHARSET_NAME = "ARMSCII8|ASCII|BIG5|BINARY|CP1250" //
      + "|CP1251|CP1256|CP1257|CP850" //
      + "|CP852|CP866|CP932|DEC8|EUCJPMS" //
      + "|EUCKR|GB2312|GBK|GEOSTD8|GREEK" //
      + "|HEBREW|HP8|KEYBCS2|KOI8R|KOI8U" //
      + "|LATIN1|LATIN2|LATIN5|LATIN7" //
      + "|MACCE|MACROMAN|SJIS|SWE7|TIS620" //
      + "|UCS2|UJIS|UTF16|UTF16LE|UTF32" //
      + "|UTF8|UTF8MB3|UTF8MB4";
  // fragment EXPONENT_NUM_PART: 'E' [-+]? DEC_DIGIT+;
  String EXPONENT_NUM_PART = "E[-+]?[0-9]+";
  // fragment ID_LITERAL: [A-Z_$0-9]*?[A-Z_$]+?[A-Z_$0-9]*;
  String ID_LITERAL = "[A-Z_$0-9]*?[A-Z_$]+?[A-Z_$0-9]*";
  // fragment DQUOTA_STRING: '"' ( '\\'. | '""' | ~('"'| '\\') )* '"';
  String DQUOTA_STRING = "\"(.|\"\")*\"";
  // fragment SQUOTA_STRING: '\'' ('\\'. | '\'\'' | ~('\'' | '\\'))* '\'';
  String SQUOTA_STRING = "'(.|'')*'";
  // fragment BQUOTA_STRING: '`' ( '\\'. | '``' | ~('`'|'\\'))* '`';
  String BQUOTA_STRING = "`(.|``)*`";
  // fragment HEX_DIGIT: [0-9A-F];
  String HEX_DIGIT = "[0-9A-F]";
  // fragment DEC_DIGIT: [0-9];
  String DEC_DIGIT = "[0-9]";
  // fragment BIT_STRING_L: 'B' '\'' [01]+ '\'';
  String BIT_STRING_L = "B'[01]+'";

  // ---------------------------------------------------------------------------
  // Common Keywords
  // ---------------------------------------------------------------------------
  String ADD = "ADD";
  String ALL = "ALL";
  String ALTER = "ALTER";
  String ALWAYS = "ALWAYS";
  String ANALYZE = "ANALYZE";
  String AND = "AND";
  String AS = "AS";
  String ASC = "ASC";
  String BEFORE = "BEFORE";
  String BETWEEN = "BETWEEN";
  String BOTH = "BOTH";
  String BY = "BY";
  String CALL = "CALL";
  String CASCADE = "CASCADE";
  String CASE = "CASE";
  String CAST = "CAST";
  String CHANGE = "CHANGE";
  String CHARACTER = "CHARACTER";
  String CHECK = "CHECK";
  String COLLATE = "COLLATE";
  String COLUMN = "COLUMN";
  String CONDITION = "CONDITION";
  String CONSTRAINT = "CONSTRAINT";
  String CONTINUE = "CONTINUE";
  String CONVERT = "CONVERT";
  String CREATE = "CREATE";
  String CROSS = "CROSS";
  String CURRENT_USER = "CURRENT_USER";
  String CURSOR = "CURSOR";
  String DATABASE = "DATABASE";
  String DATABASES = "DATABASES";
  String DECLARE = "DECLARE";
  String DEFAULT = "DEFAULT";
  String DELAYED = "DELAYED";
  String DELETE = "DELETE";
  String DESC = "DESC";
  String DESCRIBE = "DESCRIBE";
  String DETERMINISTIC = "DETERMINISTIC";
  String DISTINCT = "DISTINCT";
  String DISTINCTROW = "DISTINCTROW";
  String DROP = "DROP";
  String EACH = "EACH";
  String ELSE = "ELSE";
  String ELSEIF = "ELSEIF";
  String ENCLOSED = "ENCLOSED";
  String ESCAPED = "ESCAPED";
  String EXISTS = "EXISTS";
  String EXIT = "EXIT";
  String EXPLAIN = "EXPLAIN";
  String FALSE = "FALSE";
  String FETCH = "FETCH";
  String FOR = "FOR";
  String FORCE = "FORCE";
  String FOREIGN = "FOREIGN";
  String FROM = "FROM";
  String FULLTEXT = "FULLTEXT";
  String GENERATED = "GENERATED";
  String GRANT = "GRANT";
  String GROUP = "GROUP";
  String HAVING = "HAVING";
  String HIGH_PRIORITY = "HIGH_PRIORITY";
  String IF = "IF";
  String IGNORE = "IGNORE";
  String IN = "IN";
  String INDEX = "INDEX";
  String INFILE = "INFILE";
  String INNER = "INNER";
  String INOUT = "INOUT";
  String INSERT = "INSERT";
  String INTERVAL = "INTERVAL";
  String INTO = "INTO";
  String IS = "IS";
  String ITERATE = "ITERATE";
  String JOIN = "JOIN";
  String KEY = "KEY";
  String KEYS = "KEYS";
  String KILL = "KILL";
  String LEADING = "LEADING";
  String LEAVE = "LEAVE";
  String LEFT = "LEFT";
  String LIKE = "LIKE";
  String LIMIT = "LIMIT";
  String LINEAR = "LINEAR";
  String LINES = "LINES";
  String LOAD = "LOAD";
  String LOCK = "LOCK";
  String LOOP = "LOOP";
  String LOW_PRIORITY = "LOW_PRIORITY";
  String MASTER_BIND = "MASTER_BIND";
  String MASTER_SSL_VERIFY_SERVER_CERT = "MASTER_SSL_VERIFY_SERVER_CERT";
  String MATCH = "MATCH";
  String MAXVALUE = "MAXVALUE";
  String MODIFIES = "MODIFIES";
  String NATURAL = "NATURAL";
  String NOT = "NOT";
  String NO_WRITE_TO_BINLOG = "NO_WRITE_TO_BINLOG";
  String NULL_LITERAL = "NULL";
  String ON = "ON";
  String OPTIMIZE = "OPTIMIZE";
  String OPTION = "OPTION";
  String OPTIONALLY = "OPTIONALLY";
  String OR = "OR";
  String ORDER = "ORDER";
  String OUT = "OUT";
  String OUTER = "OUTER";
  String OUTFILE = "OUTFILE";
  String PARTITION = "PARTITION";
  String PRIMARY = "PRIMARY";
  String PROCEDURE = "PROCEDURE";
  String PURGE = "PURGE";
  String RANGE = "RANGE";
  String READ = "READ";
  String READS = "READS";
  String REFERENCES = "REFERENCES";
  String REGEXP = "REGEXP";
  String RELEASE = "RELEASE";
  String RENAME = "RENAME";
  String REPEAT = "REPEAT";
  String REPLACE = "REPLACE";
  String REQUIRE = "REQUIRE";
  String RESTRICT = "RESTRICT";
  String RETURN = "RETURN";
  String REVOKE = "REVOKE";
  String RIGHT = "RIGHT";
  String RLIKE = "RLIKE";
  String SCHEMA = "SCHEMA";
  String SCHEMAS = "SCHEMAS";
  String SELECT = "SELECT";
  String SET = "SET";
  String SEPARATOR = "SEPARATOR";
  String SHOW = "SHOW";
  String SPATIAL = "SPATIAL";
  String SQL = "SQL";
  String SQLEXCEPTION = "SQLEXCEPTION";
  String SQLSTATE = "SQLSTATE";
  String SQLWARNING = "SQLWARNING";
  String SQL_BIG_RESULT = "SQL_BIG_RESULT";
  String SQL_CALC_FOUND_ROWS = "SQL_CALC_FOUND_ROWS";
  String SQL_SMALL_RESULT = "SQL_SMALL_RESULT";
  String SSL = "SSL";
  String STARTING = "STARTING";
  String STRAIGHT_JOIN = "STRAIGHT_JOIN";
  String TABLE = "TABLE";
  String TERMINATED = "TERMINATED";
  String THEN = "THEN";
  String TO = "TO";
  String TRAILING = "TRAILING";
  String TRIGGER = "TRIGGER";
  String TRUE = "TRUE";
  String UNDO = "UNDO";
  String UNION = "UNION";
  String UNIQUE = "UNIQUE";
  String UNLOCK = "UNLOCK";
  String UNSIGNED = "UNSIGNED";
  String UPDATE = "UPDATE";
  String USAGE = "USAGE";
  String USE = "USE";
  String USING = "USING";
  String VALUES = "VALUES";
  String WHEN = "WHEN";
  String WHERE = "WHERE";
  String WHILE = "WHILE";
  String WITH = "WITH";
  String WRITE = "WRITE";
  String XOR = "XOR";
  String ZEROFILL = "ZEROFILL";

  // ---------------------------------------------------------------------------
  // DATA TYPE Keywords
  // ---------------------------------------------------------------------------
  String TINYINT = "TINYINT";
  String SMALLINT = "SMALLINT";
  String MEDIUMINT = "MEDIUMINT";
  String INT = "INT";
  String INTEGER = "INTEGER";
  String BIGINT = "BIGINT";
  String REAL = "REAL";
  String DOUBLE = "DOUBLE";
  String PRECISION = "PRECISION";
  String FLOAT = "FLOAT";
  String DECIMAL = "DECIMAL";
  String DEC = "DEC";
  String NUMERIC = "NUMERIC";
  String DATE = "DATE";
  String TIME = "TIME";
  String TIMESTAMP = "TIMESTAMP";
  String DATETIME = "DATETIME";
  String YEAR = "YEAR";
  String CHAR = "CHAR";
  String VARCHAR = "VARCHAR";
  String NVARCHAR = "NVARCHAR";
  String NATIONAL = "NATIONAL";
  String BINARY = "BINARY";
  String VARBINARY = "VARBINARY";
  String TINYBLOB = "TINYBLOB";
  String BLOB = "BLOB";
  String MEDIUMBLOB = "MEDIUMBLOB";
  String LONGBLOB = "LONGBLOB";
  String TINYTEXT = "TINYTEXT";
  String TEXT = "TEXT";
  String MEDIUMTEXT = "MEDIUMTEXT";
  String LONGTEXT = "LONGTEXT";
  String ENUM = "ENUM";
  String VARYING = "VARYING";
  String SERIAL = "SERIAL";

  // ---------------------------------------------------------------------------
  // Interval type Keywords
  // ---------------------------------------------------------------------------
  String YEAR_MONTH = "YEAR_MONTH";
  String DAY_HOUR = "DAY_HOUR";
  String DAY_MINUTE = "DAY_MINUTE";
  String DAY_SECOND = "DAY_SECOND";
  String HOUR_MINUTE = "HOUR_MINUTE";
  String HOUR_SECOND = "HOUR_SECOND";
  String MINUTE_SECOND = "MINUTE_SECOND";
  String SECOND_MICROSECOND = "SECOND_MICROSECOND";
  String MINUTE_MICROSECOND = "MINUTE_MICROSECOND";
  String HOUR_MICROSECOND = "HOUR_MICROSECOND";
  String DAY_MICROSECOND = "DAY_MICROSECOND";

  // ---------------------------------------------------------------------------
  // Group function Keywords
  // ---------------------------------------------------------------------------
  String AVG = "AVG";
  String BIT_AND = "BIT_AND";
  String BIT_OR = "BIT_OR";
  String BIT_XOR = "BIT_XOR";
  String COUNT = "COUNT";
  String GROUP_CONCAT = "GROUP_CONCAT";
  String MAX = "MAX";
  String MIN = "MIN";
  String STD = "STD";
  String STDDEV = "STDDEV";
  String STDDEV_POP = "STDDEV_POP";
  String STDDEV_SAMP = "STDDEV_SAMP";
  String SUM = "SUM";
  String VAR_POP = "VAR_POP";
  String VAR_SAMP = "VAR_SAMP";
  String VARIANCE = "VARIANCE";

  // ---------------------------------------------------------------------------
  // Common function Keywords
  // ---------------------------------------------------------------------------
  String CURRENT_DATE = "CURRENT_DATE";
  String CURRENT_TIME = "CURRENT_TIME";
  String CURRENT_TIMESTAMP = "CURRENT_TIMESTAMP";
  String LOCALTIME = "LOCALTIME";
  String CURDATE = "CURDATE";
  String CURTIME = "CURTIME";
  String DATE_ADD = "DATE_ADD";
  String DATE_SUB = "DATE_SUB";
  String EXTRACT = "EXTRACT";
  String LOCALTIMESTAMP = "LOCALTIMESTAMP";
  String NOW = "NOW";
  String POSITION = "POSITION";
  String SUBSTR = "SUBSTR";
  String SUBSTRING = "SUBSTRING";
  String SYSDATE = "SYSDATE";
  String TRIM = "TRIM";
  String UTC_DATE = "UTC_DATE";
  String UTC_TIME = "UTC_TIME";
  String UTC_TIMESTAMP = "UTC_TIMESTAMP";

  // ---------------------------------------------------------------------------
  // Common Keywords, but can be ID
  // ---------------------------------------------------------------------------
  String ACCOUNT = "ACCOUNT";
  String ACTION = "ACTION";
  String AFTER = "AFTER";
  String AGGREGATE = "AGGREGATE";
  String ALGORITHM = "ALGORITHM";
  String ANY = "ANY";
  String AT = "AT";
  String AUTHORS = "AUTHORS";
  String AUTOCOMMIT = "AUTOCOMMIT";
  String AUTOEXTEND_SIZE = "AUTOEXTEND_SIZE";
  String AUTO_INCREMENT = "AUTO_INCREMENT";
  String AVG_ROW_LENGTH = "AVG_ROW_LENGTH";
  String BEGIN = "BEGIN";
  String BINLOG = "BINLOG";
  String BIT = "BIT";
  String BLOCK = "BLOCK";
  String BOOL = "BOOL";
  String BOOLEAN = "BOOLEAN";
  String BTREE = "BTREE";
  String CACHE = "CACHE";
  String CASCADED = "CASCADED";
  String CHAIN = "CHAIN";
  String CHANGED = "CHANGED";
  String CHANNEL = "CHANNEL";
  String CHECKSUM = "CHECKSUM";
  String PAGE_CHECKSUM = "PAGE_CHECKSUM";
  String CIPHER = "CIPHER";
  String CLIENT = "CLIENT";
  String CLOSE = "CLOSE";
  String COALESCE = "COALESCE";
  String CODE = "CODE";
  String COLUMNS = "COLUMNS";
  String COLUMN_FORMAT = "COLUMN_FORMAT";
  String COMMENT = "COMMENT";
  String COMMIT = "COMMIT";
  String COMPACT = "COMPACT";
  String COMPLETION = "COMPLETION";
  String COMPRESSED = "COMPRESSED";
  String COMPRESSION = "COMPRESSION";
  String CONCURRENT = "CONCURRENT";
  String CONNECTION = "CONNECTION";
  String CONSISTENT = "CONSISTENT";
  String CONTAINS = "CONTAINS";
  String CONTEXT = "CONTEXT";
  String CONTRIBUTORS = "CONTRIBUTORS";
  String COPY = "COPY";
  String CPU = "CPU";
  String DATA = "DATA";
  String DATAFILE = "DATAFILE";
  String DEALLOCATE = "DEALLOCATE";
  String DEFAULT_AUTH = "DEFAULT_AUTH";
  String DEFINER = "DEFINER";
  String DELAY_KEY_WRITE = "DELAY_KEY_WRITE";
  String DES_KEY_FILE = "DES_KEY_FILE";
  String DIRECTORY = "DIRECTORY";
  String DISABLE = "DISABLE";
  String DISCARD = "DISCARD";
  String DISK = "DISK";
  String DO = "DO";
  String DUMPFILE = "DUMPFILE";
  String DUPLICATE = "DUPLICATE";
  String DYNAMIC = "DYNAMIC";
  String ENABLE = "ENABLE";
  String ENCRYPTION = "ENCRYPTION";
  String END = "END";
  String ENDS = "ENDS";
  String ENGINE = "ENGINE";
  String ENGINES = "ENGINES";
  String ERROR = "ERROR";
  String ERRORS = "ERRORS";
  String ESCAPE = "ESCAPE";
  String EVEN = "EVEN";
  String EVENT = "EVENT";
  String EVENTS = "EVENTS";
  String EVERY = "EVERY";
  String EXCHANGE = "EXCHANGE";
  String EXCLUSIVE = "EXCLUSIVE";
  String EXPIRE = "EXPIRE";
  String EXPORT = "EXPORT";
  String EXTENDED = "EXTENDED";
  String EXTENT_SIZE = "EXTENT_SIZE";
  String FAST = "FAST";
  String FAULTS = "FAULTS";
  String FIELDS = "FIELDS";
  String FILE_BLOCK_SIZE = "FILE_BLOCK_SIZE";
  String FILTER = "FILTER";
  String FIRST = "FIRST";
  String FIXED = "FIXED";
  String FLUSH = "FLUSH";
  String FOLLOWS = "FOLLOWS";
  String FOUND = "FOUND";
  String FULL = "FULL";
  String FUNCTION = "FUNCTION";
  String GENERAL = "GENERAL";
  String GLOBAL = "GLOBAL";
  String GRANTS = "GRANTS";
  String GROUP_REPLICATION = "GROUP_REPLICATION";
  String HANDLER = "HANDLER";
  String HASH = "HASH";
  String HELP = "HELP";
  String HOST = "HOST";
  String HOSTS = "HOSTS";
  String IDENTIFIED = "IDENTIFIED";
  String IGNORE_SERVER_IDS = "IGNORE_SERVER_IDS";
  String IMPORT = "IMPORT";
  String INDEXES = "INDEXES";
  String INITIAL_SIZE = "INITIAL_SIZE";
  String INPLACE = "INPLACE";
  String INSERT_METHOD = "INSERT_METHOD";
  String INSTALL = "INSTALL";
  String INSTANCE = "INSTANCE";
  String INVOKER = "INVOKER";
  String IO = "IO";
  String IO_THREAD = "IO_THREAD";
  String IPC = "IPC";
  String ISOLATION = "ISOLATION";
  String ISSUER = "ISSUER";
  String JSON = "JSON";
  String KEY_BLOCK_SIZE = "KEY_BLOCK_SIZE";
  String LANGUAGE = "LANGUAGE";
  String LAST = "LAST";
  String LEAVES = "LEAVES";
  String LESS = "LESS";
  String LEVEL = "LEVEL";
  String LIST = "LIST";
  String LOCAL = "LOCAL";
  String LOGFILE = "LOGFILE";
  String LOGS = "LOGS";
  String MASTER = "MASTER";
  String MASTER_AUTO_POSITION = "MASTER_AUTO_POSITION";
  String MASTER_CONNECT_RETRY = "MASTER_CONNECT_RETRY";
  String MASTER_DELAY = "MASTER_DELAY";
  String MASTER_HEARTBEAT_PERIOD = "MASTER_HEARTBEAT_PERIOD";
  String MASTER_HOST = "MASTER_HOST";
  String MASTER_LOG_FILE = "MASTER_LOG_FILE";
  String MASTER_LOG_POS = "MASTER_LOG_POS";
  String MASTER_PASSWORD = "MASTER_PASSWORD";
  String MASTER_PORT = "MASTER_PORT";
  String MASTER_RETRY_COUNT = "MASTER_RETRY_COUNT";
  String MASTER_SSL = "MASTER_SSL";
  String MASTER_SSL_CA = "MASTER_SSL_CA";
  String MASTER_SSL_CAPATH = "MASTER_SSL_CAPATH";
  String MASTER_SSL_CERT = "MASTER_SSL_CERT";
  String MASTER_SSL_CIPHER = "MASTER_SSL_CIPHER";
  String MASTER_SSL_CRL = "MASTER_SSL_CRL";
  String MASTER_SSL_CRLPATH = "MASTER_SSL_CRLPATH";
  String MASTER_SSL_KEY = "MASTER_SSL_KEY";
  String MASTER_TLS_VERSION = "MASTER_TLS_VERSION";
  String MASTER_USER = "MASTER_USER";
  String MAX_CONNECTIONS_PER_HOUR = "MAX_CONNECTIONS_PER_HOUR";
  String MAX_QUERIES_PER_HOUR = "MAX_QUERIES_PER_HOUR";
  String MAX_ROWS = "MAX_ROWS";
  String MAX_SIZE = "MAX_SIZE";
  String MAX_UPDATES_PER_HOUR = "MAX_UPDATES_PER_HOUR";
  String MAX_USER_CONNECTIONS = "MAX_USER_CONNECTIONS";
  String MEDIUM = "MEDIUM";
  String MERGE = "MERGE";
  String MID = "MID";
  String MIGRATE = "MIGRATE";
  String MIN_ROWS = "MIN_ROWS";
  String MODE = "MODE";
  String MODIFY = "MODIFY";
  String MUTEX = "MUTEX";
  String MYSQL = "MYSQL";
  String NAME = "NAME";
  String NAMES = "NAMES";
  String NCHAR = "NCHAR";
  String NEVER = "NEVER";
  String NEXT = "NEXT";
  String NO = "NO";
  String NODEGROUP = "NODEGROUP";
  String NONE = "NONE";
  String OFFLINE = "OFFLINE";
  String OFFSET = "OFFSET";
  String OJ = "OJ";
  String OLD_PASSWORD = "OLD_PASSWORD";
  String ONE = "ONE";
  String ONLINE = "ONLINE";
  String ONLY = "ONLY";
  String OPEN = "OPEN";
  String OPTIMIZER_COSTS = "OPTIMIZER_COSTS";
  String OPTIONS = "OPTIONS";
  String OWNER = "OWNER";
  String PACK_KEYS = "PACK_KEYS";
  String PAGE = "PAGE";
  String PARSER = "PARSER";
  String PARTIAL = "PARTIAL";
  String PARTITIONING = "PARTITIONING";
  String PARTITIONS = "PARTITIONS";
  String PASSWORD = "PASSWORD";
  String PHASE = "PHASE";
  String PLUGIN = "PLUGIN";
  String PLUGIN_DIR = "PLUGIN_DIR";
  String PLUGINS = "PLUGINS";
  String PORT = "PORT";
  String PRECEDES = "PRECEDES";
  String PREPARE = "PREPARE";
  String PRESERVE = "PRESERVE";
  String PREV = "PREV";
  String PROCESSLIST = "PROCESSLIST";
  String PROFILE = "PROFILE";
  String PROFILES = "PROFILES";
  String PROXY = "PROXY";
  String QUERY = "QUERY";
  String QUICK = "QUICK";
  String REBUILD = "REBUILD";
  String RECOVER = "RECOVER";
  String REDO_BUFFER_SIZE = "REDO_BUFFER_SIZE";
  String REDUNDANT = "REDUNDANT";
  String RELAY = "RELAY";
  String RELAY_LOG_FILE = "RELAY_LOG_FILE";
  String RELAY_LOG_POS = "RELAY_LOG_POS";
  String RELAYLOG = "RELAYLOG";
  String REMOVE = "REMOVE";
  String REORGANIZE = "REORGANIZE";
  String REPAIR = "REPAIR";
  String REPLICATE_DO_DB = "REPLICATE_DO_DB";
  String REPLICATE_DO_TABLE = "REPLICATE_DO_TABLE";
  String REPLICATE_IGNORE_DB = "REPLICATE_IGNORE_DB";
  String REPLICATE_IGNORE_TABLE = "REPLICATE_IGNORE_TABLE";
  String REPLICATE_REWRITE_DB = "REPLICATE_REWRITE_DB";
  String REPLICATE_WILD_DO_TABLE = "REPLICATE_WILD_DO_TABLE";
  String REPLICATE_WILD_IGNORE_TABLE = "REPLICATE_WILD_IGNORE_TABLE";
  String REPLICATION = "REPLICATION";
  String RESET = "RESET";
  String RESUME = "RESUME";
  String RETURNS = "RETURNS";
  String ROLLBACK = "ROLLBACK";
  String ROLLUP = "ROLLUP";
  String ROTATE = "ROTATE";
  String ROW = "ROW";
  String ROWS = "ROWS";
  String ROW_FORMAT = "ROW_FORMAT";
  String SAVEPOINT = "SAVEPOINT";
  String SCHEDULE = "SCHEDULE";
  String SECURITY = "SECURITY";
  String SERVER = "SERVER";
  String SESSION = "SESSION";
  String SHARE = "SHARE";
  String SHARED = "SHARED";
  String SIGNED = "SIGNED";
  String SIMPLE = "SIMPLE";
  String SLAVE = "SLAVE";
  String SLOW = "SLOW";
  String SNAPSHOT = "SNAPSHOT";
  String SOCKET = "SOCKET";
  String SOME = "SOME";
  String SONAME = "SONAME";
  String SOUNDS = "SOUNDS";
  String SOURCE = "SOURCE";
  String SQL_AFTER_GTIDS = "SQL_AFTER_GTIDS";
  String SQL_AFTER_MTS_GAPS = "SQL_AFTER_MTS_GAPS";
  String SQL_BEFORE_GTIDS = "SQL_BEFORE_GTIDS";
  String SQL_BUFFER_RESULT = "SQL_BUFFER_RESULT";
  String SQL_CACHE = "SQL_CACHE";
  String SQL_NO_CACHE = "SQL_NO_CACHE";
  String SQL_THREAD = "SQL_THREAD";
  String START = "START";
  String STARTS = "STARTS";
  String STATS_AUTO_RECALC = "STATS_AUTO_RECALC";
  String STATS_PERSISTENT = "STATS_PERSISTENT";
  String STATS_SAMPLE_PAGES = "STATS_SAMPLE_PAGES";
  String STATUS = "STATUS";
  String STOP = "STOP";
  String STORAGE = "STORAGE";
  String STORED = "STORED";
  String STRING = "STRING";
  String SUBJECT = "SUBJECT";
  String SUBPARTITION = "SUBPARTITION";
  String SUBPARTITIONS = "SUBPARTITIONS";
  String SUSPEND = "SUSPEND";
  String SWAPS = "SWAPS";
  String SWITCHES = "SWITCHES";
  String TABLESPACE = "TABLESPACE";
  String TEMPORARY = "TEMPORARY";
  String TEMPTABLE = "TEMPTABLE";
  String THAN = "THAN";
  String TRADITIONAL = "TRADITIONAL";
  String TRANSACTION = "TRANSACTION";
  String TRIGGERS = "TRIGGERS";
  String TRUNCATE = "TRUNCATE";
  String UNDEFINED = "UNDEFINED";
  String UNDOFILE = "UNDOFILE";
  String UNDO_BUFFER_SIZE = "UNDO_BUFFER_SIZE";
  String UNINSTALL = "UNINSTALL";
  String UNKNOWN = "UNKNOWN";
  String UNTIL = "UNTIL";
  String UPGRADE = "UPGRADE";
  String USER = "USER";
  String USE_FRM = "USE_FRM";
  String USER_RESOURCES = "USER_RESOURCES";
  String VALIDATION = "VALIDATION";
  String VALUE = "VALUE";
  String VARIABLES = "VARIABLES";
  String VIEW = "VIEW";
  String VIRTUAL = "VIRTUAL";
  String WAIT = "WAIT";
  String WARNINGS = "WARNINGS";
  String WITHOUT = "WITHOUT";
  String WORK = "WORK";
  String WRAPPER = "WRAPPER";
  String X509 = "X509";
  String XA = "XA";
  String XML = "XML";

  // ---------------------------------------------------------------------------
  // Date format Keywords
  // ---------------------------------------------------------------------------
  String EUR = "EUR";
  String USA = "USA";
  String JIS = "JIS";
  String ISO = "ISO";
  String INTERNAL = "INTERNAL";

  // ---------------------------------------------------------------------------
  // Interval type Keywords
  // ---------------------------------------------------------------------------
  String QUARTER = "QUARTER";
  String MONTH = "MONTH";
  String DAY = "DAY";
  String HOUR = "HOUR";
  String MINUTE = "MINUTE";
  String WEEK = "WEEK";
  String SECOND = "SECOND";
  String MICROSECOND = "MICROSECOND";

  // ---------------------------------------------------------------------------
  // PRIVILEGES
  // ---------------------------------------------------------------------------
  String TABLES = "TABLES";
  String ROUTINE = "ROUTINE";
  String EXECUTE = "EXECUTE";
  String FILE = "FILE";
  String PROCESS = "PROCESS";
  String RELOAD = "RELOAD";
  String SHUTDOWN = "SHUTDOWN";
  String SUPER = "SUPER";
  String PRIVILEGES = "PRIVILEGES";

  // ---------------------------------------------------------------------------
  // Charsets
  // ---------------------------------------------------------------------------
  String ARMSCII8 = "ARMSCII8";
  String ASCII = "ASCII";
  String BIG5 = "BIG5";
  String CP1250 = "CP1250";
  String CP1251 = "CP1251";
  String CP1256 = "CP1256";
  String CP1257 = "CP1257";
  String CP850 = "CP850";
  String CP852 = "CP852";
  String CP866 = "CP866";
  String CP932 = "CP932";
  String DEC8 = "DEC8";
  String EUCJPMS = "EUCJPMS";
  String EUCKR = "EUCKR";
  String GB2312 = "GB2312";
  String GBK = "GBK";
  String GEOSTD8 = "GEOSTD8";
  String GREEK = "GREEK";
  String HEBREW = "HEBREW";
  String HP8 = "HP8";
  String KEYBCS2 = "KEYBCS2";
  String KOI8R = "KOI8R";
  String KOI8U = "KOI8U";
  String LATIN1 = "LATIN1";
  String LATIN2 = "LATIN2";
  String LATIN5 = "LATIN5";
  String LATIN7 = "LATIN7";
  String MACCE = "MACCE";
  String MACROMAN = "MACROMAN";
  String SJIS = "SJIS";
  String SWE7 = "SWE7";
  String TIS620 = "TIS620";
  String UCS2 = "UCS2";
  String UJIS = "UJIS";
  String UTF16 = "UTF16";
  String UTF16LE = "UTF16LE";
  String UTF32 = "UTF32";
  String UTF8 = "UTF8";
  String UTF8MB3 = "UTF8MB3";
  String UTF8MB4 = "UTF8MB4";

  // ---------------------------------------------------------------------------
  // DB Engines
  // ---------------------------------------------------------------------------
  String ARCHIVE = "ARCHIVE";
  String BLACKHOLE = "BLACKHOLE";
  String CSV = "CSV";
  String FEDERATED = "FEDERATED";
  String INNODB = "INNODB";
  String MEMORY = "MEMORY";
  String MRG_MYISAM = "MRG_MYISAM";
  String MYISAM = "MYISAM";
  String NDB = "NDB";
  String NDBCLUSTER = "NDBCLUSTER";
  String PERFORMANCE_SCHEMA = "PERFORMANCE_SCHEMA";
  String TOKUDB = "TOKUDB";

  // ---------------------------------------------------------------------------
  // Transaction Levels
  // ---------------------------------------------------------------------------
  String REPEATABLE = "REPEATABLE";
  String COMMITTED = "COMMITTED";
  String UNCOMMITTED = "UNCOMMITTED";
  String SERIALIZABLE = "SERIALIZABLE";

  // ---------------------------------------------------------------------------
  // Spatial data types
  // ---------------------------------------------------------------------------
  String GEOMETRYCOLLECTION = "GEOMETRYCOLLECTION";
  String GEOMCOLLECTION = "GEOMCOLLECTION";
  String GEOMETRY = "GEOMETRY";
  String LINESTRING = "LINESTRING";
  String MULTILINESTRING = "MULTILINESTRING";
  String MULTIPOINT = "MULTIPOINT";
  String MULTIPOLYGON = "MULTIPOLYGON";
  String POINT = "POINT";
  String POLYGON = "POLYGON";

  // ---------------------------------------------------------------------------
  // Common function names
  // ---------------------------------------------------------------------------
  String ABS = "ABS";
  String ACOS = "ACOS";
  String ADDDATE = "ADDDATE";
  String ADDTIME = "ADDTIME";
  String AES_DECRYPT = "AES_DECRYPT";
  String AES_ENCRYPT = "AES_ENCRYPT";
  String AREA = "AREA";
  String ASBINARY = "ASBINARY";
  String ASIN = "ASIN";
  String ASTEXT = "ASTEXT";
  String ASWKB = "ASWKB";
  String ASWKT = "ASWKT";
  String ASYMMETRIC_DECRYPT = "ASYMMETRIC_DECRYPT";
  String ASYMMETRIC_DERIVE = "ASYMMETRIC_DERIVE";
  String ASYMMETRIC_ENCRYPT = "ASYMMETRIC_ENCRYPT";
  String ASYMMETRIC_SIGN = "ASYMMETRIC_SIGN";
  String ASYMMETRIC_VERIFY = "ASYMMETRIC_VERIFY";
  String ATAN = "ATAN";
  String ATAN2 = "ATAN2";
  String BENCHMARK = "BENCHMARK";
  String BIN = "BIN";
  String BIT_COUNT = "BIT_COUNT";
  String BIT_LENGTH = "BIT_LENGTH";
  String BUFFER = "BUFFER";
  String CEIL = "CEIL";
  String CEILING = "CEILING";
  String CENTROID = "CENTROID";
  String CHARACTER_LENGTH = "CHARACTER_LENGTH";
  String CHARSET = "CHARSET";
  String CHAR_LENGTH = "CHAR_LENGTH";
  String COERCIBILITY = "COERCIBILITY";
  String COLLATION = "COLLATION";
  String COMPRESS = "COMPRESS";
  String CONCAT = "CONCAT";
  String CONCAT_WS = "CONCAT_WS";
  String CONNECTION_ID = "CONNECTION_ID";
  String CONV = "CONV";
  String CONVERT_TZ = "CONVERT_TZ";
  String COS = "COS";
  String COT = "COT";
  String CRC32 = "CRC32";
  String CREATE_ASYMMETRIC_PRIV_KEY = "CREATE_ASYMMETRIC_PRIV_KEY";
  String CREATE_ASYMMETRIC_PUB_KEY = "CREATE_ASYMMETRIC_PUB_KEY";
  String CREATE_DH_PARAMETERS = "CREATE_DH_PARAMETERS";
  String CREATE_DIGEST = "CREATE_DIGEST";
  String CROSSES = "CROSSES";
  String DATEDIFF = "DATEDIFF";
  String DATE_FORMAT = "DATE_FORMAT";
  String DAYNAME = "DAYNAME";
  String DAYOFMONTH = "DAYOFMONTH";
  String DAYOFWEEK = "DAYOFWEEK";
  String DAYOFYEAR = "DAYOFYEAR";
  String DECODE = "DECODE";
  String DEGREES = "DEGREES";
  String DES_DECRYPT = "DES_DECRYPT";
  String DES_ENCRYPT = "DES_ENCRYPT";
  String DIMENSION = "DIMENSION";
  String DISJOINT = "DISJOINT";
  String ELT = "ELT";
  String ENCODE = "ENCODE";
  String ENCRYPT = "ENCRYPT";
  String ENDPOINT = "ENDPOINT";
  String ENVELOPE = "ENVELOPE";
  String EQUALS = "EQUALS";
  String EXP = "EXP";
  String EXPORT_SET = "EXPORT_SET";
  String EXTERIORRING = "EXTERIORRING";
  String EXTRACTVALUE = "EXTRACTVALUE";
  String FIELD = "FIELD";
  String FIND_IN_SET = "FIND_IN_SET";
  String FLOOR = "FLOOR";
  String FORMAT = "FORMAT";
  String FOUND_ROWS = "FOUND_ROWS";
  String FROM_BASE64 = "FROM_BASE64";
  String FROM_DAYS = "FROM_DAYS";
  String FROM_UNIXTIME = "FROM_UNIXTIME";
  String GEOMCOLLFROMTEXT = "GEOMCOLLFROMTEXT";
  String GEOMCOLLFROMWKB = "GEOMCOLLFROMWKB";
  String GEOMETRYCOLLECTIONFROMTEXT = "GEOMETRYCOLLECTIONFROMTEXT";
  String GEOMETRYCOLLECTIONFROMWKB = "GEOMETRYCOLLECTIONFROMWKB";
  String GEOMETRYFROMTEXT = "GEOMETRYFROMTEXT";
  String GEOMETRYFROMWKB = "GEOMETRYFROMWKB";
  String GEOMETRYN = "GEOMETRYN";
  String GEOMETRYTYPE = "GEOMETRYTYPE";
  String GEOMFROMTEXT = "GEOMFROMTEXT";
  String GEOMFROMWKB = "GEOMFROMWKB";
  String GET_FORMAT = "GET_FORMAT";
  String GET_LOCK = "GET_LOCK";
  String GLENGTH = "GLENGTH";
  String GREATEST = "GREATEST";
  String GTID_SUBSET = "GTID_SUBSET";
  String GTID_SUBTRACT = "GTID_SUBTRACT";
  String HEX = "HEX";
  String IFNULL = "IFNULL";
  String INET6_ATON = "INET6_ATON";
  String INET6_NTOA = "INET6_NTOA";
  String INET_ATON = "INET_ATON";
  String INET_NTOA = "INET_NTOA";
  String INSTR = "INSTR";
  String INTERIORRINGN = "INTERIORRINGN";
  String INTERSECTS = "INTERSECTS";
  String ISCLOSED = "ISCLOSED";
  String ISEMPTY = "ISEMPTY";
  String ISNULL = "ISNULL";
  String ISSIMPLE = "ISSIMPLE";
  String IS_FREE_LOCK = "IS_FREE_LOCK";
  String IS_IPV4 = "IS_IPV4";
  String IS_IPV4_COMPAT = "IS_IPV4_COMPAT";
  String IS_IPV4_MAPPED = "IS_IPV4_MAPPED";
  String IS_IPV6 = "IS_IPV6";
  String IS_USED_LOCK = "IS_USED_LOCK";
  String LAST_INSERT_ID = "LAST_INSERT_ID";
  String LCASE = "LCASE";
  String LEAST = "LEAST";
  String LENGTH = "LENGTH";
  String LINEFROMTEXT = "LINEFROMTEXT";
  String LINEFROMWKB = "LINEFROMWKB";
  String LINESTRINGFROMTEXT = "LINESTRINGFROMTEXT";
  String LINESTRINGFROMWKB = "LINESTRINGFROMWKB";
  String LN = "LN";
  String LOAD_FILE = "LOAD_FILE";
  String LOCATE = "LOCATE";
  String LOG = "LOG";
  String LOG10 = "LOG10";
  String LOG2 = "LOG2";
  String LOWER = "LOWER";
  String LPAD = "LPAD";
  String LTRIM = "LTRIM";
  String MAKEDATE = "MAKEDATE";
  String MAKETIME = "MAKETIME";
  String MAKE_SET = "MAKE_SET";
  String MASTER_POS_WAIT = "MASTER_POS_WAIT";
  String MBRCONTAINS = "MBRCONTAINS";
  String MBRDISJOINT = "MBRDISJOINT";
  String MBREQUAL = "MBREQUAL";
  String MBRINTERSECTS = "MBRINTERSECTS";
  String MBROVERLAPS = "MBROVERLAPS";
  String MBRTOUCHES = "MBRTOUCHES";
  String MBRWITHIN = "MBRWITHIN";
  String MD5 = "MD5";
  String MLINEFROMTEXT = "MLINEFROMTEXT";
  String MLINEFROMWKB = "MLINEFROMWKB";
  String MONTHNAME = "MONTHNAME";
  String MPOINTFROMTEXT = "MPOINTFROMTEXT";
  String MPOINTFROMWKB = "MPOINTFROMWKB";
  String MPOLYFROMTEXT = "MPOLYFROMTEXT";
  String MPOLYFROMWKB = "MPOLYFROMWKB";
  String MULTILINESTRINGFROMTEXT = "MULTILINESTRINGFROMTEXT";
  String MULTILINESTRINGFROMWKB = "MULTILINESTRINGFROMWKB";
  String MULTIPOINTFROMTEXT = "MULTIPOINTFROMTEXT";
  String MULTIPOINTFROMWKB = "MULTIPOINTFROMWKB";
  String MULTIPOLYGONFROMTEXT = "MULTIPOLYGONFROMTEXT";
  String MULTIPOLYGONFROMWKB = "MULTIPOLYGONFROMWKB";
  String NAME_CONST = "NAME_CONST";
  String NULLIF = "NULLIF";
  String NUMGEOMETRIES = "NUMGEOMETRIES";
  String NUMINTERIORRINGS = "NUMINTERIORRINGS";
  String NUMPOINTS = "NUMPOINTS";
  String OCT = "OCT";
  String OCTET_LENGTH = "OCTET_LENGTH";
  String ORD = "ORD";
  String OVERLAPS = "OVERLAPS";
  String PERIOD_ADD = "PERIOD_ADD";
  String PERIOD_DIFF = "PERIOD_DIFF";
  String PI = "PI";
  String POINTFROMTEXT = "POINTFROMTEXT";
  String POINTFROMWKB = "POINTFROMWKB";
  String POINTN = "POINTN";
  String POLYFROMTEXT = "POLYFROMTEXT";
  String POLYFROMWKB = "POLYFROMWKB";
  String POLYGONFROMTEXT = "POLYGONFROMTEXT";
  String POLYGONFROMWKB = "POLYGONFROMWKB";
  String POW = "POW";
  String POWER = "POWER";
  String QUOTE = "QUOTE";
  String RADIANS = "RADIANS";
  String RAND = "RAND";
  String RANDOM_BYTES = "RANDOM_BYTES";
  String RELEASE_LOCK = "RELEASE_LOCK";
  String REVERSE = "REVERSE";
  String ROUND = "ROUND";
  String ROW_COUNT = "ROW_COUNT";
  String RPAD = "RPAD";
  String RTRIM = "RTRIM";
  String SEC_TO_TIME = "SEC_TO_TIME";
  String SESSION_USER = "SESSION_USER";
  String SHA = "SHA";
  String SHA1 = "SHA1";
  String SHA2 = "SHA2";
  String SIGN = "SIGN";
  String SIN = "SIN";
  String SLEEP = "SLEEP";
  String SOUNDEX = "SOUNDEX";
  String SQL_THREAD_WAIT_AFTER_GTIDS = "SQL_THREAD_WAIT_AFTER_GTIDS";
  String SQRT = "SQRT";
  String SRID = "SRID";
  String STARTPOINT = "STARTPOINT";
  String STRCMP = "STRCMP";
  String STR_TO_DATE = "STR_TO_DATE";
  String ST_AREA = "ST_AREA";
  String ST_ASBINARY = "ST_ASBINARY";
  String ST_ASTEXT = "ST_ASTEXT";
  String ST_ASWKB = "ST_ASWKB";
  String ST_ASWKT = "ST_ASWKT";
  String ST_BUFFER = "ST_BUFFER";
  String ST_CENTROID = "ST_CENTROID";
  String ST_CONTAINS = "ST_CONTAINS";
  String ST_CROSSES = "ST_CROSSES";
  String ST_DIFFERENCE = "ST_DIFFERENCE";
  String ST_DIMENSION = "ST_DIMENSION";
  String ST_DISJOINT = "ST_DISJOINT";
  String ST_DISTANCE = "ST_DISTANCE";
  String ST_ENDPOINT = "ST_ENDPOINT";
  String ST_ENVELOPE = "ST_ENVELOPE";
  String ST_EQUALS = "ST_EQUALS";
  String ST_EXTERIORRING = "ST_EXTERIORRING";
  String ST_GEOMCOLLFROMTEXT = "ST_GEOMCOLLFROMTEXT";
  String ST_GEOMCOLLFROMTXT = "ST_GEOMCOLLFROMTXT";
  String ST_GEOMCOLLFROMWKB = "ST_GEOMCOLLFROMWKB";
  String ST_GEOMETRYCOLLECTIONFROMTEXT = "ST_GEOMETRYCOLLECTIONFROMTEXT";
  String ST_GEOMETRYCOLLECTIONFROMWKB = "ST_GEOMETRYCOLLECTIONFROMWKB";
  String ST_GEOMETRYFROMTEXT = "ST_GEOMETRYFROMTEXT";
  String ST_GEOMETRYFROMWKB = "ST_GEOMETRYFROMWKB";
  String ST_GEOMETRYN = "ST_GEOMETRYN";
  String ST_GEOMETRYTYPE = "ST_GEOMETRYTYPE";
  String ST_GEOMFROMTEXT = "ST_GEOMFROMTEXT";
  String ST_GEOMFROMWKB = "ST_GEOMFROMWKB";
  String ST_INTERIORRINGN = "ST_INTERIORRINGN";
  String ST_INTERSECTION = "ST_INTERSECTION";
  String ST_INTERSECTS = "ST_INTERSECTS";
  String ST_ISCLOSED = "ST_ISCLOSED";
  String ST_ISEMPTY = "ST_ISEMPTY";
  String ST_ISSIMPLE = "ST_ISSIMPLE";
  String ST_LINEFROMTEXT = "ST_LINEFROMTEXT";
  String ST_LINEFROMWKB = "ST_LINEFROMWKB";
  String ST_LINESTRINGFROMTEXT = "ST_LINESTRINGFROMTEXT";
  String ST_LINESTRINGFROMWKB = "ST_LINESTRINGFROMWKB";
  String ST_NUMGEOMETRIES = "ST_NUMGEOMETRIES";
  String ST_NUMINTERIORRING = "ST_NUMINTERIORRING";
  String ST_NUMINTERIORRINGS = "ST_NUMINTERIORRINGS";
  String ST_NUMPOINTS = "ST_NUMPOINTS";
  String ST_OVERLAPS = "ST_OVERLAPS";
  String ST_POINTFROMTEXT = "ST_POINTFROMTEXT";
  String ST_POINTFROMWKB = "ST_POINTFROMWKB";
  String ST_POINTN = "ST_POINTN";
  String ST_POLYFROMTEXT = "ST_POLYFROMTEXT";
  String ST_POLYFROMWKB = "ST_POLYFROMWKB";
  String ST_POLYGONFROMTEXT = "ST_POLYGONFROMTEXT";
  String ST_POLYGONFROMWKB = "ST_POLYGONFROMWKB";
  String ST_SRID = "ST_SRID";
  String ST_STARTPOINT = "ST_STARTPOINT";
  String ST_SYMDIFFERENCE = "ST_SYMDIFFERENCE";
  String ST_TOUCHES = "ST_TOUCHES";
  String ST_UNION = "ST_UNION";
  String ST_WITHIN = "ST_WITHIN";
  String ST_X = "ST_X";
  String ST_Y = "ST_Y";
  String SUBDATE = "SUBDATE";
  String SUBSTRING_INDEX = "SUBSTRING_INDEX";
  String SUBTIME = "SUBTIME";
  String SYSTEM_USER = "SYSTEM_USER";
  String TAN = "TAN";
  String TIMEDIFF = "TIMEDIFF";
  String TIMESTAMPADD = "TIMESTAMPADD";
  String TIMESTAMPDIFF = "TIMESTAMPDIFF";
  String TIME_FORMAT = "TIME_FORMAT";
  String TIME_TO_SEC = "TIME_TO_SEC";
  String TOUCHES = "TOUCHES";
  String TO_BASE64 = "TO_BASE64";
  String TO_DAYS = "TO_DAYS";
  String TO_SECONDS = "TO_SECONDS";
  String UCASE = "UCASE";
  String UNCOMPRESS = "UNCOMPRESS";
  String UNCOMPRESSED_LENGTH = "UNCOMPRESSED_LENGTH";
  String UNHEX = "UNHEX";
  String UNIX_TIMESTAMP = "UNIX_TIMESTAMP";
  String UPDATEXML = "UPDATEXML";
  String UPPER = "UPPER";
  String UUID = "UUID";
  String UUID_SHORT = "UUID_SHORT";
  String VALIDATE_PASSWORD_STRENGTH = "VALIDATE_PASSWORD_STRENGTH";
  String VERSION = "VERSION";
  String WAIT_UNTIL_SQL_THREAD_AFTER_GTIDS = "WAIT_UNTIL_SQL_THREAD_AFTER_GTIDS";
  String WEEKDAY = "WEEKDAY";
  String WEEKOFYEAR = "WEEKOFYEAR";
  String WEIGHT_STRING = "WEIGHT_STRING";
  String WITHIN = "WITHIN";
  String YEARWEEK = "YEARWEEK";
  String Y_FUNCTION = "Y";
  String X_FUNCTION = "X";

  // ---------------------------------------------------------------------------
  // Operators. Assigns
  // ---------------------------------------------------------------------------
  String VAR_ASSIGN = "==";
  String PLUS_ASSIGN = "+=";
  String MINUS_ASSIGN = "-=";
  String MULT_ASSIGN = "*=";
  String DIV_ASSIGN = "/=";
  String MOD_ASSIGN = "%=";
  String AND_ASSIGN = "&=";
  String XOR_ASSIGN = "^=";
  String OR_ASSIGN = "|=";

  // ---------------------------------------------------------------------------
  // Operators. Arithmetics
  // ---------------------------------------------------------------------------
  String STAR = "*";
  String DIVIDE = "/";
  String MODULE = "%";
  String PLUS = "+";
  String MINUSMINUS = "--";
  String MINUS = "-";
  String DIV = "DIV";
  String MOD = "MOD";

  // ---------------------------------------------------------------------------
  // Operators. Comparation
  // ---------------------------------------------------------------------------
  String EQUAL_SYMBOL = "=";
  String GREATER_SYMBOL = ">";
  String LESS_SYMBOL = "<";
  String EXCLAMATION_SYMBOL = "!";

  // ---------------------------------------------------------------------------
  // Operators. Bit
  // ---------------------------------------------------------------------------
  String BIT_NOT_OP = "~";
  String BIT_OR_OP = "|";
  String BIT_AND_OP = "&";
  String BIT_XOR_OP = "^";

  // ---------------------------------------------------------------------------
  // Constructors symbols
  // ---------------------------------------------------------------------------
  String DOT = ".";
  String LR_BRACKET = "(";
  String RR_BRACKET = ")";
  String COMMA = ",";
  String SEMI = ";";
  String AT_SIGN = "@";
  String ZERO_DECIMAL = "0";
  String ONE_DECIMAL = "1";
  String TWO_DECIMAL = "2";
  String SINGLE_QUOTE_SYMB = "'";
  String DOUBLE_QUOTE_SYMB = "\"";
  String REVERSE_QUOTE_SYMB = "`";
  String COLON_SYMB = "=";

  // ---------------------------------------------------------------------------
  // Charsets
  // ---------------------------------------------------------------------------
  String CHARSET_REVERSE_QOUTE_STRING = "`" + CHARSET_NAME + "`";

  // ---------------------------------------------------------------------------
  // File's sizes
  // ---------------------------------------------------------------------------
  String FILESIZE_LITERAL = DEC_DIGIT + ('K' | 'M' | 'G' | 'T');

  // ---------------------------------------------------------------------------
  // Literal Primitives
  // ---------------------------------------------------------------------------

  // START_NATIONAL_STRING_LITERAL: 'N' SQUOTA_STRING;
  String START_NATIONAL_STRING_LITERAL = "N" + SQUOTA_STRING;
  // STRING_LITERAL: DQUOTA_STRING | SQUOTA_STRING | BQUOTA_STRING;
  String STRING_LITERAL = DQUOTA_STRING + "|" + SQUOTA_STRING + "|" + BQUOTA_STRING + "";
  // DECIMAL_LITERAL: DEC_DIGIT+;
  String DECIMAL_LITERAL = "[0-9]+";
  // HEXADECIMAL_LITERAL: 'X' '\'' (HEX_DIGIT HEX_DIGIT)+ '\''
  // | '0X' HEX_DIGIT+;
  String HEXADECIMAL_LITERAL = "X'(HEX_DIGIT\\sHEX_DIGIT)+'|0x[0-9A-F]+";
  // REAL_LITERAL: (DEC_DIGIT+)? '.' DEC_DIGIT+
  // | DEC_DIGIT+ '.' EXPONENT_NUM_PART
  // | (DEC_DIGIT+)? '.' (DEC_DIGIT+ EXPONENT_NUM_PART)
  // | DEC_DIGIT+ EXPONENT_NUM_PART;
  String REAL_LITERAL = "(DEC_DIGIT+)? '.' DEC_DIGIT+" //
      + "|DEC_DIGIT+'.'EXPONENT_NUM_PART"//
      + "|(DEC_DIGIT+)?'.'(DEC_DIGIT+EXPONENT_NUM_PART)"//
      + "|DEC_DIGIT+ EXPONENT_NUM_PART";
  // NULL_SPEC_LITERAL: '\\' 'N';
  String NULL_SPEC_LITERAL = "\\\\N";
  // BIT_STRING: BIT_STRING_L;
  String BIT_STRING = BIT_STRING_L;
  // STRING_CHARSET_NAME: '_' CHARSET_NAME;
  String STRING_CHARSET_NAME = "_(" + CHARSET_NAME + ")";

  // ---------------------------------------------------------------------------
  // Identifiers
  // ---------------------------------------------------------------------------

  String DOT_ID = "\\." + ID_LITERAL;

  // ID: ID_LITERAL;
  String ID = ID_LITERAL;
  // REVERSE_QUOTE_ID: '`' ~'`'+ '`';
  String REVERSE_QUOTE_ID = "`[^`]+`";
  // STRING_USER_NAME: (
  // SQUOTA_STRING | DQUOTA_STRING
  // | BQUOTA_STRING | ID_LITERAL
  // ) '@'
  // (
  // SQUOTA_STRING | DQUOTA_STRING
  // | BQUOTA_STRING | ID_LITERAL
  // );
  String STRING_USER_NAME =
      "(" + SQUOTA_STRING + "|" + DQUOTA_STRING + "|" + BQUOTA_STRING + "|ID_LITERAL" + ")" + "@"
          + "(" + SQUOTA_STRING + "|" + DQUOTA_STRING + "|" + BQUOTA_STRING + "|ID_LITERAL" + ")";
  // LOCAL_ID: '@'
  // (
  // [A-Z0-9._$]+
  // | SQUOTA_STRING
  // | DQUOTA_STRING
  // | BQUOTA_STRING
  // );
  String LOCAL_ID =
      "@([A-Z0-9._$]+)|" + SQUOTA_STRING + "|" + DQUOTA_STRING + "|" + BQUOTA_STRING + ")";
  // GLOBAL_ID: '@' '@'
  // (
  // [A-Z0-9._$]+
  // | BQUOTA_STRING
  // );
  String GLOBAL_ID = "@@([A-Z0-9._$]+|" + BQUOTA_STRING + ")";

  // ---------------------------------------------------------------------------
  //
  // ---------------------------------------------------------------------------

  // ---------------------------------------------------------------------------
  //
  // ---------------------------------------------------------------------------

  // ---------------------------------------------------------------------------
  //
  // ---------------------------------------------------------------------------

  // ---------------------------------------------------------------------------
  //
  // ---------------------------------------------------------------------------

  // ---------------------------------------------------------------------------
  //
  // ---------------------------------------------------------------------------

  // ---------------------------------------------------------------------------
  //
  // ---------------------------------------------------------------------------

  // ---------------------------------------------------------------------------
  //
  // ---------------------------------------------------------------------------

  // ---------------------------------------------------------------------------
  //
  // ---------------------------------------------------------------------------

  // ---------------------------------------------------------------------------
  //
  // ---------------------------------------------------------------------------

  // ---------------------------------------------------------------------------
  //
  // ---------------------------------------------------------------------------

  // ---------------------------------------------------------------------------
  //
  // ---------------------------------------------------------------------------

  // ---------------------------------------------------------------------------
  //
  // ---------------------------------------------------------------------------

}
