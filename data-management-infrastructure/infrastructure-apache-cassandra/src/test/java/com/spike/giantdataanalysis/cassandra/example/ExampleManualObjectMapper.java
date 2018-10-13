package com.spike.giantdataanalysis.cassandra.example;

// 选项
import static com.datastax.driver.mapping.Mapper.Option.consistencyLevel;
import static com.datastax.driver.mapping.Mapper.Option.tracing;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.CodecRegistry;
import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.DataType;
import com.datastax.driver.core.ProtocolVersion;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.TypeCodec;
import com.datastax.driver.core.exceptions.InvalidTypeException;
import com.datastax.driver.core.utils.UUIDs;
import com.datastax.driver.mapping.DefaultNamingStrategy;
import com.datastax.driver.mapping.DefaultPropertyMapper;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingConfiguration;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.NamingConventions;
import com.datastax.driver.mapping.PropertyMapper;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;
import com.datastax.driver.mapping.annotations.QueryParameters;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.spike.giantdataanalysis.cassandra.example.domain.Address;
import com.spike.giantdataanalysis.cassandra.example.domain.User;

/**
 * Object Mapper Examples.
 * <p>
 * REF: https://docs.datastax.com/en/developer/java-driver/3.5/manual/object_mapper/
 * @see com.datastax.driver.core.DataType
 */
public class ExampleManualObjectMapper {
  private static final Logger LOG = LoggerFactory.getLogger(ExampleManualObjectMapper.class);

  public static void main(String[] args) {
    Cluster cluster = null;
    try {
      cluster = Cluster.builder()//
          .withClusterName("Test Cluster")// client side cluster name
          .withCodecRegistry( // MARK register codec
            new CodecRegistry().register(new MyTypeCodec(DataType.text(), MyType.class)))
          .addContactPoint("127.0.0.1").build();

      final String keyspace = "my_keyspace";
      Session session = cluster.connect(keyspace);

      // MARK mapping manager
      PropertyMapper propertyMapper = new DefaultPropertyMapper().setNamingStrategy(//
        new DefaultNamingStrategy(NamingConventions.LOWER_SNAKE_CASE,
            NamingConventions.LOWER_SNAKE_CASE));
      MappingConfiguration configuration = MappingConfiguration.builder()//
          .withPropertyMapper(propertyMapper)//
          .build();
      MappingManager manager = new MappingManager(session, configuration);
      // MARK entity mapper
      Mapper<User> userMapper = manager.mapper(User.class);
      // MARK entity mapper options
      userMapper.setDefaultGetOptions(tracing(true), consistencyLevel(ConsistencyLevel.QUORUM));

      /// CRUD
      User user1 = new User();
      user1.setFirst_name("Dave");
      user1.setLast_name("David");
      user1.setEmails(Sets.newHashSet("dave@example.com"));
      user1.setPhone_numbers(Lists.newArrayList("111103"));
      Map<String, Address> addresses = Maps.newHashMap();
      addresses.put("address1", new Address("street4", "city4", "state4", 0004));
      user1.setAddresses(addresses);
      Map<UUID, Integer> login_sessions = Maps.newHashMap(); // DefaultPropertyMapper
      login_sessions.put(UUIDs.timeBased(), 1);
      user1.setLogin_sessions(login_sessions);

      userMapper.save(user1);
      user1.setLast_name("Green");
      userMapper.save(user1);
      User queryResultUser = userMapper.get("Dave");
      LOG.info("\n\n" + queryResultUser);
      userMapper.delete("Dave");

      /// Mapper and Statement
      Statement saveStmt = userMapper.saveQuery(user1); // Statement
      LOG.info("\n\n" + saveStmt);
      ResultSet userRS = session.execute("SELECT * FROM user");
      Result<User> users = userMapper.map(userRS);
      LOG.info("\n\n" + Joiner.on("\n").join(users));

      /// accessor
      UserAccessor userAccessor = manager.createAccessor(UserAccessor.class);
      users = userAccessor.getAll();
      LOG.info("\n\n" + Joiner.on("\n").join(users));

    } finally {
      if (cluster != null) cluster.close();
    }
  }

  @Accessor
  public interface UserAccessor {
    @Query("SELECT * FROM user")
    @QueryParameters(consistency = "QUORUM")
    Result<User> getAll();

    @Query("INSERT INTO user (first_name, last_name) VALUES (?,?)")
    ResultSet insert(String first_name, String last_name);

    @Query("INSERT INTO user (first_name, last_name) VALUES (?,?)")
    ResultSet insert2(@Param("fn") String first_name, @Param("ln") String last_name);
  }

  /// codec
  public static class MyType {
    private String value;

    public MyType() {
    }

    public MyType(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    public void setValue(String value) {
      this.value = value;
    }
  }

  // can be used in @Column(codec=...), @Field(codec=...)
  public static class MyTypeCodec extends TypeCodec<MyType> {

    public MyTypeCodec(DataType cqlType, Class<MyType> javaClass) {
      super(cqlType, javaClass);
    }

    @Override
    public ByteBuffer serialize(MyType value, ProtocolVersion protocolVersion)
        throws InvalidTypeException {
      return ByteBuffer.wrap(value.value.getBytes());
    }

    @Override
    public MyType deserialize(ByteBuffer bytes, ProtocolVersion protocolVersion)
        throws InvalidTypeException {
      return new MyType(new String(bytes.array()));
    }

    @Override
    public MyType parse(String value) throws InvalidTypeException {
      return new MyType(value);
    }

    @Override
    public String format(MyType value) throws InvalidTypeException {
      return value.value;
    }

  }

}
