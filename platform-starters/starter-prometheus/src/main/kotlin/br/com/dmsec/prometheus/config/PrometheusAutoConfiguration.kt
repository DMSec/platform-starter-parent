package br.com.dmsec.prometheus.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource

@Configuration
@ConditionalOnProperty(value = ["app.prometheus.enable"], havingValue ="true", matchIfMissing = true)
@PropertySource("classpath:prometheus.properties")
class PrometheusAutoConfiguration