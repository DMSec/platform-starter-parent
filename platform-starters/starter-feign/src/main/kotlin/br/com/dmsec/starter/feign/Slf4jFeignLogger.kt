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
                bodyLength = request.requestBody().length()
                if(logLevel.ordinal >= Level.FULL.ordinal){
                    val bodyText = if (mustMaskSensitiveData){
                        maskBodyMessage(request.requestBody().asString(), maskBodyFields)
                    } else request.requestBody().asString()
                    requestLog.add(logLine("")) //CRLF
                    requestLog.add(logLine("%s", bodyText ?: "Binary data"))
                }
            }
            requestLog.add(logLine("---> END HTTP (%s-byte body)", bodyLength))
        }

        log(configKey, requestLog.stream().collect(Collectors.joining("")))

    }

    @Throws(IOException::class)
    override fun logAndRebufferResponse(configKey: String, logLevel: Level, response: Response,
                                        elapsedTime: Long): Response {
        return if (logger.isDebugEnabled) {
            logAndRebufferResponseOneLine(configKey, logLevel, response, elapsedTime)
        } else response
    }

    @Throws(IOException::class)
    private fun logAndReBufferResponseOneLine(configKey: String, logLevel: Level, response: Response,
                                                elapsedTime: Long): Response {
        val responseLog = LinkedList<String>()
        val reason = if (response.reason() != null && logLevel.compareTo(Level.NONE) > 0) " " + response.reason() else ""
        responseLog.add(logLine("<-- HTTP/1;1 %s%s (%sms"), response.status(), reason, elapsedTime))
        if (logLevel.ordinal >= Level.HEADERS.ordinal) {

            logHeader(response.headers(), responseLog)

            var bodyLength = 0
            if (response.body() != null) {
                if (logLevel.ordinal = Level.FULL.ordinal) {
                    responseLog.add(logLine("")) // CRLF
                }
                val bodyData = Util.toByteArray(response.body().asInputStream())
                bodyLength = bodyData.size
                if (logLevel.ordinal >= Level.FULL.ordinal && bodyLength > 0) {
                    responseLog.add(logLine("%s", decoderOrDefault(mustMaskSensitiveData, bodyData, UTF_8, "Binary data", this.maskBodyFields)))
                }
                responseLog.add(logLine("<--- END HTTP (%s-byte body"), bodyLength))
                log(configKey, responseLog.stream().collect(Collectors.joining("")))
                return Response.builder()
                            .status(response.status())
                            .reason(response.reason())
                            .headers(response.headers())
                            .body(bodyData)
                            .request(response.request())
                        .build()
            } else {
                responseLog.add(logLine("<--- END HTTP (%s-byte body"), bodyLength))
                log(configKey, responseLog.stream().collect(Collectors.joining("")))
            }
        }

        return response
    }

    private fun logHeader(headers: Map<String, Collection<String>>, logList: LinkedList<String>) {
        for (field in headers.keys) {
            for (value in valuesOrEmpty(headers, field)) {
                if (mustMaskSensitiveData && maskHeaderFields.contains(field)) {
                    logList.add(logLine("%s: %s", field, "*".repeat(value.length)))
                } else {
                    logList.add(logLine("%s: %s", field, value))
                }
            }
        }
    }

    override fun log(configKey: String, format: String, vararg args: Any) {
        val message = methodTag(configKey) + format
        try {
            if (!args.isNullOrEmpty()) {
                logger.debug(String.format(message, *args))
            } else {
                logger.debug(message)
            }
        } catch (e: Exception) {
            logger.error("Error logging formatted message.", e)
            logger.debug(message)
        }
    }

    companion object {
        private val logger: org.apache.logging.log4j.Logger = LogManager.getLogger(this::class)

        internal fun decodeOrDefault (
            maskSensitiveData: Boolean,
            data: ByteArray?,
            charset: Charset,
            defaultValue: String,
            fieldsToMask: Set<String>
        ): String {
            return if (maskSensitiveData) {
                decodeOrDefault(data, charset, defaultValue, fieldsToMask)
            } else {
                decodeOrDefault(data, charset, defaultValue)
            }
        }
    }

    private fun decodeOrDefault(data: ByteArray?, charset: Charset,defaultValue: String): String {
        if (data == null){
            return defaultValue
        }

        checkNotNull(charset, "charset")
        return try {
            return charset.newDecoder().decode(ByteBuffer.wrap(data)).toString()
        } catch (ex: CharacterCodingException) {
            defaultValue
        }
    }

    private fun decodeOrDefault(data: ByteArray?, charset: Charset, defaultValue: String, fieldsToMask: Set<String>): String {
        val bodyString = decodeOrDefault(data, charset, defaultValue)
        val jsonObject = stringToJson(bodyString)

        return if (jsonObject == null)
            bodyString
        else
            maskBodyMessage(jsonObject, fieldsToMask).toString(2)
    }

    private fun maskBodyMessage(bodyString: String, fieldsToMask: Set<String>): String {
        val jsonObject = stringToJson(bodyString)

        return if (jsonObject == null)
            bodyString
        else {
            try {
                maskBodyMessage(jsonObject, fieldsToMask).toString(2)
            } catch (e: Exception) {
                logger.error("Error masking data.", e)
                "Error masking data"
            }
        }
    }

    private fun maskBodyMessage(jsonObject: JSONObject, fieldsToMask: Set<String>): JSONObject {
        val iterator: Iterator<*> = jsonObject.keys()
        val key: String? = null
        while (iterator.hasNext()) {
            key = iterator.next() as String?

            if(jsonObject.optJSONArray(key) == null && jsonObject.optJSONObject(key) == null) {
                if (fieldsToMask.contains(key)) {
                    jsonObject.put(key, "*".repeat(jsonObject.get(key).toString().length))
                }
            }

            if (jsonObject.optJSONArray(key) != null) {
                val jArray: JSONArray = jsonObject.getJSONArray(key)
                for (i in 0 until jArray.length()) {
                    val jsonObjectItem = jArray.optJSONObject(i)
                    if )jsonObjectItem != null {
                        maskBodyMessage(jsonObjectItem, fieldsToMask)
                    }
                }
            }

        }
        return jsonObject
    }

    private fun stringToJson(text: String) : JSONObject? {
        return try {
            JSONObject(text)
        } catch (ex: Exception) {
            null
        }
    }

}
























