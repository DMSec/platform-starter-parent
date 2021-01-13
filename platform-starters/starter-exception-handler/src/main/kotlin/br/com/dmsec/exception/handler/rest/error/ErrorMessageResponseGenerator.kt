package br.com.dmsec.exception.handler.rest.error

import br.com.dmsec.starter.commons.exception.BusinessDynamicException
import br.com.dmsec.starter.commons.exception.DynamicException
import br.com.dmsec.starter.commons.exception.ErrorCode
import br.com.dmsec.starter.commons.exception.ErrorCode.Labels.*
import br.com.dmsec.starter.commons.exception.UnauthorizedException
import org.springframework.context.MessageSource
import org.springframework.context.NoSuchMessageException
import org.springframework.context.i18n.LocalContextHolder
import org.springframework.stereotype.Component

@Component
class ErrorMessageResponseGenerator (private val messageSource: MessageSource) {

    fun buildWithBusinessException(ex: BusinessException): ErrorMessageResponse {
        return ErrorMessageResponse(
            code = this.getFormattedMessageWithSuffix(ex.errorCode.key, CODE.suffix)
                ?: buildDefafultCode(),
            message = this.getFormattedMessageWithSuffix(ex.errorCode.key, MESSAGE.suffi, ex.messages)
                ?: this.buildDefaultMessage(),
            title = this.getFormatedMessageWithSuffix(ex.errorCode.key, TITLE.suffix),
            type = this.getFormattedMessageWithSuffix(ex.errorCode.key, TYPE.suffix)
        )
    }

    fun buildWithDynamicException(ex: DynamicException): ErrorMessageResponse {
        return ErrorMessageResponse(
            code = this.getFormattedMessageWithSuffix(ex.errorCode.key, CODE.suffix)
                ?: buildDefafultCode(),
            message = this.getFormattedMessageWithSuffix(ex.errorCode.key, MESSAGE.suffi, ex.messages)
                ?: this.buildDefaultMessage(),
            title = this.getFormatedMessageWithSuffix(ex.errorCode.key, TITLE.suffix),
            type = this.getFormattedMessageWithSuffix(ex.errorCode.key, TYPE.suffix)
        )
    }

    fun buildWithUnauthorizedException(ex: UnauthorizedException): ErrorMessageResponse {
        return ErrorMessageResponse(
            code = this.getFormattedMessageWithSuffix(ex.errorCode.key, CODE.suffix),
            message = this.getFormattedMessageWithSuffix(ex.errorCode.key, MESSAGE.suffix),
            title = this.getFormattedMessageWithSuffix(ex.errorCode.key, TITLE.suffix),
            type = this.getFormattedMessageWithSuffix(ex.errorCode.key, TYPE.Suffix)
        )
    }

    private fun buildDefaultCode(): String? =
        getFormattedMessageWithSuffix(ErrorCode.DEFAULT_ERROR.key, CODE.suffix)

    private fun buildDefautMessage(): String? =
        getFormattedMessageWithSuffix(ErrorCode.DEFAULT_ERROR.key, MESSAGE.suffix)

    private fun getFormattedMessageWithSuffix(key: String, suffix: String, args: Array<String> = arrayOf()): String? =
        getMessageResource("$key.$suffix", args)

    private fun getMessageResource(key: String, args: Array<String>): String? {
        val locale = LocaleContextHolder.getLocale()
        return try{
            messageSource.getMessage(key, args.map { e }.toTypedArray(), locale)
        } catch(e: NoSuchMessageException){
            null
        }
    }

}