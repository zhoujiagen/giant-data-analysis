package com.spike.giantdataanalysis.benchmark.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class JDBCs {
  private static final Logger LOG = LoggerFactory.getLogger(JDBCs.class);

  static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
  static final String DB_URL = "jdbc:mysql://localhost:3306/tpc";
  static final String USER = "root";
  static final String PASS = "admin";

  enum Op {
    INSERT, UPDATE, DELETE, QUERY
  }

  // ---------------------------------------------------------------------------
  // Provided Connection
  // ---------------------------------------------------------------------------
  public static Connection connection() {
    try {
      Class.forName(JDBC_DRIVER);
      return DriverManager.getConnection(DB_URL, USER, PASS);
    } catch (ClassNotFoundException e) {
      LOG.error("", e);
    } catch (SQLException e) {
      LOG.error("", e);
    }
    return null;
  }

  public static int create(Connection connection, String sql) {
    Statement stmt;
    try {
      stmt = connection.createStatement();
      int result = stmt.executeUpdate(sql);
      stmt.close();
      return result;
    } catch (SQLException e) {
      LOG.error("execute sql: " + sql + " failed", e);
    }
    return -1;
  }

  public static int create(Connection connection, String preparedStmt, Object[] data)
      throws SQLException {
    PreparedStatement pstmt = connection.prepareStatement(preparedStmt);
    for (int i = 1, len = data.length; i <= len; i++) {
      pstmt.setObject(i, data[i - 1]);
    }

    int result = pstmt.executeUpdate();
    pstmt.close();
    return result;
  }

  public static <T> T query(Connection conn, String sql, OpCallback<T> callback)
      throws SQLException {
    Statement stmt = conn.createStatement();
    ResultSet rs = stmt.executeQuery(sql);
    if (callback != null) {
      return callback.rs(rs);
    } else {
      return null;
    }
  }

  public static void close(Connection connection) {
    if (connection != null) {
      try {
        connection.close();
      } catch (SQLException e) {
        LOG.error("", e);
      }
    }
  }

  // ---------------------------------------------------------------------------
  // One shot Connection
  // ---------------------------------------------------------------------------

  public static void batch(String preparedStmt, List<Object[]> datas, OpCallback<String> callback) {

    Connection conn = null;
    try {
      Class.forName(JDBC_DRIVER);
      conn = DriverManager.getConnection(DB_URL, USER, PASS);

      for (Object[] data : datas) {
        int i = create(conn, preparedStmt, data);
        if (callback != null) {
          callback.sql(preparedStmt);
          callback.status(i > 0);
        }
      }

    } catch (SQLException e) {
      LOG.error("", e);
    } catch (Exception e) {
      LOG.error("", e);
    } finally {
      try {
        if (conn != null) conn.close();
      } catch (SQLException e) {
        LOG.error("", e);
      }
    }

  }

  public static void batch(String[] sqls, OpCallback<String> callback) {
    Connection conn = null;
    try {
      Class.forName(JDBC_DRIVER);
      conn = DriverManager.getConnection(DB_URL, USER, PASS);

      for (String sql : sqls) {
        int i = create(conn, sql);
        if (callback != null) {
          callback.sql(sql);
          callback.status(i > 0);
        }
      }

    } catch (SQLException e) {
      LOG.error("", e);
    } catch (Exception e) {
      LOG.error("", e);
    } finally {
      try {
        if (conn != null) conn.close();
      } catch (SQLException e) {
        LOG.error("", e);
      }
    }

  }

  public static void op(Op op, String sql, OpCallback<String> callback) {
    Connection conn = null;
    try {
      Class.forName(JDBC_DRIVER);
      conn = DriverManager.getConnection(DB_URL, USER, PASS);

      // do something
      switch (op) {
      case INSERT:
      case UPDATE:
      case DELETE:
        int i = create(conn, sql);
        if (callback != null) {
          callback.sql(sql);
          callback.status(i > 0);
        }
        break;
      case QUERY:
        query(conn, sql, callback);
        if (callback != null) {
          callback.sql(sql);
          callback.status(true);
        }
        break;
      default:
        conn.close();
        throw new RuntimeException();
      }
    } catch (SQLException e) {
      LOG.error("", e);
    } catch (Exception e) {
      LOG.error("", e);
    } finally {
      try {
        if (conn != null) conn.close();
      } catch (SQLException e) {
        LOG.error("", e);
      }
    }

  }

  public static void main(String[] args) {
    String sql = "SELECT * FROM Item";
    OpCallback<String> callback = new OpCallback<String>() {
      private String data;

      @Override
      public void status(boolean status) {
        System.err.println(status);
      }

      @Override
      public void sql(String sql) {
        System.err.println(sql);
      }

      @Override
      public String rs(ResultSet rs) {
        try {
          boolean hasNext = rs.next();
          if (hasNext) {
            data = rs.getString(1);
          }
        } catch (SQLException e) {
          LOG.error("", e);
        }
        return data;
      }

      @Override
      public String data() {
        return data;
      }
    };
    JDBCs.op(Op.QUERY, sql, callback);
    System.err.println(callback.data());

  }

}
