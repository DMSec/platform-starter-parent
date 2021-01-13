package br.com.dmsec.starter.feign

import feign.Request
import feign.Response
import feign.Util
import feign.Util.UTF_8
import feign.Util.checkNotNull
import feign.Util.valuesOrEmpty
import org.apache.logging.log4j.LogManager
import org.json.JSONArray
import org.json.JSONObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.charset.CharacterCodingException
import java.nio.charset.Charset
import java.util.LinkedList
import java.util.stream.Collectors

class Slf4jFeignLogger internal constructor(
    private val logger: Logger,
    private val mustMaskSensitiveData: Boolean,
    private val maskHeaderFields: Set<String>,
    private val maskBodyFields: Set<String>
) : feign.Logger(){

    @JvmOverLoads
    constructor(
        clazz: Class<*> = Slf4jFeignLogger::class.java,
        mustMaskSensitiveData: Boolean,
        maskHeaderFields: Set<String>,
        maskBodyFields: Set<String>
    ) :
            this(LoggerFactory.getLogger(clazz), mustMaskSensitiveData, maskHeaderFields, maskBodyFields) {

            }

    constructor(
        name: String,
        mustMaskSensitiveData: Boolean,
        maskHeaderFields: Set<String>,
        maskBodyFields: Set<String>
    ) :
            this(LoggerFactory,getLogger(name), mustMaskSensitiveData, maskHeaderFields, maskBodyFields) {}

    override fun logRequest(configKey: String, logLevel: Level, request: Request){
        if(logger.isDebugEnabled){
            logRequestOneLine(configKey, logLevel, request)
        }
    }

    private fun logLine(format: String, varang args: Any): String {
        val requestLog = LinkedList<String>()
        requestLog.add(logLine("---> %s %s HTTP/1.1", request.httpMethod(), EncodeUtils.decode(request.url())))

        if (logLevel.ordinal >= Level.HEADERS.ordinal) {

            logHeader(request.headers(), requestLog)

            var bodyLength = 0
            if (request.requestBody() != null){
                
            }
        }

    }
}