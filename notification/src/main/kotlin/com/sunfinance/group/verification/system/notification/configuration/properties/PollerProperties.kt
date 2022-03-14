package com.sunfinance.group.verification.system.notification.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("poller.new")
class NewPollerProperties : CommonPollerConfig()

@ConfigurationProperties("poller.rendered")
class RenderedPollerProperties : CommonPollerConfig()

open class CommonPollerConfig {
    lateinit var threadNamePrefix: String
    var poolSize: Int = 15
    var batchSize: Int = 10
}
