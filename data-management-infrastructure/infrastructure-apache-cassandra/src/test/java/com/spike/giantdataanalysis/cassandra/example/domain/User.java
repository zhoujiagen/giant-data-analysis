package com.spike.giantdataanalysis.cassandra.example.domain;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.FrozenValue;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

/**
 * @see com.datastax.driver.core.ConsistencyLevel
 */
@Table(keyspace = "my_keyspace", name = "user", //
    readConsistency = "QUORUM", writeConsistency = "QUORUM", //
    caseSensitiveKeyspace = false, caseSensitiveTable = false)
public class User {

  @PartitionKey
  @Column(name = "first_name")
  private String first_name;

  private String last_name;
  private String title;

  @FrozenValue
  private Map<String, Address> addresses;

  private Set<String> emails;
  private List<String> phone_numbers;

  /**
   * timeuuid => count
   * @see com.datastax.driver.core.utils.UUIDs.timeBased()
   * @see com.datastax.driver.core.utils.UUIDs.unixTimestamp(UUID)
   */
  private Map<UUID, Integer> login_sessions;

  public String getFirst_name() {
    return first_name;
  }

  public void setFirst_name(String first_name) {
    this.first_name = first_name;
  }

  public String getLast_name() {
    return last_name;
  }

  public void setLast_name(String last_name) {
    this.last_name = last_name;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Map<String, Address> getAddresses() {
    return addresses;
  }

  public void setAddresses(Map<String, Address> addresses) {
    this.addresses = addresses;
  }

  public Set<String> getEmails() {
    return emails;
  }

  public void setEmails(Set<String> emails) {
    this.emails = emails;
  }

  public List<String> getPhone_numbers() {
    return phone_numbers;
  }

  public void setPhone_numbers(List<String> phone_numbers) {
    this.phone_numbers = phone_numbers;
  }

  public Map<UUID, Integer> getLogin_sessions() {
    return login_sessions;
  }

  public void setLogin_sessions(Map<UUID, Integer> login_sessions) {
    this.login_sessions = login_sessions;
  }

  @Override
  public String toString() {
    return "User [first_name=" + first_name + ", last_name=" + last_name + ", title=" + title
        + ", addresses=" + addresses + ", emails=" + emails + ", phone_numbers=" + phone_numbers
        + ", login_sessions=" + login_sessions + "]";
  }
}
