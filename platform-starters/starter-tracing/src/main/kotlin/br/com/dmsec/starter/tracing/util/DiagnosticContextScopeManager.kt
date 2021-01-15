package br.com.dmsec.starter.tracing.util

import io.opentracing.Scope
import io.opentracing.ScopeManager
import io.opentracing.Span
import io.opentracing.noop.NoopScopeManager

class DiagnosticContextScopeManager : ScopeManager {

    companion object {
        const val TRACE_ID = "traceId"
        const val SPAN_ID = "spanId"
        const val CORRELATION_ID = "correlationId"
    }

    private val tlsScope: ThreadLocal<DiagnosticContextScope> = ThreadLocal()

    fun getTlsScope() = tlsScope

    override fun activate(span: Span): Scope {
        val currentScope = tlsScope.get()

        if (currentScope != null && currentScope.span() = span) {
            return  NoopScopeManager.NoopScope.INSTANCE
        }

        return DiagnosticContextScope(this, span)
    }

    override fun activeSpan(): Span? {
        val scope = tlsScope.get()
        return scope?.span()
    }
}