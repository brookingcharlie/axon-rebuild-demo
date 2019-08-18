# Rebuilding Projections with the Axon Framework

Demo application showing an approach to rebuilding projections when using the Axon Framework.

## Usage

### Running the server

From the command line, run `mvn spring-boot:run`.

In an IDE, run the `main` method as a Java application.

### Running example client

Simple terminal UI that allows you to load data,
control/monitor tracking projections,
and do queries/updates.

```
$ ./ui.sh
```

### Querying in-memory database

You can use the terminal:

```
mvn exec:java@h2
```

Or H2's web console at <http://localhost:8080/h2-console>. Enter JBDC URL "jdbc:h2:file:./db/demo" when connecting.

Example queries:

```
show tables;
show columns from domain_event_entry;
select payload_type, utf8tostring(payload), type, aggregate_identifier, sequence_number from domain_event_entry order by time_stamp;
select payload_type, utf8tostring(payload), type, aggregate_identifier, sequence_number from snapshot_event_entry order by time_stamp;
select * from account;
```
