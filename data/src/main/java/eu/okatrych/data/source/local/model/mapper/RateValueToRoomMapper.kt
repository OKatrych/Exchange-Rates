package eu.okatrych.data.source.local.model.mapper

import eu.okatrych.data.source.local.model.RoomRateValue
import eu.okatrych.domain.model.RateValue
import eu.okatrych.domain.util.IMapper

class RateValueToRoomMapper: IMapper<RateValue, RoomRateValue> {

    override fun map(value: RateValue): RoomRateValue {
        return RoomRateValue(
            date = value.date.toString(),
            baseCurrency = value.baseCurrency.name,
            currency = value.currency.name,
            rate = value.rate,
            timestamp = value.timestamp.toString()
        )
    }
}