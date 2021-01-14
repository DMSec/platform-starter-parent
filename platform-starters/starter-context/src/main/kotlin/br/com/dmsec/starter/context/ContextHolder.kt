package br.com.dmsec.starter.context

import kotlin.concurrent.getOrSet

object ContextHolder {
    private val CONTEXT_THREAD_LOCAL: ThreadLocal<Context> = ThreadLocal()

    fun getContextHolder(): ThreadLocal<Context> = CONTEXT_THREAD_LOCAL

    fun getContext(): Context = CONTEXT_THREAD_LOCAL.getOrSet {Context()}

    fun resetContext() {
        CONTEXT_THREAD_LOCAL.remove()
    }

    fun isEmpty(): Boolean = CONTEXT_THREAD_LOCAL.get() == null
}