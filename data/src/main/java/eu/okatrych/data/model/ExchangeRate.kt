package eu.okatrych.data.model

import eu.okatrych.domain.model.Currency
import eu.okatrych.domain.model.RateValue
import org.threeten.bp.LocalDateTime

data class ExchangeRate(
    val baseCurrency: Currency,
    val startDate: String?, // 2018-01-01
    val endDate: String?, // 2018-01-01
    val rates: List<RateValue>?,
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val error: String? = null
)