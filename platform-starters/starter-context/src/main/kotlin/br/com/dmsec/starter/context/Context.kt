package br.com.dmsec.starter.context

import br.com.dmsec.starter.context.utils.generate

class Context {
    var requestId: String? = null
    get() {
        if (field == null || field!!.trim().isEmpty()) {
            field = generate()
        }
        return field!!
    }

    var correlationId: String? = null
        get() {
            if (field == null || field!!.trim().isEmpty()) {
                field = generate()
            }
            return field!!
        }

    var custom: MutableMap<String, Any> = mutableMapOf()

    fun clear() {
        this.requestId = null
        this.correlationId = null
        this.custom.clear()
    }

    override fun toString(): String {
        return "Context(requestId=$requestId, correlationId=$correlationId, custom=$custom"
    }
}