package br.com.dmsec.starter.context.web.config

import br.com.dmsec.context.web.filter.TrackingFilter
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TrackingConfig {

    @Bean
    fun trackingFilterBean(): FilterRegistrationBean<TrackingFilter> =
        FilterRegistrationBean<TrackingFilter>()
            .apply {
                this.setName("trackingFilter")
                this.filter = TrackingFilter()
                this.order = 1
            }
}