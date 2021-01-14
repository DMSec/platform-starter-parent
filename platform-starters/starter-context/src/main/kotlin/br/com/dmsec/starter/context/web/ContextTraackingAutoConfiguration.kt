package br.com.dmsec.starter.context.web.config

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(TrackingConfig::class)
class ContextTraackingAutoConfiguration