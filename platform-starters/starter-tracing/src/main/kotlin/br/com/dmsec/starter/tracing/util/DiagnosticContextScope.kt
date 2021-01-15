package br.com.dmsec.starter.tracing.util

import io.opentracing.Scope
import io.opentracing.Span
import io.opentracing.SpanContext
import br.com.dmsec.starter.tracing.util.DiagnostiContextScopeManager.Companion.CORRELATION_ID
import br.com.dmsec.starter.tracing.util.DiagnostiContextScopeManager.Companion.SPAN_ID
import br.com.dmsec.starter.tracing.util.DiagnostiContextScopeManager.Companion.TRACE_ID


class DiagnosticContextScope private constructor(
    private val scopeManager: DiagnosticContextScopeManager,
    private val wrapped: Span,
    private val toRestore: DiagnosticContextScope?
): Scope {

    constructor(scopeManager: DiagnosticContextScopeManager, wrapped: Span) : this(
        scopeManager,
        wrapped,
        scopeManager.getTLsScope().get()
    ) {
        this.scopeManager.getTLsScope.set(this)
        injectMdc(wrapped.context())
    }

    override fun close() {
        if (scopeManager.getTLsScope().get() != this) {
            return
        }
        scopeManager.getTLsScope().set(toRestore)

        if (toRestore?.wrapped != null) {
            injectMdc(toRestore.wrapped.context())
        } else {
            cleanMdc(wrapped.context())
        }
    }

    fun span(): Span {
        return wrapped
    }

    private fun injectMdc(context: SpanContext){
        mdcReplace(TRACE_ID, context.toTraceId())
        mdcReplace(SPAN_ID, context.toSpanId())
        context.baggageItems().forEach {
            var key = e.key
            if (key == CORRELATION_ID.toLowerCase()) key = CORRELATION_ID
            mdcReplace(key, e.value)
        }
    }

}