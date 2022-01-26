package ru.serobyan.kotlin_kafka_dsl_example

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import java.util.*
import kotlin.reflect.KClass

class Producer<K : Any, V : Any>(
    bootstrapServers: String,
    private val topic: String,
    keySerializer: KClass<*> = StringSerializer::class,
    valueSerializer: KClass<*> = StringSerializer::class,
) {
    private val producer: KafkaProducer<K, V>

    init {
        val config = Properties()
        config[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers
        config[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = keySerializer.java
        config[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = valueSerializer.java
        producer = KafkaProducer(config)
    }

    fun send(key: K, value: V) {
        producer.send(
            ProducerRecord(topic, key, value)
        )
    }

    fun send(value: V) {
        producer.send(
            ProducerRecord(topic, value)
        )
    }

    fun flush() {
        producer.flush()
    }
}