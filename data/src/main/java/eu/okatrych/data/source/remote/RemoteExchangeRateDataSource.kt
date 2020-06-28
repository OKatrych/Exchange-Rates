package eu.okatrych.data.source.remote

import eu.okatrych.data.source.remote.model.mapper.JsonExchangeRateToDomainMapper
import eu.okatrych.data.source.remote.service.ExchangeRatesApiService
import eu.okatrych.domain.model.Currency
import eu.okatrych.domain.model.ExchangeRate
import eu.okatrych.domain.repository.IExchangeRateRepository
import org.threeten.bp.LocalDate

class RemoteExchangeRateDataSource(
    private val exchangeRatesApiService: ExchangeRatesApiService,
    private val jsonExchangeRateToDomainMapper: JsonExchangeRateToDomainMapper
) : IRemoteExchangeRateDataSource {

    override suspend fun getExchangeRate(
        baseCurrency: Currency,
        specificCurrencies: List<Currency>,
        startDate: LocalDate,
        endDate: LocalDate
    ): ExchangeRate? {
        return wrapExceptions {
            exchangeRatesApiService.getExchangeRate(
                baseCurrency,
                specificCurrencies.joinToString(separator = ",") { it.name },
                startDate,
                endDate
            ).let {
                jsonExchangeRateToDomainMapper.map(it)
            }
        }
    }

    private inline fun <T> wrapExceptions(block: () -> T?): T? {
        return try {
            block.invoke()
        } catch (exception: Throwable) {
            // TODO add other exception types
            throw IExchangeRateRepository.Error.RemoteDataSourceError.UnknownError(exception)
        }
    }
}