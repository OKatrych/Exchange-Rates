package eu.okatrych.data.source.local.model.mapper

import eu.okatrych.data.source.local.model.RoomExchangeRate
import eu.okatrych.domain.model.Currency
import eu.okatrych.domain.model.ExchangeRate
import eu.okatrych.domain.util.IMapper
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

class RoomExchangeRateToDomainMapper: IMapper<RoomExchangeRate, ExchangeRate> {

    override fun map(value: RoomExchangeRate): ExchangeRate {
        return ExchangeRate(
            baseCurrency = Currency.fromString(value.baseCurrency),
            startDate = LocalDate.parse(value.startDate),
            endDate = LocalDate.parse(value.endDate),
            rates = value.rates,
            timestamp = LocalDateTime.parse(value.timestamp)
        )
    }
}