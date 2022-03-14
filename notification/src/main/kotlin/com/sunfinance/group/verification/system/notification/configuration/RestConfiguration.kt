package com.sunfinance.group.verification.system.notification.configuration

import com.sunfinance.group.verification.system.notification.configuration.properties.GotifyProperties
import com.sunfinance.group.verification.system.notification.configuration.properties.RestProperties
import com.sunfinance.group.verification.system.notification.configuration.properties.TemplateProperties
import org.apache.http.HttpHost
import org.apache.http.conn.routing.HttpRoute
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.ClientHttpRequestFactory
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestTemplate

@Configuration
class RestConfiguration {

    @Bean("templateRestTemplate")
    fun templateRestTemplate(
        restTemplateBuilder: RestTemplateBuilder,
        templateProperties: TemplateProperties,
        @Qualifier("templateConnectionPoolConfig")
        tcrmConnectionPoolConfig: PoolingHttpClientConnectionManager
    ): RestTemplate = restTemplateBuilder.setConnectTimeout(templateProperties.timeout)
        .setReadTimeout(templateProperties.timeout)
        .requestFactory {
            getCustomClientHttpRequestFactory(
                templateProperties,
                tcrmConnectionPoolConfig
            )
        }
        .build()

    @Bean("gotifyRestTemplate")
    fun gotifyRestTemplate(
        restTemplateBuilder: RestTemplateBuilder,
        gotifyProperties: GotifyProperties,
        @Qualifier("gotifyConnectionPoolConfig")
        gotifyConnectionPoolConfig: PoolingHttpClientConnectionManager
    ): RestTemplate = restTemplateBuilder.setConnectTimeout(gotifyProperties.timeout)
        .setReadTimeout(gotifyProperties.timeout)
        .requestFactory {
            getCustomClientHttpRequestFactory(
                gotifyProperties,
                gotifyConnectionPoolConfig
            )
        }
        .build()

    private fun getCustomClientHttpRequestFactory(
        restProperties: RestProperties,
        connectionPoolConfig: PoolingHttpClientConnectionManager
    ): ClientHttpRequestFactory {
        connectionPoolConfig.setMaxPerRoute(
            HttpRoute(HttpHost(restProperties.endpoint)),
            restProperties.poolSize
        )

        val client = HttpClientBuilder.create()
            .setConnectionManager(connectionPoolConfig)
            .setRetryHandler(DefaultHttpRequestRetryHandler(restProperties.retryCount, true))
            .build()

        return HttpComponentsClientHttpRequestFactory(client)
    }
}
