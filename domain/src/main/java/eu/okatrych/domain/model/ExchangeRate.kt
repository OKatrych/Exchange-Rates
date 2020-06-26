package eu.okatrych.domain.model

import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

data class ExchangeRate(
    val baseCurrency: Currency,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val rates: List<RateValue>,
    val timestamp: LocalDateTime
)