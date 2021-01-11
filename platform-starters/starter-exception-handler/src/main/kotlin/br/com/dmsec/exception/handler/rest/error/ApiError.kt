package br.com.dmsec.exception.handler.rest.error

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonAutoDetect.visibility.ANY
import com.fasterxml.jackson.annotation.JsonInclude

@JsonAutoDetect(fieldVisibility = ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class ApiError(val field: String?, val message: String?)