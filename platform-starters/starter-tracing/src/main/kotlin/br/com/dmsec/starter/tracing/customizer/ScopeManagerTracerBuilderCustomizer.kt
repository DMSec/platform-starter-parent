package br.com.dmsec.starter.tracing.customizer

import io.jaegertracing.internal.JaegerTracer
import io.opentracing.contrib.java.spring.jaeger.starter.TracerBuilderCustomizer
import br.com.dmsec.starter.tracing.util.DiagnosticContextScopeManager

class ScopeManagerTracerBuilderCustomizer : TracerBuilderCustomizer {

    override fun customize(builder: JaegerTracer.Builder) {
        builder.withScopeManager(DiagnosticContextScopeManager())
    }
}