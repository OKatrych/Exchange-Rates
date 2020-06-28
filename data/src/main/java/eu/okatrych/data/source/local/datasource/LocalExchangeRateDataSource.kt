package eu.okatrych.data.source.local.datasource

import eu.okatrych.domain.model.Currency
import eu.okatrych.domain.model.ExchangeRate
import org.threeten.bp.LocalDate

class LocalExchangeRateDataSource : ILocalExchangeRateDataSource {

    override suspend fun getExchangeRate(
        baseCurrency: Currency,
        specificCurrencies: List<Currency>,
        startDate: LocalDate,
        endDate: LocalDate
    ): ExchangeRate {
        TODO("Not yet implemented")
    }

    override suspend fun insertExchangeRate(exchangeRate: ExchangeRate) {
        TODO("Not yet implemented")
    }

    override suspend fun isExchangeRateStored(): Boolean {
        TODO("Not yet implemented")
    }
}