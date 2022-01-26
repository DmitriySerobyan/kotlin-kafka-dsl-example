package ru.serobyan.kotlin_kafka_dsl_example

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import java.time.Duration
import java.util.*
import kotlin.reflect.KClass

class Kafka(private val bootstrapServers: String) {

    fun <K : Any, V : Any> consumer(
        topic: String,
        groupId: String = UUID.randomUUID().toString(),
        keySerializer: KClass<*> = StringDeserializer::class,
        valueSerializer: KClass<*> = StringDeserializer::class,
        pollTimeout: Duration = Duration.ofSeconds(1),
        consumer: (record: ConsumerRecord<K, V>?) -> Unit
    ) {
        Consumer(
            bootstrapServers = bootstrapServers,
            topic = topic,
            groupId = groupId,
            keyDeserializer = keySerializer,
            valueDeserializer = valueSerializer,
            pollTimeout = pollTimeout,
            handler = consumer
        ).start()
    }

    fun <K : Any, V : Any> producer(
        topic: String,
        keySerializer: KClass<*> = StringSerializer::class,
        valueSerializer: KClass<*> = StringSerializer::class,
        producer: Producer<K, V>.() -> Unit
    ) {
        Producer<K, V>(
            bootstrapServers = bootstrapServers,
            topic = topic,
            keySerializer = keySerializer,
            valueSerializer = valueSerializer,
        ).producer()
    }
}