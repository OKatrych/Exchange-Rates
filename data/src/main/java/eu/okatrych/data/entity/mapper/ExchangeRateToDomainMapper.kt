package eu.okatrych.data.entity.mapper

import eu.okatrych.data.entity.ExchangeRate
import eu.okatrych.data.entity.Rates
import eu.okatrych.domain.entity.Currency
import eu.okatrych.domain.entity.RateValue
import eu.okatrych.domain.util.IMapper

class ExchangeRateToDomainMapper: IMapper<ExchangeRate, eu.okatrych.domain.entity.ExchangeRate> {

    override fun map(value: ExchangeRate): eu.okatrych.domain.entity.ExchangeRate {
        return eu.okatrych.domain.entity.ExchangeRate(
            baseCurrency = value.base,
            date = value.date,
            rates = mapRates(value.rates),
            timestamp = value.timestamp
        )
    }

    private fun mapRates(rates: Rates): List<RateValue> {
        // TODO replace reflection
        return rates.javaClass.fields.map { field ->
            RateValue(
                Currency.fromString(field.name),
                field.getDouble(rates)
            )
        }
    }
}