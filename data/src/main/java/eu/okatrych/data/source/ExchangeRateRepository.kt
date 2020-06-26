package eu.okatrych.data.source

import eu.okatrych.data.model.mapper.ExchangeRateToDomainMapper
import eu.okatrych.data.source.remote.ExchangeRatesApiService
import eu.okatrych.domain.model.Currency
import eu.okatrych.domain.model.ExchangeRate
import eu.okatrych.domain.repository.IExchangeRateRepository
import org.threeten.bp.LocalDate

class ExchangeRateRepository(
    private val exchangeRatesApiService: ExchangeRatesApiService,
    private val exchangeRateToDomainMapper: ExchangeRateToDomainMapper
) : IExchangeRateRepository {

    // TODO replace by remote/local data source combination
    override suspend fun loadExchangeRate(
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