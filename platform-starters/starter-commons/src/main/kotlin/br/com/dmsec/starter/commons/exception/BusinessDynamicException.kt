package br.com.dmsec.starter.commons.exception

import com.fasterxml.jackson.databind.ObjectMapper

class BusinessDynamicException (
        val statusCode: Int,
        val response: Any ) : RuntimeException() {

            constructor(statusCode: Int, responseJsonObject: String):
                    this(statusCode, try{
                        ObjectMapper().readValue(responseJsonObject, Any::class.java)
                    } catch (ex: Exception) {
                        // non json Object
                        responseJsonObject
                    })
        }