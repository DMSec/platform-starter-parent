package br.com.dmsec.exception.handler.rest

import br.com.dmsec.exception.handler.rest.error.ErrorMessageResponse
import br.com.dmsec.exception.handler.rest.error.ErrorMessageResponseGenerator
import br.com.dmsec.starter.commons.exception.BusinessDynamicException
import br.com.dmsec.starter.commons.exception.BusinessException
import br.com.dmsec.starter.commons.exception.DynamicException
import br.com.dmsec.starter.commons.exception.NotFoundException
import br.com.dmsec.starter.commons.exception.UnauthorizedException
import br.com.dmsec.starter.commons.logger.logger
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler


@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
class ApiExceptionHandler (
    val generator: ErrorMessageResponseGenerator
        ) : ResponseEntityExceptionHandler() {

    private val log = logger<ApiExceptionHandler>()

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundException(ex: NotFoundException, request: WebRequest): ResponseEntity<Any> {
        val errorResponse = ErrorMessageResponse(status = NOT_FOUND.value(), message = ex.message)

        if (log.isDebugEnabled) {
            log.debug("Not found exception handled", ex)
        } else {
            log.warn("Not found exception handled (resource: ${ex.resource}, value: ${ex.value})")
        }

        return ResponseEntity
            .status(NOT_FOUND)
            .body(errorResponse)
    }

    @ExceptionHandler(Exception::class)
    fun handleOthersExceptions(ex: Exception, request: WebRequest): ResponseEntity<Any> {
        val errorResponse =
            ErrorMessageResponse(
                status = INTERNAL_SERVER_ERROR.value(),
                message = ex.message ?: "Internal server error"
            )

        log.error("Internal server exception handled", ex)

        return handleExceptionInternal(
            ex = ex,
            body = errorResponse,
            headers = HttpHeaders(),
            request = request,
            status = INTERNAL_SERVER_ERROR
        )
    }

    @ExceptionHandler(BusinessException::class)
    @ResponseStatus(UNPROCESSABLE_ENTITY)
    fun handleBusinessException(ex: BusinessException, request: WebRequest): ErrorMessageResponse =
        ex.run { generator.buildWithBusinessException(this) }
            .apply { this.status = UNPROCESSABLE_ENTITY.value() }
            .also {
                ex.executeAppLog {
                    if (log.isDebugEnabled) {
                        log.debug("Business exception handled", ex)
                    } else {
                        log.warn("Business exception handled (code: ${e.code ?: "N/A"}, message: ${ex.message ?: "N/A"})")
                    }
                }
            }


    @ExceptionHandler(DynamicException::class)
    fun handleDynamicException(ex: DynamicException, request: WebRequest): ResponseEntity<ErrorMessageResponse> {

        return ResponseEntity
            .status(ex.statusCode)
            .body(ex.run { generator.buildWithDynamicException(this) }
                .apply { this.status = ex.statusCode }
                .also {
                    ex.executeAppLog {
                        if (log.isDebugEnabled) {
                            log.debug("Dynamic exception handled", ex)
                        } else {
                            log.warn("Dynamic exception handled (code: ${e.code ?: "N/A"}, message: ${ex.message ?: "N/A"})")
                        }
                    }
                })

    }

    @ExceptionHandler(BusinessDynamicException::class)
    fun handleBusinessDynamicException(ex: BusinessDynamicException, request: WebRequest): ResponseEntity<Any> {
        if (log.isDebugEnabled) {
            log.debug("Business Dynamic exception handled", ex)
        } else {
            log.warn("Business Dynamic exception handled (status: ${ex.statusCode})")
        }

        return ResponseEntity
            .status(ex.statusCode)
            .body(ex.response)
    }

    @ExceptionHandler(UnauthorizedException::class)
    @ResponseStatus(UNAUTHORIZED)
    fun handleUnathorizedException(ex: UnathorizedException, request: WebRequest): ErrorMessageResponse =
        ex.run { generator.buildWithUnauthorizedException(this) }
            .apply { this.status = UNAUTHORIZED.value() }
            .also {
                log.debug("Unathorized exception handled", ex)
            }

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException, headers: HttHeaders,
        status: HttpStatus, request: WebRequest
    ): ResponseEntity<Any> {
        val errorResponse = ErrorMessageResponse(
            status = status.value(),
            message = "method argument not valid",
            errors = ex.bindingResult.fieldErrors
        )
        return ResponseEntity
            .status(status)
            .body(errorResponse)
    }

    override fun handleExceptionInternal(
        ex: Exception,
        body: Any?,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        val errorResponse = ErrorMessageResponse(status = status.value(), message = ex.message ?: status.reasonPhrase)
        return ResponseEntity
            .status(status)
            .body(errorResponse)
    }

}

