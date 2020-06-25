package eu.okatrych.domain.entity

import java.time.LocalDate
import java.time.LocalDateTime

data class ExchangeRate(
    val baseCurrency: Currency,
    val date: LocalDate,
    val rates: List<RateValue>,
    val timestamp: LocalDateTime
)