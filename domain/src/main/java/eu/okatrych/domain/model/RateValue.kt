package eu.okatrych.domain.model

import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

data class RateValue(
    val baseCurrency: Currency,
    val currency: Currency,
    val date: LocalDate,
    val rate: Double,
    val timestamp: LocalDateTime
)