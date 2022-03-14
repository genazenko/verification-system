package com.sunfinance.group.verification.system.notification.configuration

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ConnectionPoolConfiguration {

    @Bean
    @ConfigurationProperties("rest.template.pool")
    fun templateConnectionPoolConfig() = PoolingHttpClientConnectionManager()

    @Bean
    @ConfigurationProperties("rest.gotify.pool")
    fun gotifyConnectionPoolConfig() = PoolingHttpClientConnectionManager()
}
