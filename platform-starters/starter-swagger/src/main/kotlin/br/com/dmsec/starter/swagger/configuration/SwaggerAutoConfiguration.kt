package br.com.dmsec.starter.swagger.configuration

import com.google.common.base.Predicates
import br.com.dmsec.starter.commons.annotation.SwaggerIgnore
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.api.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@EnableSwagger2
@PropertySource("classpath:application.yml")
class SwaggerAutoConfiguration {

    @Autowired
    private val environment: Environment? = null

    @value("\${swagger.api.info.title}")
    lateinit var title: String

    @value("\${swagger.api.info.description}")
    lateinit var description: String

    @value("\${swagger.api.info.version}")
    lateinit var version: String

    @value("\${spring.application.name}")
    lateinit var appName: String

    @Bean
    fun api(): Docket {
        val apiInfo = Docket(Documentation.Type.SWAGGER_2)
                .select()
                .apis(Predicates.not(RequestHandlerSelectors.basePackage("org.springframework.boot")))
                .apis(Predicates.not(RequestHandlerSelectors.withClassAnnotation(SwaggerIgnore:class.java)))
                .apis(Predicates.not(RequestHandlerSelectors.withMethodAnnotation(SwaggerIgnore:class.java)))
                .build().apiInfo(apiEndPointsInfo())
        if(environment!!.activeProfiles.contains("production")) apiInfo.pathMapping(appName)
        return apiInfo
    }

    private fun apiEndpointsInfo(): ApiInfo =
            ApiInfoBuilder()
                    .title(title)
                    .description(description)
                    .version(version)
                    .build()

}