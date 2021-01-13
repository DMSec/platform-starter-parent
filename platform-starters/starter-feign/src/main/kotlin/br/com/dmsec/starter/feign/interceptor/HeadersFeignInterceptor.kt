package br.com.dmsec.starter.feign.interceptor

import feign.RequestInterceptor
import feign.RequestTemplate
import br.com.dmsec.starter.context.ContextHolder
import br.com.dmsec.starter.context.utils.ContextConstants

class HeadersFeignInterceptor : RequestInterceptor {

    override fun apply(requestTemplate: RequestTemplate) {
        ContextHolder.getContext()
            .apply {
                requestTemplate.header(ContextHolderConstants.CORRELATION_ID_HEADER, this.correlationId)
            }
    }
}