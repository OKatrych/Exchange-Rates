package eu.okatrych.data.source.local.model.mapper

import eu.okatrych.data.source.local.model.RoomExchangeRate
import eu.okatrych.domain.model.ExchangeRate
import eu.okatrych.domain.util.IMapper

class ExchangeRateToRoomMapper: IMapper<ExchangeRate, RoomExchangeRate> {

    override fun map(value: ExchangeRate): RoomExchangeRate {
        return RoomExchangeRate(
            baseCurrency = value.baseCurrency.name,
            startDate = value.startDate.toString(),
            endDate = value.endDate.toString(),
            rates = value.rates,
            timestamp = value.timestamp.toString()
        )
    }
}