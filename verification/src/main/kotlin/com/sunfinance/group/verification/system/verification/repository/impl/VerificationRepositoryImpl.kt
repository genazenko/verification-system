package com.sunfinance.group.verification.system.verification.repository.impl

import com.sunfinance.group.verification.system.verification.exception.DuplicateVerificationException
import com.sunfinance.group.verification.system.verification.model.constant.IdentityType
import com.sunfinance.group.verification.system.verification.model.constant.VerificationStatus
import com.sunfinance.group.verification.system.verification.model.entity.Verification
import com.sunfinance.group.verification.system.verification.repository.VerificationRepository
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.set
import org.springframework.stereotype.Repository
import java.sql.JDBCType
import java.sql.ResultSet
import java.util.Optional

@Repository
class VerificationRepositoryImpl(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) : VerificationRepository {
    override fun findById(id: String): Optional<Verification> {
        return try {
            Optional.of(
                jdbcTemplate.queryForObject(
                    FIND_BY_ID_SQL,
                    MapSqlParameterSource().addValue(ID_COLUMN, id),
                    verificationEntityRowMapper
                )!!
            )
        } catch (ex: EmptyResultDataAccessException) {
            Optional.empty()
        }
    }

    override fun createVerification(verification: Verification): String? {
        val sqlParams = MapSqlParameterSource().apply {
            set(ID_COLUMN, JDBCType.VARCHAR.vendorTypeNumber, verification.id)
            set(CODE_COLUMN, JDBCType.VARCHAR.vendorTypeNumber, verification.code)
            set(USER_AGENT_COLUMN, JDBCType.VARCHAR.vendorTypeNumber, verification.userAgent)
            set(CLIENT_IP_COLUMN, JDBCType.VARCHAR.vendorTypeNumber, verification.clientIp)
            set(IDENTITY_COLUMN, JDBCType.VARCHAR.vendorTypeNumber, verification.identity)
            set(TYPE_COLUMN, JDBCType.VARCHAR.vendorTypeNumber, verification.type.name)
            set(
                EXPIRATION_TIME_COLUMN,
                JDBCType.TIMESTAMP.vendorTypeNumber,
                verification.expirationTime
            )
            set(STATUS_COLUMN, JDBCType.VARCHAR.vendorTypeNumber, VerificationStatus.PENDING.name)
            set(CONFIRMATION_ATTEMPT_COLUMN, JDBCType.INTEGER.vendorTypeNumber, 0)
        }
        val updated = jdbcTemplate.update(INSERT_SQL, sqlParams)
        return if (updated == 0) {
            throw DuplicateVerificationException(DUPLICATE_ERROR_MESSAGE)
        } else {
            verification.id
        }
    }

    override fun updateVerificationStatus(id: String, status: VerificationStatus): Boolean {
        return jdbcTemplate.update(
            UPDATE_STATUS_SQL,
            MapSqlParameterSource()
                .addValue(STATUS_COLUMN, status.name)
                .addValue(ID_COLUMN, id)
        ) == 1
    }

    override fun increaseAttempt(id: String): Boolean {
        return jdbcTemplate.update(
            UPDATE_ATTEMPTS_SQL,
            MapSqlParameterSource()
                .addValue(ID_COLUMN, id)
        ) == 1
    }

    val verificationEntityRowMapper = { rs: ResultSet, _: Int ->
        Verification(
            rs.getString(ID_COLUMN),
            rs.getString(CODE_COLUMN),
            rs.getString(USER_AGENT_COLUMN),
            rs.getString(CLIENT_IP_COLUMN),
            rs.getString(IDENTITY_COLUMN),
            IdentityType.valueOf(rs.getString(TYPE_COLUMN)),
            rs.getTimestamp(EXPIRATION_TIME_COLUMN).toLocalDateTime(),
            VerificationStatus.valueOf(rs.getString(STATUS_COLUMN)),
            rs.getInt(CONFIRMATION_ATTEMPT_COLUMN)
        )
    }

    companion object {
        private const val ID_COLUMN = "id"
        private const val CODE_COLUMN = "code"
        private const val USER_AGENT_COLUMN = "user_agent"
        private const val CLIENT_IP_COLUMN = "client_ip"
        private const val IDENTITY_COLUMN = "identity"
        private const val TYPE_COLUMN = "type"
        private const val EXPIRATION_TIME_COLUMN = "expiration_time"
        private const val STATUS_COLUMN = "status"
        private const val CONFIRMATION_ATTEMPT_COLUMN = "confirmation_attempt"
        private const val FIND_BY_ID_SQL = """
            SELECT * FROM verification WHERE $ID_COLUMN = :$ID_COLUMN FOR UPDATE SKIP LOCKED
        """

        private const val INSERT_SQL = """
            INSERT INTO verification($ID_COLUMN, $CODE_COLUMN, $USER_AGENT_COLUMN, $CLIENT_IP_COLUMN, $IDENTITY_COLUMN, $TYPE_COLUMN, $EXPIRATION_TIME_COLUMN, $STATUS_COLUMN, $CONFIRMATION_ATTEMPT_COLUMN)
            SELECT :$ID_COLUMN, :$CODE_COLUMN, :$USER_AGENT_COLUMN, :$CLIENT_IP_COLUMN, :$IDENTITY_COLUMN, :$TYPE_COLUMN, :$EXPIRATION_TIME_COLUMN, :$STATUS_COLUMN, :$CONFIRMATION_ATTEMPT_COLUMN
            WHERE ((SELECT count(id)
                        FROM verification
                        WHERE $IDENTITY_COLUMN = :$IDENTITY_COLUMN AND $TYPE_COLUMN = :$TYPE_COLUMN AND $STATUS_COLUMN = 'PENDING' AND $EXPIRATION_TIME_COLUMN > CURRENT_TIMESTAMP) = 0);
            """

        private const val UPDATE_STATUS_SQL = """
            UPDATE verification SET $STATUS_COLUMN = :$STATUS_COLUMN WHERE $ID_COLUMN = :$ID_COLUMN
        """

        private const val UPDATE_ATTEMPTS_SQL = """
            UPDATE verification SET $CONFIRMATION_ATTEMPT_COLUMN = $CONFIRMATION_ATTEMPT_COLUMN + 1 WHERE $ID_COLUMN = :$ID_COLUMN
        """

        private const val DUPLICATE_ERROR_MESSAGE = "Verification with given subject already exists"
    }
}
