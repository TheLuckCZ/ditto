package cz.luck.ditto

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class Config {

    @Bean
    open fun objectMapper(): ObjectMapper {
        return ObjectMapper().apply {
            // configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
            // Add any additional configurations here
        }
    }
}
