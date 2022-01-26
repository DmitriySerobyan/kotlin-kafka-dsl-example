# kotlin kafka dsl example

```sh
docker-compose -f docker-compose-single-broker.yml up
```

```kotlin
kafka("localhost:9092") {
    consumer<String, String>("test-topic") { record ->
        log.info("consume value: ${record?.value()}")
    }

    producer<String, String>("test-topic") {
        send("111111111111111")
        flush()
    }
}
```