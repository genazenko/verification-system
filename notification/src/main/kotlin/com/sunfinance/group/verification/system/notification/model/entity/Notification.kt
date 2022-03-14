package com.sunfinance.group.verification.system.notification.model.entity

import com.sunfinance.group.verification.system.notification.model.constant.Channel
import com.sunfinance.group.verification.system.notification.model.constant.Status
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "notification")
data class Notification(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    val id: Long? = null,
    val recipient: String,
    @Enumerated(EnumType.STRING)
    val channel: Channel,
    val code: String,
    var content: String? = null,
    @Enumerated(EnumType.STRING)
    var status: Status,
    @Column(name = "time_to_process", columnDefinition = "TIMESTAMP")
    var timeToProcess: LocalDateTime,
    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    var createdAt: LocalDateTime = LocalDateTime.now(),
    @Column(name = "attempt_count")
    var attemptCount: Int = 0,
    @Column(name = "event_id")
    val eventId: String
)
