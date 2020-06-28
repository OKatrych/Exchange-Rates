package eu.okatrych.domain.repository

import eu.okatrych.domain.model.Currency
import eu.okatrych.domain.model.ExchangeRate
import eu.okatrych.domain.util.DEFAULT_BASE_CURRENCY
import org.threeten.bp.LocalDate

interface IExchangeRateRepository {

    suspend fun getExchangeRate(
        baseCurrency: Currency = DEFAULT_BASE_CURRENCY,
        specificCurrencies: List<Currency> = Currency.values().toList(),
        startDate: LocalDate = LocalDate.now(),
        endDate: LocalDate = startDate
    ): ExchangeRate
}