package eu.okatrych.data.source.remote

import eu.okatrych.data.source.remote.service.ExchangeRatesApiService
import eu.okatrych.domain.model.Currency
import eu.okatrych.domain.model.RateValue
import eu.okatrych.domain.repository.IExchangeRateRepository
import org.threeten.bp.LocalDate

class RemoteExchangeRateDataSource(
    private val exchangeRatesApiService: ExchangeRatesApiService
) : IRemoteExchangeRateDataSource {

    override suspend fun getExchangeRates(
        baseCurrency: Currency,
        specificCurrencies: List<Currency>,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<RateValue> {
        return wrapExceptions {
            exchangeRatesApiService.getExchangeRates(
                baseCurrency,
                specificCurrencies.joinToString(separator = ",") { it.name },
                startDate,
                endDate
            )
        }
    }

    override suspend fun getLatestExchangeRates(baseCurrency: Currency): List<RateValue> {
        return wrapExceptions {
            exchangeRatesApiService.getLatestExchangeRates(baseCurrency)
        }
    }

    private inline fun <T> wrapExceptions(block: () -> T): T {
        return try {
            block.invoke()
        } catch (exception: Throwable) {
            // TODO add other exception types
            throw IExchangeRateRepository.Error.RemoteDataSourceError.UnknownError(exception)
        }
    }
}