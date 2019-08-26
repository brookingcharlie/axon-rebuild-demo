# Rebuilding Projections with the Axon Framework

Demo application showing an approach to rebuilding projections when using the Axon Framework.

## Scenario

The project includes a trivial bank account aggregate: accounts are opened, then have a series
of deposits and withdrawals made. The initial projection tries to track account balances, but
it contains an error since both deposited and withdrawn amounts are added to the balance! We
fix this by creating and running our first reprojection.

Then we decide to include a new field in our query model: the number of transactions that have
been made on an account. We perform another reprojection to set the value for this new column.

To introduction Reprojection 1:

* Uncomment changeset 0003 in `db.changelog-master.xml`
* Uncomment withdrawal fix in `AccountProjector.kt` (and comment old line)
* Uncomment event handler class in `Reprojection1.kt`
* Uncomment reprojection-1 event processor registration in `ReprojectionConfiguration.kt`

To introduce Reprojection 2:

* Uncomment changeset 0004 in `db.changelog-master.xml`
* Uncomment numTransactions field in `AccountView.kt`
* Uncomment numTransactions lines in `AccountProjector.kt` (and comment old lines)
* Uncomment event handler class in `Reprojection2.kt`
* Uncomment reprojection-2 event processor registration in `ReprojectionConfiguration.kt`
* Uncomment empty class in `Reprojection1.kt` (and commend old class)
* Comment reprojection-1 event processor registration in `ReprojectionConfiguration.kt`

## Usage

### Starting the database

Rather than embedding our H2 database within the Java application, we run it separately;
this allows us to test behaviour of a multi-process deployment and to perform database
operations when a Java process isn't running.

To run a database daemon, use:

```
docker run -d -p 9092:1521 -p 8082:81 -v $PWD/db:/opt/h2-data oscarfonts/h2
```

### Running the server

```
mvn spring-boot:run
```

### Running example client

Simple terminal UI that allows you to load data and do queries/updates.

```
$ ./ui.sh
```

### Querying in-memory database

You can use the terminal:

```
mvn exec:java@h2
```

Or H2's web console at <http://localhost:8082/h2-console>. Enter JBDC URL "jdbc:h2:tcp://localhost:1521/file:./db/demo" when connecting.

Example queries:

```
show tables;
show columns from domain_event_entry;
select payload_type, utf8tostring(payload), type, aggregate_identifier, sequence_number from domain_event_entry order by time_stamp;
select payload_type, utf8tostring(payload), type, aggregate_identifier, sequence_number from snapshot_event_entry order by time_stamp;
select * from account;
```
