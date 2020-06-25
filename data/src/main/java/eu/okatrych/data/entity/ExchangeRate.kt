package eu.okatrych.data.entity

import eu.okatrych.data.serializer.LocalDateSerializer
import eu.okatrych.domain.entity.Currency
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.time.LocalDate
import java.time.LocalDateTime

@Serializable
data class ExchangeRate(
    val base: Currency,
    @Serializable(with = LocalDateSerializer::class)
    val date: LocalDate,
    val rates: Rates,
    @Transient
    val timestamp: LocalDateTime = LocalDateTime.now()
)