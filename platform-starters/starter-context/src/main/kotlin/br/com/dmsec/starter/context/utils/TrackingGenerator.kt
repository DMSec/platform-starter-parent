package br.com.dmsec.starter.context.utils

import java.util.*

fun generate(): String = UUID.randomUUID().toString().replace("-","")