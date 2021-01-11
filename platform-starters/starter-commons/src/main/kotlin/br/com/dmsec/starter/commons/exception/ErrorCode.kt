package br.com.dmsec.starter.commmons.exception

class ErrorCode(val key: String){

    companion object {
        val DEFAULT_ERROR = ErrorCode("default.business.error")
        val UNAUTHORIZED_ERROR = ErrorCode("default.unathorized.error")

    }

    init {
        require(key.isNotEmpty()) { "Key can't be null or blank"}
    }

    enum class Labels(val suffix: String){
        CODE("code"),
        MESSAGE("message"),
        TITLE("title"),
        TYPE("type")
    }
}