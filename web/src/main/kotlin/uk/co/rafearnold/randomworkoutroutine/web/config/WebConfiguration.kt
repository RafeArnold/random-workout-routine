package uk.co.rafearnold.randomworkoutroutine.web.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry

@Configuration
open class WebConfiguration : WebMvcConfigurer {
    override fun addViewControllers(registry: ViewControllerRegistry) {
        registry.addViewController("/{x:[\\w\\-]+}")
                .setViewName("forward:/")
        registry.addViewController("/{x:[\\w\\-]+}/")
                .setViewName("forward:/")
        registry.addViewController("/{x:^(?!api$).*$}/**/{y:[\\w\\-]+}")
                .setViewName("forward:/")
    }
}
