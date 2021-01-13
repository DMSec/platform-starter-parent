package br.com.dmsec.starter.feign

import br.com.dmsec.starter.feign.config.FeignInterceptorConfig
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(FeignLoggerFactory::class, FeignInterceptorConfig::class)
class FeignAutoConfiguration