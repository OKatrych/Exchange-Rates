package eu.okatrych.data.source.remote.model

import com.squareup.moshi.Json
import eu.okatrych.domain.model.Currency
import eu.okatrych.domain.model.RateValue
import org.threeten.bp.LocalDateTime

data class JsonExchangeRate(
    @Json(name = "base")
    val baseCurrency: Currency,
    @Json(name = "start_at")
    val startDate: String, // 2018-01-01
    @Json(name = "end_at")
    val endDate: String, // 2018-01-01
    @Json(name = "rates")
    val rates: List<RateValue>,
    @Json(name = "error")
    val error: String? = null,
    @Transient
    val timestamp: LocalDateTime = LocalDateTime.now()
)