package com.sunfinance.group.verification.system.notification.repository

import com.sunfinance.group.verification.system.notification.model.constant.Status
import com.sunfinance.group.verification.system.notification.model.entity.Notification
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import java.time.LocalDateTime
import javax.persistence.LockModeType

interface NotificationRepository : JpaRepository<Notification, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findByStatusAndTimeToProcessBeforeOrderByTimeToProcessAsc(
        status: Status,
        timeToProcess: LocalDateTime,
        pageable: Pageable
    ): List<Notification>?
}
