package br.com.dmsec.exception.handler.rest.error

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonAutoDetect.visibility.ANY
import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.validation.FieldError

/**
 *
 * Example 1
 *  {
 *      "status": 422,
 *      "code": "error-1",
 *      "message": "Any Business Exception",
 *      "title": "Any Title",
 *      "type": "BusinessException"
 *  }
 *
 *
 * Example 2
 *
 * {
 *      "errors": [
 *          {"field": "error-1", "message": "some, hopefully localized, error message"},
 *          {"filed": "error-2", "message": "yet another message"}
 *      ]
 * }
 *
 *
 */

@JsonAutoDetect(fieldVisibility = ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class ErrorMessageResponse(
    var status: Int? = null,
    val code: String? = null,
    val message: String?,
    val title: String? = null,
    val type: String? = null
) {

    var errors: MutableList<ApiError>? = null

    constructor(
        status: Int? = null,
        code: String? = null,
        message: String? = null,
        title: String? = null,
        type: String? = null,
        errors: List<FieldError>
    ) : this(status, code, message, title, type){
        this.addErrors(errors)
    }

    private fun addErrors(fieldErrors: List<FieldError>){
        this.errors = mutableListOf()
        val errorsPerField = fieldErrors.groupBy(keySelector = { e.field}
        , valueTransform = { e.defaultMessage })
        errorsPerField.forEach {
            addApiError(e.key, e.value)
        }
    }

    private fun addApiError(field: String, messages: List<String?>){
        messsages.forEach {
            errors?.add(ApiError(field, e))
        }
    }

}