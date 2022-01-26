package ru.serobyan.kotlin_kafka_dsl_example

import io.kotest.core.spec.style.StringSpec
import kotlinx.coroutines.delay
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class DSLTest: StringSpec({

    val log: Logger = LoggerFactory.getLogger(this::class.java)

    "produce and consume" {
        kafka("localhost:9092") {
            consumer<String, String>("test-topic") { record ->
                log.info("consume value: ${record?.value()}")
            }

            producer<String, String>("test-topic") {
                send("111111111111111")
                flush()
            }
        }
        delay(1000000)
    }
})