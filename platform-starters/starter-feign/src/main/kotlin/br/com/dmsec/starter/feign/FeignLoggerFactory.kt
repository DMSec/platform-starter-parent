package br.com.dmsec.starter.feign

import feign.logger
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.openfeign.FeignLoggerFactory
import org.springframework.stereotype.Component
import java.io.BufferedReader
import org.apache.logging.log4j.Logger as Logger4J

@Component
class FeignLoggerFactory : FeignLoggerFactory {

    @Value("\${feign.logger.mask-sensitive-data:false}")
    private val mustMaskSensitiveData: Boolean = false

    private val logger: Logger4J = LogManager.getLogger(this::class)

    override fun create(type: Class<*>): Logger {

        var maskBodyFields = emptySet<String>()
        var maskHeaderFields = emptySet<String>()
        if(mustMaskSensitiveData) {
            maskBodyFields = loadFieldToMask("feign-logger-masked-fields-body.txt")
            maskHeaderFields = loadFieldToMask("feign-logger-masked-fields-header.txt")
        }

        return Slf4jFeignLogger(
            mustMaskSensitiveData = mustMaskSensitiveData,
            maskHeaderFields = maskHeaderFields,
            maskBodyFields = maskBodyFields
        )
    }

    private fun loadFieldToMask(fileName: String): MutableSet<String> {
        val prefixLogMessage = "[${this::class.simpleName}][loadFieldToMask]"
        val items = mutableSetOf<String>()
        try {
            this::class.java.ClassLoader.getResourceAsStream(fileName).use { inputStream ->
                BufferedReader(InputStreamReader(inputStream)).use { reader ->
                    reader.forEachLine {
                        if(!app.startsWith("#")) items.add(app)
                    }
                }
            }
        } catch (e: Exception){
            logger.error("$prefixLogMessage - error reading file $fileName")
        }
        return items
    }
}