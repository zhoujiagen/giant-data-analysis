package com.spike.giantdataanalysis.benchmark.database.tpc;

import java.sql.Connection;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.giantdataanalysis.benchmark.database.JDBCs;
import com.spike.giantdataanalysis.commons.lang.DateUtils;
import com.spike.giantdataanalysis.commons.support.Randoms;

public class TPC_C {
  private static final Logger LOG = LoggerFactory.getLogger(TPC_C.class);

  public static int MAXITEMS = 10;// 100000;
  public static int CUST_PER_DIST = 30;// 3000;
  public static int DIST_PER_WARE = 10;
  public static int ORD_PER_DIST = 30;// 3000;

  public static int COUNT_WARE = 2;// 20;

  public static boolean debug = true;

  public static String now() {
    return DateUtils.now(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
  }

  public static String[] LASTNAMES = new String[] { //
      "BAR", "OUGHT", "ABLE", "PRI", "PRES", "ESE", "ANTI", "CALLY", "ATION", "EING" };

  public static void main(String[] args) {
    LoadItems();
    LoadWare();

    LoadCust();
    LoadOrd();
  }

  // for C_LAST, the range is [0 .. 999] and A = 255
  // for C_ID, the range is [1 .. 3000] and A = 1023
  // for OL_I_ID, the range is [1 .. 100000] and A = 8191
  // C is a run-time constant randomly chosen within [0 .. A]
  static long NURand(int A, int x, int y, int C) {
    return (((Randoms.INT(A) | Randoms.INT(x, y)) + C) % (y - x + 1)) + x;
  }

  static void LoadItems() {
    LOG.info("load Item START");

    boolean[] orig = new boolean[MAXITEMS];
    for (int i = 0; i < MAXITEMS / 10; i++) {
      orig[i] = true;
    }
    int pos;
    for (int i = 0; i < MAXITEMS / 10; i++) {
      do {
        pos = Randoms.INT(MAXITEMS);
      } while (orig[pos]);
      orig[pos] = true;
    }

    String query =
        "INSERT INTO `C_Item`(`I_ID`, `I_NAME`,`I_PRICE`,`I_DATA`) VALUES (%1$s,'%2$s',%3$s,'%4$s')";
    Connection connection = JDBCs.connection();
    String i_data = null;
    String postfix = "original";
    String sql = null;
    for (int i_id = 1; i_id <= MAXITEMS; i_id++) {
      i_data = Randoms.ALPHA_STRING(26, 50);
      i_data = i_data.substring(0, i_data.length() - postfix.length()) + postfix;
      sql = String.format(query, //
        i_id, Randoms.ALPHA_STRING(14, 24), Randoms.FLOAT(999f) + 1f, i_data);
      if (debug) {
        LOG.info(sql);
      }
      JDBCs.create(connection, sql);
    }
    JDBCs.close(connection);

    LOG.info("load Item END");
  }

  static void LoadWare() {
    LOG.info("load Warehouse START");

    String query =
        "INSERT INTO `C_Warehouse` (`W_ID`,`W_NAME`,`W_STREET1`,`W_STREET2`,`W_CITY`,`W_STATE`,`W_ZIP`,`W_TAX`,`W_YTD`) "
            + "VALUES (%1$s,'%2$s','%3$s','%4$s','%5$s','%6$s','%7$s',%8$s,%9$s)";
    Connection connection = JDBCs.connection();
    String sql = null;
    for (int w_id = 1; w_id <= COUNT_WARE; w_id++) {
      sql = String.format(query, //
        w_id, //
        Randoms.ALPHA_STRING(6, 10), //
        Randoms.ALPHA_STRING(10, 20), // street_1
        Randoms.ALPHA_STRING(10, 20), // street_2
        Randoms.ALPHA_STRING(10, 20), // city
        Randoms.ALPHA_STRING(2, 2), // state
        Randoms.ALPHA_STRING(9, 9), // zip
        (float) Randoms.INT(10, 20) / 100.0f, //
        3000000.00f);
      if (debug) {
        LOG.info(sql);
      }
      JDBCs.create(connection, sql);

      Stock(w_id);
      District(w_id);
    }

    JDBCs.close(connection);

    LOG.info("load Warehouse END");
  }

  // for C_LAST, the range is [0 .. 999] and A = 255
  // for C_ID, the range is [1 .. 3000] and A = 1023
  static int C_LAST = Randoms.INT(0, 256);
  static int C_ID = Randoms.INT(0, 1023);

  static void LoadCust() {
    LOG.info("load Customer START");

    Connection connection = JDBCs.connection();
    for (int w_id = 1; w_id <= COUNT_WARE; w_id++) {
      for (int d_id = 1; d_id <= DIST_PER_WARE; d_id++) {
        Customer(connection, w_id, d_id);
      }
    }
    JDBCs.close(connection);

    LOG.info("load Customer END");
  }

  static void LoadOrd() {
    LOG.info("load Orders START");

    Connection connection = JDBCs.connection();
    for (int w_id = 1; w_id <= COUNT_WARE; w_id++) {
      for (int d_id = 1; d_id <= DIST_PER_WARE; d_id++) {
        Orders(connection, w_id, d_id);
      }
    }
    JDBCs.close(connection);

    LOG.info("load Orders END");
  }

  static void LoadNewOrd() {
    LOG.info("load NewOrder START");
    LOG.info("load NewOrder END");
  }

  static void Stock(int w_id) {
    boolean[] orig = new boolean[MAXITEMS];
    for (int i = 0; i < MAXITEMS / 10; i++) {
      orig[i] = true;
    }
    int pos;
    for (int i = 0; i < MAXITEMS / 10; i++) {
      do {
        pos = Randoms.INT(MAXITEMS);
      } while (orig[pos]);
      orig[pos] = true;
    }

    String stockQuery = "INSERT INTO `C_Stock` (`S_I_ID`,`S_W_ID`,`S_QUANTITY`,"
        + "`S_DIST_01`,`S_DIST_02`,`S_DIST_03`,`S_DIST_04`,`S_DIST_05`,`S_DIST_06`,`S_DIST_07`,`S_DIST_08`,`S_DIST_09`,`S_DIST_10`,"
        + "`S_YTD`,`S_ORDER_CNT`,`S_REMOTE_CNT`,`S_DATA`) " + "VALUES (%1$s,%2$s,%3$s,"
        + "'%4$s','%5$s','%6$s','%7$s','%8$s','%9$s','%10$s','%11$s','%12$s','%13$s',"
        + "%14$s,%15$s,%16$s,'%17$s')";
    Connection connection = JDBCs.connection();
    String stockSQL = null;
    String s_data = null;
    String postfix = "original";
    for (int s_i_id = 1; s_i_id <= MAXITEMS; s_i_id++) {

      s_data = Randoms.ALPHA_STRING(26, 50);
      if (orig[s_i_id - 1]) {
        s_data = s_data.substring(0, s_data.length() - postfix.length()) + postfix;
      }
      stockSQL = String.format(stockQuery, //
        s_i_id, w_id, Randoms.INT(10, 100), //
        Randoms.ALPHA_STRING(24, 24), Randoms.ALPHA_STRING(24, 24), Randoms.ALPHA_STRING(24, 24),
        Randoms.ALPHA_STRING(24, 24), Randoms.ALPHA_STRING(24, 24), Randoms.ALPHA_STRING(24, 24),
        Randoms.ALPHA_STRING(24, 24), Randoms.ALPHA_STRING(24, 24), //
        Randoms.ALPHA_STRING(24, 24), Randoms.ALPHA_STRING(24, 24), //
        0, 0, 0, s_data);
      if (debug) {
        LOG.info(stockSQL);
      }
      JDBCs.create(connection, stockSQL);
    }
    JDBCs.close(connection);
  }

  static void District(int w_id) {

    String districtQuery = "INSERT INTO `C_District` (`D_ID`,`D_W_ID`,`D_NAME`,"
        + "`D_STREET_1`,`D_STREET_2`,`D_CITY`,`D_STATE`,`D_ZIP`,"
        + "`D_TAX`,`D_YTD`,`D_NEXT_O_ID`) " + "VALUES (%1$s,%2$s,'%3$s',"
        + "'%4$s','%5$s','%6$s','%7$s','%8$s'," + "%9$s,%10$s,%11$s)";
    Connection connection = JDBCs.connection();
    String districtSQL = null;
    for (int d_id = 1; d_id <= DIST_PER_WARE; d_id++) {
      districtSQL = String.format(districtQuery, //
        d_id, w_id, Randoms.ALPHA_STRING(6, 10), //
        Randoms.ALPHA_STRING(10, 20), // street_1
        Randoms.ALPHA_STRING(10, 20), // street_2
        Randoms.ALPHA_STRING(10, 20), // city
        Randoms.ALPHA_STRING(2, 2), // state
        Randoms.ALPHA_STRING(9, 9), // zip
        (float) Randoms.INT(10, 20) / 100.0, 30000.0, 3000L);

      JDBCs.create(connection, districtSQL);
    }
    JDBCs.close(connection);
  }

  static void Customer(Connection connection, int w_id, int d_id) {

    String query = "INSERT INTO `C_Customer`(`C_ID`,`C_D_ID`,`C_W_ID`,`C_FIRST`,`C_MIDDLE`,`C_LAST`,"
        + "`C_STREET_1`,`C_STREET_2`,`C_CITY`,`C_STATE`,`C_ZIP`,`C_PHONE`,`C_SINCE`,`C_CREDIT`,`C_CREDIT_LIM`,"
        + "`C_DISCOUNT`,`C_BALANCE`,`C_DATA`) " + "VALUES (%1$s,%2$s,%3$s,'%4$s','%5$s','%6$s',"
        + "'%7$s','%8$s','%9$s','%10$s','%11$s','%12$s','%13$s','%14$s',%15$s,"
        + "%16$s,%17$s,'%18$s')";

    String historyQuery =
        "INSERT INTO `C_History`(`H_C_ID`,`H_C_D_ID`,`H_C_W_ID`,`H_D_ID`,`H_W_ID`,`H_DATE`,`H_AMOUNT`,`H_DATA`) "
            + "VALUES (%1$s,%2$s,%3$s,%4$s,%5$s,'%6$s',%7$s,'%8$s')";
    String sql = null;
    String historySql = null;
    for (int c_id = 1; c_id <= CUST_PER_DIST; c_id++) {
      sql = String.format(query, //
        c_id, //
        d_id, //
        w_id, //
        Randoms.ALPHA_STRING(8, 16), //
        "OE", //
        c_id <= 1000 ? Lastname(c_id - 1) : NURand(255, 0, 999, C_LAST), // C_LAST
        Randoms.ALPHA_STRING(10, 20), // street_1
        Randoms.ALPHA_STRING(10, 20), // street_2
        Randoms.ALPHA_STRING(10, 20), // city
        Randoms.ALPHA_STRING(2, 2), // state
        Randoms.ALPHA_STRING(9, 9), // zip
        Randoms.NUMBER_STRING(16, 16), //
        now(), //
        Randoms.BOOLEAN() ? "GC" : "BC", //
        50000, //
        (float) Randoms.INT(0, 50) / 100.0, //
        -10.0, //
        Randoms.ALPHA_STRING(300, 500)//
      );
      if (debug) {
        LOG.info(sql);
      }
      JDBCs.create(connection, sql);
      historySql = String.format(historyQuery, //
        c_id, d_id, w_id, d_id, w_id, now(), 10.0, Randoms.ALPHA_STRING(12, 24));
      if (debug) {
        LOG.info(historySql);
      }
      JDBCs.create(connection, historySql);
    }

  }

  static void Orders(Connection connection, int w_id, int d_id) {

    String orderQuery =
        "INSERT INTO `C_Orders`(`O_ID`,`O_D_ID`,`O_W_ID`,`O_C_ID`,`O_ENTRY_D`,`O_CARRIER_ID`,`O_OL_CNT`,`O_ALL_LOCAL`) "
            + "VALUES (%1$s,%2$s,%3$s,%4$s,'%5$s',%6$s,%7$s,%8$s)";
    String orderQueryUnDelivered =
        "INSERT INTO `C_Orders`(`O_ID`,`O_D_ID`,`O_W_ID`,`O_C_ID`,`O_ENTRY_D`,`O_OL_CNT`,`O_ALL_LOCAL`) "
            + "VALUES (%1$s,%2$s,%3$s,%4$s,'%5$s',%6$s,%7$s)";
    String newOrderQuery =
        "INSERT INTO `C_NewOrder`(`NO_O_ID`,`NO_D_ID`,`NO_W_ID`) VALUES (%1$s,%2$s,%3$s)";
    String orderLineUnDelivered =
        "INSERT INTO `C_OrderLine`(`OL_O_ID`,`OL_D_ID`,`OL_W_ID`,`OL_NUMBER`,`OL_I_ID`,"
            + "`OL_SUPPLY_W_ID`,`OL_QUANTITY`,`OL_AMOUNT`,`OL_DIST_INFO`) "
            + "VALUES (%1$s,%2$s,%3$s,%4$s,%5$s,%6$s,%7$s,%8$s,'%9$s')";
    String orderLineQuery =
        "INSERT INTO `C_OrderLine`(`OL_O_ID`,`OL_D_ID`,`OL_W_ID`,`OL_NUMBER`,`OL_I_ID`,"
            + "`OL_SUPPLY_W_ID`,`OL_DELIVERY_D`,`OL_QUANTITY`,`OL_AMOUNT`,`OL_DIST_INFO`) "
            + "VALUES (%1$s,%2$s,%3$s,%4$s,%5$s,%6$s,'%7$s',%8$s,%9$s,'%10$s')";

    String orderSQL = null;
    String newOrderSQL = null;
    String orderLineSQL = null;
    for (int o_id = 1; o_id < ORD_PER_DIST; o_id++) {
      int o_c_id = Randoms.INT(1, CUST_PER_DIST);
      int o_carrier_id = Randoms.INT(1, 10);
      int o_ol_cnt = Randoms.INT(5, 15);

      // last part: non dilivered
      if (o_id > ORD_PER_DIST - ORD_PER_DIST / 3) {
        orderSQL = String.format(orderQueryUnDelivered, //
          o_id, d_id, w_id, o_c_id, now(), o_ol_cnt, 1);
        newOrderSQL = String.format(newOrderQuery, //
          o_id, d_id, w_id);
        if (debug) {
          LOG.info(orderSQL);
          LOG.info(newOrderSQL);
        }
        JDBCs.create(connection, orderSQL);
        JDBCs.create(connection, newOrderSQL);
      } else {
        orderSQL = String.format(orderQuery, //
          o_id, d_id, w_id, o_c_id, now(), o_carrier_id, o_ol_cnt, 1);
        if (debug) {
          LOG.info(orderSQL);
        }
        JDBCs.create(connection, orderSQL);
      }

      // OrderLine
      for (int ol = 1; ol < o_ol_cnt; ol++) {
        int ol_i_id = Randoms.INT(1, MAXITEMS);

        if (o_id > ORD_PER_DIST - ORD_PER_DIST / 3) {
          orderLineSQL = String.format(orderLineUnDelivered, //
            o_id, d_id, w_id, ol, ol_i_id, //
            w_id, 5, 0.0, Randoms.ALPHA_STRING(24, 24));
        } else {
          orderLineSQL = String.format(orderLineQuery, //
            o_id, d_id, w_id, ol, ol_i_id, //
            w_id, now(), 5, (float) Randoms.INT(10, 10000) / 100.0, Randoms.ALPHA_STRING(24, 24));
        }
        if (debug) {
          LOG.info(orderLineSQL);
        }
        JDBCs.create(connection, orderLineSQL);
      }
    }

  }

  static void New_Orders() {
  }

  static void MakeAddress() {
    Randoms.ALPHA_STRING(10, 20); // street_1
    Randoms.ALPHA_STRING(10, 20); // street_2
    Randoms.ALPHA_STRING(10, 20); // city
    Randoms.ALPHA_STRING(2, 2); // state
    Randoms.ALPHA_STRING(9, 9); // zip
  }

  static void Error() {
  }

  static String Lastname(int num) {
    return LASTNAMES[num / 100] + LASTNAMES[(num / 10) % 10] + LASTNAMES[num % 10];
  }
}
