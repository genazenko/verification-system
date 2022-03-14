package com.sunfinance.group.verification.system.notification.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

open class RestProperties {
    lateinit var endpoint: String
    lateinit var timeout: Duration
    var poolSize: Int = 15
    var retryCount: Int = 3
}

@ConfigurationProperties(prefix = "rest.template")
class TemplateProperties : RestProperties()

@ConfigurationProperties(prefix = "rest.gotify")
class GotifyProperties : RestProperties() {
    lateinit var token: String
}
