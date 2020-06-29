package eu.okatrych.data.source.local.model.mapper

import eu.okatrych.data.source.local.model.RoomRateValue
import eu.okatrych.domain.model.Currency
import eu.okatrych.domain.model.RateValue
import eu.okatrych.domain.util.IMapper
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

class RoomRateValueToDomainMapper : IMapper<RoomRateValue, RateValue> {

    override fun map(value: RoomRateValue): RateValue {
        return RateValue(
            date = LocalDate.parse(value.date),
            baseCurrency = Currency.fromString(value.baseCurrency),
            currency = Currency.fromString(value.currency),
            rate = value.rate,
            timestamp = LocalDateTime.parse(value.timestamp)
        )
    }
}