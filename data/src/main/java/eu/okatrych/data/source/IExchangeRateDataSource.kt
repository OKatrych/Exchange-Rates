package eu.okatrych.data.source

import eu.okatrych.domain.model.Currency
import eu.okatrych.domain.model.RateValue
import org.threeten.bp.LocalDate

interface IExchangeRateDataSource {

    suspend fun getExchangeRates(
        baseCurrency: Currency,
        specificCurrencies: List<Currency>,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<RateValue>

    suspend fun getLatestExchangeRates(baseCurrency: Currency): List<RateValue>
}