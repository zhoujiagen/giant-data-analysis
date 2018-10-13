package com.spike.giantdataanalysis.cassandra.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ColumnDefinitions;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.UDTValue;

/**
 * Quick Start Examples.
 */
public class ExampleManualQuickstart {
  private static final Logger LOG = LoggerFactory.getLogger(ExampleManualQuickstart.class);

  public static void main(String[] args) {

    Cluster cluster = null;
    try {
      // MARK more cluster options...
      // MARK cluster initialization...
      cluster = Cluster.builder()//
          .withClusterName("Test Cluster")// client side cluster name
          .addContactPoint("127.0.0.1").build();

      final String keyspace = "my_keyspace";
      Session session = cluster.connect(keyspace);
      String query = "SELECT * FROM user"; // CQL
      ResultSet rs = session.execute(query);

      StringBuilder sb = new StringBuilder();
      for (Row row : rs) {

        // MARK primitive type, collection type, UDT
        sb.append("\nfirst_name=" + row.getString("first_name"));
        sb.append("\nphone_numbers=" + row.getList("phone_numbers", String.class));
        sb.append("\nemails=" + row.getSet("emails", String.class));
        sb.append("\naddresses=" + row.getMap("addresses", String.class, UDTValue.class)); // UDT

        // MARK column metadata
        sb.append("\n");
        for (ColumnDefinitions.Definition colDef : row.getColumnDefinitions()) {
          sb.append("\t" + colDef.getName() + "(" + colDef.getType() + ")");
        }
      }
      LOG.info(sb.toString());

    } finally {
      if (cluster != null) cluster.close();
    }

  }
}
