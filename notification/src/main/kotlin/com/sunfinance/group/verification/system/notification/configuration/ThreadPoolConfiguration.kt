package com.sunfinance.group.verification.system.notification.configuration

import com.sunfinance.group.verification.system.notification.configuration.properties.NewPollerProperties
import com.sunfinance.group.verification.system.notification.configuration.properties.RenderedPollerProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.TaskScheduler
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler

@Configuration
class ThreadPoolConfiguration {
    @Bean("NEW_NOTIFICATION_POOL")
    fun fnsInnPool(newPollerProperties: NewPollerProperties): TaskScheduler {
        val taskScheduler = ThreadPoolTaskScheduler()
            .apply {
                poolSize = newPollerProperties.poolSize
            }

        taskScheduler.setThreadNamePrefix(newPollerProperties.threadNamePrefix)
        taskScheduler.initialize()

        return taskScheduler
    }

    @Bean("RENDERED_NOTIFICATION_POOL")
    fun pollerTaskScheduler(renderedPollerProperties: RenderedPollerProperties): TaskScheduler {
        val taskScheduler = ThreadPoolTaskScheduler()
            .apply {
                poolSize = renderedPollerProperties.poolSize
            }

        taskScheduler.setThreadNamePrefix(renderedPollerProperties.threadNamePrefix)
        taskScheduler.initialize()

        return taskScheduler
    }
}
