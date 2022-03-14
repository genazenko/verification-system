package com.sunfinance.group.verification.system.notification.configuration

import com.sunfinance.group.verification.system.notification.model.constant.Channel
import com.sunfinance.group.verification.system.notification.service.DispatchContentService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DispatchBeanConfiguration {

    @Bean
    fun dispatchServiceMap(dispatchContentServices: List<DispatchContentService>): Map<Channel, DispatchContentService> =
        dispatchContentServices.associateBy { it.getChannel() }
}
