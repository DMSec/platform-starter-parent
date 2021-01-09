package br.com.dmsec.starter.commons.extension


import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.type.TypeFactory
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.kotlinModule
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef

object JacksonExtension {

    val jacksonObjectMapper: ObjectMapper by lazy {
        ObjectMapper().registerModule(JavaTimeModule())
                .setSeriaizationInclusion(JsonInclude.Include.NON_NULL)
                .disable(SerializationFeature.WRITE_DATE_AS_TIMESTAMPS)
                .registerModule(KotlinModule())
    }
}