package eu.okatrych.data.model.mapper

import eu.okatrych.data.model.ExchangeRate
import eu.okatrych.domain.model.RateValue
import eu.okatrych.domain.util.IMapper
import org.threeten.bp.LocalDate

class ExchangeRateToDomainMapper: IMapper<ExchangeRate, eu.okatrych.domain.model.ExchangeRate> {

    override fun map(value: ExchangeRate): eu.okatrych.domain.model.ExchangeRate {
        require(value.startDate != null && value.endDate != null && value.rates != null) {
            "Some of required arguments are null: $value"
        }

        return eu.okatrych.domain.model.ExchangeRate(
            baseCurrency = value.baseCurrency,
            startDate = value.startDate.let { LocalDate.parse(it) },
            endDate = value.endDate.let { LocalDate.parse(it) },
            rates = value.rates.map { RateValue(it.currency, it.date, it.rate) },
            timestamp = value.timestamp
        )
    }
}