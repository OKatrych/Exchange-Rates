package eu.okatrych.data.source.local.model

import androidx.room.Entity

@Entity(tableName = "exchange_rates", primaryKeys = ["date", "baseCurrency", "currency"])
data class RoomRateValue(
    val date: String,
    val baseCurrency: String,
    val currency: String,
    val rate: Double,
    val timestamp: String
)