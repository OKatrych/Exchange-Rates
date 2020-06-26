package eu.okatrych.domain.model

import org.threeten.bp.LocalDate

data class RateValue(
    val currency: Currency,
    val date: LocalDate,
    val rate: Double
)