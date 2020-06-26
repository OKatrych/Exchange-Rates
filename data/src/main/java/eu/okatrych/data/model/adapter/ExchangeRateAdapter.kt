package eu.okatrych.data.model.adapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import eu.okatrych.data.model.ExchangeRate
import eu.okatrych.domain.model.Currency
import eu.okatrych.domain.model.RateValue
import org.json.JSONObject
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

/**
 * JSON response from exchangeratesapi.io contains "rates" as separate objects instead of one array.
 * [ExchangeRateAdapter] will manually parse received JSONObject and build ExchangeRate.
 */
class ExchangeRateAdapter {

    /*
     * {
     *     "rates": {
     *         "2018-01-03": {
     *             "JPY": 134.97,
     *             "ILS": 4.1588
     *         },
     *         "2018-01-02": {
     *             "JPY": 135.35,
     *             "ILS": 4.1693
     *         }
     *     },
     *     "start_at": "2018-01-01",
     *     "base": "EUR",
     *     "end_at": "2018-01-03"
     * }
     */
    @FromJson
    fun exchangeRateFromJson(map: Map<String, Any>): ExchangeRate {
        val moshi = Moshi.Builder().build()
        val jsonObject = JSONObject(moshi.adapter(Map::class.java).toJson(map))

        val base = jsonObject.optString("base")
        val startDate = jsonObject.optString("start_at").takeIf { it.isNotEmpty() }
        val endDate = jsonObject.optString("end_at").takeIf { it.isNotEmpty() }
        val ratesListObject = jsonObject.optJSONObject("rates")
        val error = jsonObject.optString("error").takeIf { it.isNotEmpty() }

        val ratesList = ratesListObject?.keys()?.asSequence()?.flatMap { dateKey ->
            val rateObject = ratesListObject.getJSONObject(dateKey)
            return@flatMap rateObject.keys().asSequence().map { currencyKey ->
                val rateValue = rateObject.getDouble(currencyKey)
                RateValue(
                    date = LocalDate.parse(dateKey),
                    currency = Currency.fromString(currencyKey),
                    rate = rateValue
                )
            }
        }?.toList()

        return ExchangeRate(
            baseCurrency = Currency.fromString(base),
            startDate = startDate,
            endDate = endDate,
            rates = ratesList,
            timestamp = LocalDateTime.now(),
            error = error
        )
    }

    @ToJson
    fun exchangeRateToJson(exchangeRate: ExchangeRate): String {
        return "" // No need
    }
}