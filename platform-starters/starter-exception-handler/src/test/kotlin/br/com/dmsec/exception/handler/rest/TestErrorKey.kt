package br.com.dmsec.exception.handler

open class TestErrorKey protected constructor(key: String){
    companion object{
        const val ERROR_KEY = "error.key"
        const val DYNAMIC_ERROR_KEY = "default.business.error"
        const val ANOTHER_ERROR_KEY = "another.error.key"
        const val ANOTHER_ERROR_KEY_TYPE = "another.error.key.type"
        const val ANOTHER_ERROR_KEY_CODE = "another.error.key.code"
        const val ANOTHER_ERROR_KEY_TITLE = "another.error.key.title"
        const val ANOTHER_ERROR_KEY_MESSAGE = "another.error.key.message"
    }
}