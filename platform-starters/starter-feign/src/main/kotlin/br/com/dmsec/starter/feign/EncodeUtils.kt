package br.com.dmsec.starter.feign

import java.io.UnsupportedEncodingException
import java.net.URLDecoder

object EncodeUtils {

    fun decode(str: String): String {
        try{
            return URLDecoder.decode(str, "UTF-8")

        } catch (e: java.io.UnsupportedEncodingException){
            throw RuntimeException(e)
        }
    }
}