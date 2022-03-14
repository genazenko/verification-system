package com.sunfinance.group.verification.system.notification.model.constant

enum class Channel(
    val identityType: String,
    val slugType: String
) {
    email("email_confirmation", "email-verification"), phone(
        "mobile_confirmation",
        "mobile-verification"
    );

    companion object {
        fun getByIdentityType(type: String): Channel? {
            return values().firstOrNull { it.identityType.equals(type, true) }
        }
    }
}
