package uk.co.rafearnold.randomworkoutroutine.web.config

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.config.BeanFactoryPostProcessor
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.context.annotation.Configuration

@Configuration
open class JacksonConfiguration : BeanFactoryPostProcessor {

    override fun postProcessBeanFactory(beanFactory: ConfigurableListableBeanFactory) {
        beanFactory.getBean(ObjectMapper::class.java)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
    }
}
