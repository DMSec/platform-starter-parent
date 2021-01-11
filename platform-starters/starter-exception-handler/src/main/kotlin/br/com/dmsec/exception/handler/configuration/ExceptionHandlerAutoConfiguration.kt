package br.com.dmsec.exception.handler.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.PropertySource

@Configuration
@ComponentScan("br.com.dmsec.exception.handler")
@PropertySource("classpath:exception-handler.properties")
class ExceptionHandlerAutoConfiguration