package com.sunfinance.group.verification.system.verification.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("verification")
class VerificationProperties {
    var codeLength: Int = 8
    var expirationTimeMinutes: Long = 5
    var attemptCount: Int = 5
}
