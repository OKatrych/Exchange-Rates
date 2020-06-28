package eu.okatrych.data.source

import eu.okatrych.domain.model.Currency
import eu.okatrych.domain.model.ExchangeRate
import org.threeten.bp.LocalDate

interface IExchangeRateDataSource {

    suspend fun getExchangeRate(
        baseCurrency: Currency,
        specificCurrencies: List<Currency>,
        startDate: LocalDate,
        endDate: LocalDate
    ): ExchangeRate?
}