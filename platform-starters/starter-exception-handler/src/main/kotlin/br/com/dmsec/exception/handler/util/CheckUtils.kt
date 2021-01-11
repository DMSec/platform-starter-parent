package br.com.dmsec.exception.handler.util

fun isNotNullOrEmpty(str: String?): Boolean {
    if (str != null && str.trim().isNotEmpty()) {
        return true
    }

    return false
}

fun isNotNullOrEmpty(list: List<Any>?): Boolean {
    if(list != null && list.isNotEmpty()){
        return true
    }

    return false
}

fun isNotNull(any: Any?): Boolean {
    if(any != null) return true
    return false
}