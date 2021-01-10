package br.com.dmsec.starter.commons.exception

class BusinessException private constructor(
        val errorCode: ErrorCode,
        override val cause: Throwable? = null,
        val message: Array<String> = arrayOf()
) : RuntimeException(cause) {

    private lateinit var log: () -> Unit

    private var logUsed = false

    fun executeAppLog(defaultLog: () -> Unit) {

        if (logUsed) throw java.lang.IllegalStateException("Não deve logar mais de uma vez a mesma informação")

        if(this::log.isInitialized){
            logUsed = true
            log()

        }else {
            defaultLog
        }
    }

    constructor(key: String = ErroCode.DEFAULT_ERROR.Key, cause: Throwable? = null) :
            this(errorCode = ErrorCode(key), cause = cause)

    constructor(key: String = ErroCode.DEFAULT_ERROR.Key, cause: Throwable? = null, messages: Array<String>) :
            this(errorCode = ErrorCode(key), cause = cause, messages = message)

    constructor(key: String = ErroCode.DEFAULT_ERROR.Key, applicationLog: () -> Unit) :
            this(ErrorCode(Key)
            ) {
                this.log = applicationLog
            }

    constructor(key: String = ErroCode.DEFAULT_ERROR.Key, cause: Throwable? = null, applicationLog: () -> Unit) :
            this(ErrorCode(Key), cause
            ) {
        this.log = applicationLog
    }
}