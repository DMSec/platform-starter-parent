package br.com.dmsec.starter.feign.config

import br.com.dmsec.starter.feign.interceptor.HeaderFeignInterceptor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FeignInterceptorConfig {

    @Bean
    fun headersFeignInterceptor(): HeadersFeignInterceptor = HeadersFeignInterceptor()

}