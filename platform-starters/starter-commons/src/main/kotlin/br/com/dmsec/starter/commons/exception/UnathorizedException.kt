package br.com.dmsec.starter.commons.exception

data class UnathorizedException (
    val errorCode: ErroCode
        ) : RunTimeException() {

            constructor(key: String = ErrorCode.UNAUTHORIZED_ERROR.key) :
                    this(errorCode = ErrorCode(key))
        }