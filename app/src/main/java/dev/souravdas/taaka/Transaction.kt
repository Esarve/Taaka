package dev.souravdas.taaka

import org.threeten.bp.LocalDateTime
import org.threeten.bp.OffsetDateTime
import java.util.Date

/**
 * Created by Sourav
 * On 4/15/2023 12:36 PM
 * For Taaka
 */
data class Transaction(val id: Long, val metadata: Metadata, val amount: Double?, val txnTo: String?, val txnFrom: String?, val txnDate: LocalDateTime?, val accountBalance: Double?)
