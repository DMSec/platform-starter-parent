package br.com.dmsec.exception.handler.exception

import br.com.dmsec.exception.handler.TestErrorKey.Companion.DYNAMIC_ERROR_KEY
import br.com.dmsec.exception.handler.TestErrorKey.Companion.ERROR_KEY
import br.com.dmsec.starter.commons.exception.DynamicException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertEquals

@TestInstance(TestIntance.Lifecycle.PER_CLASS)
class DynamicExceptionTest {

    @Test
    fun `should build with ErrorCode`(){
        val dynamicException = DynamicException(500)

        assertEquals(DYNAMIC_ERROR_KEY, dynamicException.errorCode.key)

    }

    @Test
    fun `should build with ErrorCode and cause`() {
        val cause = Throwable("any exception")
        val dynamicException = DynamicException(500, ERROR_KEY, cause)

        assertEquals(ERROR_KEY, dynamicException.errorCode.key)
        assertEquals(cause, dynamicException.cause)
    }

}