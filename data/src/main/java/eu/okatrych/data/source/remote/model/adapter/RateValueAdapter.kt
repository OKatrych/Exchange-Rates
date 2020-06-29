package eu.okatrych.data.source.remote.model.adapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import eu.okatrych.domain.model.Currency
import eu.okatrych.domain.model.RateValue
import org.json.JSONObject
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

class RateValueAdapter {

    /*
     *     "rates": {
     *         "2018-01-03": {
     *             "JPY": 134.97,
     *             "ILS": 4.1588
     *         },
     *         "2018-01-02": {
     *             "JPY": 135.35,
     *             "ILS": 4.1693
     *         }
     *     }
     */
    @FromJson
    fun rateValueFromJson(map: Map<String, Any>): List<RateValue> {
        val moshi = Moshi.Builder().build()
        val jsonObject = JSONObject(moshi.adapter(Map::class.java).toJson(map))

        val baseCurrency = jsonObject.getString("base")
        val ratesListObject = jsonObject.getJSONObject("rates")
        val latestDate = jsonObject.optString("date").takeIf { it.isNotEmpty() }
        val error = jsonObject.optString("error").takeIf { it.isNotEmpty() }

        if (error != null) {
            throw IllegalStateException("Error while parsing JSON: $error")
        }

        // when latestDate is not null that means that "rates" object is not containing dates
        return if (latestDate != null) {
            parseRatesList(baseCurrency, latestDate, ratesListObject)
                .toList()
        } else {
            ratesListObject.keys().asSequence().flatMap { dateField ->
                val rateObject = ratesListObject.getJSONObject(dateField)
                return@flatMap parseRatesList(baseCurrency, dateField, rateObject)
            }.toList()
        }
    }

    private fun parseRatesList(
        baseCurrency: String,
        date: String,
        rateObject: JSONObject
    ): Sequence<RateValue> {
        return rateObject.keys().asSequence().map { currencyField ->
            val rateField = rateObject.getDouble(currencyField)
            RateValue(
                baseCurrency = Currency.fromString(baseCurrency),
                currency = Currency.fromString(currencyField),
                date = LocalDate.parse(date),
                rate = rateField,
                timestamp = LocalDateTime.now()
            )
        }
    }

    @ToJson
    fun rateValueToJson(exchangeRate: RateValue): String {
        return "" // No need
    }
}