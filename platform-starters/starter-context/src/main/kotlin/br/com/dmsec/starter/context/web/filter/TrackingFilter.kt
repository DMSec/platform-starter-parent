package br.com.dmsec.starter.context.web.filter

import io.opentracing.util.GlobalTracer
import br.com.dmsec.context.Context
import br.com.dmsec.context.ContextHolder
import br.com.dmsec.context.utils.ContextConstants.CORRELATION_ID_FIELD_NAME
import br.com.dmsec.context.utils.ContextConstants.CORRELATION_ID_HEADER
import br.com.dmsec.context.utils.ContextConstants.REQUEST_ID_FIELD_NAME
import br.com.dmsec.context.utils.ContextConstants.REQUEST_ID_HEADER
import org.slf4j.MDC
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class TrackingFilter : Filter {

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpServletRequest = request as HttpServletRequest

        val correlationId = httpServletRequest.getHeader(CORRELATION_ID_HEADER)
        val context = ContextHolder.getContext()

        context.correlationId = correlationId

        this.trySetResponseHeader(response, context)

        MDC.put(CORRELATION_ID_FIELD_NAME, context.correlationId)
        MDC.put(REQUEST_ID_FIELD_NAME, context.requestId)

        this.trySetCorrelationIdInBaggage(context.correlationId)

        try {
            chain.doFilter(request, response)
        } finally {
            MDC.clear()
            ContextHolder.resetContext()
        }
    }

    private fun trySetResponseHeader(response: ServletResponse, context: Context) {
        try {
            response as HttpServletResponse
            with(response) {
                addHeader(CORRELATION_ID_HEADER, context.correlationId)
                addHeader(REQUEST_ID_HEADER, context.requestId)
            }
        } catch (ignored: Exception) {

        }
    }

    private fun trySetCorrelationIdInBaggage(correlationId: String?) {
        try {
            if (GlobalTracer.isRegistered()) {
                val span = GlobalTracer.get().activeSpan()
                span.setBaggageItem(CORRELATION_ID_FIELD_NAME, correlationId)
            }
        } catch (ignored: Exception) {

        }
    }
}

