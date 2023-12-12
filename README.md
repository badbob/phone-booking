
# How to run?

## Start Zookeeper

    $ cd kafka
    $ bin/zookeeper-server-start.sh config/zookeeper.properties

## Start Kafka

    $ cd kafka
    $ bin/kafka-server-start.sh config/server.properties

## Start the application

    $ cd phone_booking
    $ gradle bootRun

# Request examples

## List devices

    $ curl localhost:8080/device/list | jq .

Response

```javascript
[
  {
    "id": 1,
    "version": 0,
    "availability": "AVAILABLE",
    "bookingTs": null,
    "bookedBy": null,
    "model": {
      "id": 1,
      "name": "S9",
      "manufacturer": {
        "id": 1,
        "name": "Samsung"
      }
    },
    "available": true
  },
  {
    "id": 2,
    "version": 399,
    "availability": "BOOKED",
    "bookingTs": "2023-12-12T12:35:39.791+00:00",
    "bookedBy": "alice",
    "model": {
      "id": 2,
      "name": "S8",
      "manufacturer": {
        "id": 1,
        "name": "Samsung"
      }
    },
    "available": false
  },
```

## Book device

```
$ curl -u alice:alice -X PUT -s localhost:8080/device/2/book | jq .
{
  "id": 2,
  "version": 401,
  "availability": "BOOKED",
  "bookingTs": "2023-12-12T12:42:04.971+00:00",
  "bookedBy": "alice",
  "model": {
    "id": 2,
    "name": "S8",
    "manufacturer": {
      "id": 1,
      "name": "Samsung"
    }
  },
  "available": false
}
```

## Release device

```
$ curl -u alice:alice -X PUT -s localhost:8080/device/2/release | jq .
{
  "id": 2,
  "version": 400,
  "availability": "AVAILABLE",
  "bookingTs": null,
  "bookedBy": null,
  "model": {
    "id": 2,
    "name": "S8",
    "manufacturer": {
      "id": 1,
      "name": "Samsung"
    }
  },
  "available": true
}

```


# Integration test

```
$ ./concurrent_updates.py
Device id=2 was booked successfully
Failed to book device id=2: Device with id=2 already booked by someone else
Device id=2 was released successfully
Failed to release device id=2: Device with id=2 already booked by someone else
Device id=2 was released successfully
Device id=2 was released successfully
Device id=2 was booked successfully
Failed to book device id=2: Device with id=2 updated concurrently, retry please
Failed to release device id=2: Device with id=2 already booked by someone else
Device id=2 was released successfully
Device id=2 was booked successfully
Failed to book device id=2: Device with id=2 updated concurrently, retry please
Failed to release device id=2: Device with id=2 already booked by someone else
```
