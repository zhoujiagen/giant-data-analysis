/**
 * <pre>
 * ExampleManual*: REF https://docs.datastax.com/en/developer/java-driver/3.5/manual/
 * </pre>
 */
package com.spike.giantdataanalysis.cassandra.example;

//@formatter:off
/* Schema and Data

CREATE TYPE my_keyspace.address (
street text,
city text,
state text,
zip_code int
);

CREATE TABLE my_keyspace.user (
first_name text PRIMARY KEY,
addresses map<text, frozen<address>>,
emails set<text>,
last_name text,
login_sessions map<timeuuid, int>,
phone_numbers list<text>,
title text
)

INSERT INTO user (first_name, last_name, emails, phone_numbers, addresses) VALUES
('Alice', 'David', {'alice@example.com'}, ['111100'], 
{'address1': {street: 'street', city: 'city', state: 'state', zip_code: 0001}});

INSERT INTO user (first_name, last_name, emails, phone_numbers, addresses) VALUES
('Bob', 'David', {'bob@example.com'}, ['111101'], 
{'address1': {street: 'street2', city: 'city2', state: 'state2', zip_code: 0002}});

INSERT INTO user (first_name, last_name, emails, phone_numbers, addresses) VALUES    
('Cartman', 'David', {'cartman@example.com'}, ['111102'], 
    {'address1': {street: 'street3', city: 'city3', state: 'state3', zip_code: 0003},
      'address2': {street: 'street4', city: 'city3', state: 'state3', zip_code: 0003}});
*/