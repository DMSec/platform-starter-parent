package br.com.dmsec.exception.handler.exception

import br.com.dmsec.exception.handler.TestErrorKey.Companion.ERROR_KEY
import br.com.dmsec.starter.commons.exception.BusinessException

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BusinessExceptionTest {

    @Test
    fun `should build with ErrorCode`(){
        val businessException = BusinessException(ERROR_KEY)

        assertEquals(ERROR_KEY, businessException.errorCode.key)

    }

    @Test
    fun `should build with ErrorCode and cause`() {
        val cause = Throwable("any exception")
        val businessException = BusinessException(ERROR_KEY, cause)

        assertEquals(ERROR_KEY, businessException.errorCode.key)
        assertEquals(cause, businessException.cause)
    }
}