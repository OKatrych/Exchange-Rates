package eu.okatrych.data.source.local.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import eu.okatrych.data.source.local.model.converter.RateValueTypeConverter
import eu.okatrych.domain.model.Currency
import eu.okatrych.domain.model.RateValue

@Entity(tableName = "exchange_rates")
data class RoomExchangeRate(
    @PrimaryKey
    val baseCurrency: Currency,
    val startDate: String,
    val endDate: String,
    @Embedded
    @TypeConverters(RateValueTypeConverter::class)
    val rates: List<RateValue>,
    val timestamp: String
)