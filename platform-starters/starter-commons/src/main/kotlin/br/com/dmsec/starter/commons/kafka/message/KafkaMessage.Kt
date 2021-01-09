package br.com.dmsec.starter.commons.kafka.message

data class KafkaMessage<T>(
        val payload: T,
        val headers: MutableMap<String, String> = mutableMapOf()
)