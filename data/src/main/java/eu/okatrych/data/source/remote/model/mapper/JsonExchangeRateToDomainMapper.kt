package eu.okatrych.data.source.remote.model.mapper

import eu.okatrych.data.source.remote.model.JsonExchangeRate
import eu.okatrych.domain.model.RateValue
import eu.okatrych.domain.util.IMapper
import org.threeten.bp.LocalDate

class JsonExchangeRateToDomainMapper :
    IMapper<JsonExchangeRate, eu.okatrych.domain.model.ExchangeRate> {

    override fun map(value: JsonExchangeRate): eu.okatrych.domain.model.ExchangeRate {
        return eu.okatrych.domain.model.ExchangeRate(
            baseCurrency = value.baseCurrency,
            startDate = value.startDate.let { LocalDate.parse(it) },
            endDate = value.endDate.let { LocalDate.parse(it) },
            rates = value.rates.map { RateValue(it.currency, it.date, it.rate) },
            timestamp = value.timestamp
        )
    }
}