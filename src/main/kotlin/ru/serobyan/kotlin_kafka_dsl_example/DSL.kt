package ru.serobyan.kotlin_kafka_dsl_example

fun kafka(bootstrapServers: String, init: Kafka.() -> Unit) {
    Kafka(bootstrapServers = bootstrapServers).init()
}