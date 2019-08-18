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

Example session showing loading data, tracking events, querying, then re-tracking.

```
$ ./ui.sh

Select a command:
1) Load data
2) Query all accounts
3) Make deposit
4) Shut down tracker
5) Start tracker
6) Rebuild tracker
7) Check tracking status
q) Quit
1

Select a command:
1) Load data
2) Query all accounts
3) Make deposit
4) Shut down tracker
5) Start tracker
6) Rebuild tracker
7) Check tracking status
q) Quit
7
{
  "segmentStatuses": [
    {
      "segment": {
        "segmentId": 0,
        "mask": 0
      },
      "caughtUp": true,
      "trackingToken": {
        "index": 390,
        "gaps": []
      },
      "replaying": false
    }
  ],
  "globalIndex": 390
}

Select a command:
1) Load data
2) Query all accounts
3) Make deposit
4) Shut down tracker
5) Start tracker
6) Rebuild tracker
7) Check tracking status
q) Quit
7
{
  "segmentStatuses": [
    {
      "segment": {
        "segmentId": 0,
        "mask": 0
      },
      "caughtUp": true,
      "trackingToken": {
        "index": 1503,
        "gaps": []
      },
      "replaying": false
    }
  ],
  "globalIndex": 1503
}

Select a command:
1) Load data
2) Query all accounts
3) Make deposit
4) Shut down tracker
5) Start tracker
6) Rebuild tracker
7) Check tracking status
q) Quit
2
[
  {
    "accountNumber": "efa4cdfa-1f8b-415f-8f77-f2b67960af89",
    "balance": 559236138
  },
  {
    "accountNumber": "d6aa08cf-a26c-40a0-8449-73fc747a8ec8",
    "balance": -2067202553
  },
  {
    "accountNumber": "6c2e7b9e-01cb-4455-9be6-7f954f78a370",
    "balance": -1676582417
  }
]

Select a command:
1) Load data
2) Query all accounts
3) Make deposit
4) Shut down tracker
5) Start tracker
6) Rebuild tracker
7) Check tracking status
q) Quit
3

Select a command:
1) Load data
2) Query all accounts
3) Make deposit
4) Shut down tracker
5) Start tracker
6) Rebuild tracker
7) Check tracking status
q) Quit
2
[
  {
    "accountNumber": "efa4cdfa-1f8b-415f-8f77-f2b67960af89",
    "balance": 881681163
  },
  {
    "accountNumber": "d6aa08cf-a26c-40a0-8449-73fc747a8ec8",
    "balance": -2067202553
  },
  {
    "accountNumber": "6c2e7b9e-01cb-4455-9be6-7f954f78a370",
    "balance": -1676582417
  }
]

Select a command:
1) Load data
2) Query all accounts
3) Make deposit
4) Shut down tracker
5) Start tracker
6) Rebuild tracker
7) Check tracking status
q) Quit
6

Select a command:
1) Load data
2) Query all accounts
3) Make deposit
4) Shut down tracker
5) Start tracker
6) Rebuild tracker
7) Check tracking status
q) Quit
7
{
  "segmentStatuses": [
    {
      "segment": {
        "segmentId": 0,
        "mask": 0
      },
      "caughtUp": false,
      "trackingToken": {
        "index": 1399,
        "gaps": []
      },
      "replaying": true
    }
  ],
  "globalIndex": 1504
}

Select a command:
1) Load data
2) Query all accounts
3) Make deposit
4) Shut down tracker
5) Start tracker
6) Rebuild tracker
7) Check tracking status
q) Quit
7
{
  "segmentStatuses": [
    {
      "segment": {
        "segmentId": 0,
        "mask": 0
      },
      "caughtUp": true,
      "trackingToken": {
        "index": 1504,
        "gaps": []
      },
      "replaying": true
    }
  ],
  "globalIndex": 1504
}

Select a command:
1) Load data
2) Query all accounts
3) Make deposit
4) Shut down tracker
5) Start tracker
6) Rebuild tracker
7) Check tracking status
q) Quit
q
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
