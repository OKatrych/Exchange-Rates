package eu.okatrych.data.source

import eu.okatrych.data.source.local.ILocalExchangeRateDataSource
import eu.okatrych.data.source.remote.IRemoteExchangeRateDataSource
import eu.okatrych.domain.model.Currency
import eu.okatrych.domain.model.RateValue
import eu.okatrych.domain.repository.IExchangeRateRepository
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.temporal.ChronoUnit
import timber.log.Timber

const val EXCHANGE_RATE_TTL = 10L

class ExchangeRateRepository(
    private val localDataSource: ILocalExchangeRateDataSource,
    private val remoteDataSource: IRemoteExchangeRateDataSource
) : IExchangeRateRepository {

    // TODO add cache
    override suspend fun getExchangeRates(
        baseCurrency: Currency,
        specificCurrencies: List<Currency>,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<RateValue> {
        val localExchangeRates: List<RateValue> = localDataSource.getExchangeRates(
            baseCurrency,
            specificCurrencies,
            startDate,
            endDate
        )
        Timber.d("getExchangeRates: localExchangeRates = $localExchangeRates")
        // use local exchange rate when it's present and is not expired (timestamp < 10 minutes)
        return if (checkLocalExchangeRatesExpired(localExchangeRates)) {
            Timber.d("localExchangeRates are not expired, use them")
            localExchangeRates
        } else {
            Timber.d("localExchangeRates are expired or empty, use remoteExchangeRates")
            remoteDataSource.getExchangeRates(
                baseCurrency, specificCurrencies, startDate, endDate
            ).also {
                Timber.d("remoteExchangeRates = $it")
                localDataSource.insertExchangeRates(it)
            }
        }
    }

    override suspend fun getLatestExchangeRates(baseCurrency: Currency): List<RateValue> {
        val localExchangeRates: List<RateValue> = localDataSource.getLatestExchangeRates(
            baseCurrency
        )
        Timber.d("getLatestExchangeRates: localExchangeRates = $localExchangeRates")
        // use local exchange rate when it's present and is not expired (timestamp < 10 minutes)
        return if (checkLocalExchangeRatesExpired(localExchangeRates)) {
            Timber.d("localExchangeRates are not expired, use them")
            localExchangeRates
        } else {
            Timber.d("localExchangeRates are expired or empty, use remoteExchangeRates")
            remoteDataSource.getLatestExchangeRates(baseCurrency).also {
                Timber.d("remoteExchangeRates = $it")
                localDataSource.insertExchangeRates(it)
            }
        }
    }

    /*
     * Exchange rates should be updated every [EXCHANGE_RATE_TTL] minutes
     */
    private fun checkLocalExchangeRatesExpired(localExchangeRates: List<RateValue>) =
        localExchangeRates.isNotEmpty() && localExchangeRates.all {
            it.timestamp
                .plus(EXCHANGE_RATE_TTL, ChronoUnit.MINUTES)
                .isAfter(LocalDateTime.now())
        }
}