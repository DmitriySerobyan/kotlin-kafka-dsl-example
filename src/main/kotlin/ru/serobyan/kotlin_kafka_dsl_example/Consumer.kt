package ru.serobyan.kotlin_kafka_dsl_example

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import java.time.Duration
import java.util.*
import kotlin.reflect.KClass

class Consumer<K : Any, V : Any>(
    bootstrapServers: String,
    topic: String,
    groupId: String = UUID.randomUUID().toString(),
    keyDeserializer: KClass<*> = StringDeserializer::class,
    valueDeserializer: KClass<*> = StringDeserializer::class,
    private val pollTimeout: Duration = Duration.ofSeconds(1),
    private val handler: (record: ConsumerRecord<K, V>?) -> Unit
) {
    private val consumer: KafkaConsumer<K, V>

    @Volatile
    var isConsuming = true

    init {
        val config = Properties()
        config[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers
        config[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = keyDeserializer.java
        config[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = valueDeserializer.java
        config[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "earliest"
        config[ConsumerConfig.GROUP_ID_CONFIG] = groupId
        consumer = KafkaConsumer<K, V>(config)
        consumer.subscribe(listOf(topic))
        Runtime.getRuntime().addShutdownHook(Thread { stop() })
    }

    fun start() {
        Thread {
            isConsuming = true
            consumer.use {
                while (isConsuming) {
                    it.poll(pollTimeout)?.forEach { record ->
                        handler(record)
                        consumer.commitSync()
                    }
                }
            }
        }.start()
    }

    fun stop() {
        isConsuming = false
    }
}