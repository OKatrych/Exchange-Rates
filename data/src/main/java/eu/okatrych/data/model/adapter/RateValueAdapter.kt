package eu.okatrych.data.model.adapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import eu.okatrych.domain.model.Currency
import eu.okatrych.domain.model.RateValue
import org.json.JSONObject
import org.threeten.bp.LocalDate


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

        return jsonObject.keys().asSequence().flatMap { dateField ->
            val rateObject = jsonObject.getJSONObject(dateField)
            return@flatMap rateObject.keys().asSequence().map { currencyField ->
                val rateField = rateObject.getDouble(currencyField)
                RateValue(
                    date = LocalDate.parse(dateField),
                    currency = Currency.fromString(currencyField),
                    rate = rateField
                )
            }
        }.toList()
    }

    @ToJson
    fun rateValueToJson(exchangeRate: RateValue): String {
        return "" // No need
    }
}