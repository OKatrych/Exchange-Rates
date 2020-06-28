package eu.okatrych.data.source.remote.datasource

import eu.okatrych.data.model.mapper.ExchangeRateToDomainMapper
import eu.okatrych.data.source.IExchangeRateDataSource
import eu.okatrych.data.source.remote.service.ExchangeRatesApiService
import eu.okatrych.domain.model.Currency
import eu.okatrych.domain.model.ExchangeRate
import org.threeten.bp.LocalDate

class RemoteExchangeRateDataSource(
    private val exchangeRatesApiService: ExchangeRatesApiService,
    private val exchangeRateToDomainMapper: ExchangeRateToDomainMapper
) : IRemoteExchangeRateDataSource {

    override suspend fun getExchangeRate(
        baseCurrency: Currency,
        specificCurrencies: List<Currency>,
        startDate: LocalDate,
        endDate: LocalDate
    ): ExchangeRate {
        return exchangeRatesApiService.getExchangeRate(
            baseCurrency,
            specificCurrencies.joinToString(separator = ",") { it.name },
            startDate,
            endDate
        ).let {
            exchangeRateToDomainMapper.map(it)
        }
    }
}