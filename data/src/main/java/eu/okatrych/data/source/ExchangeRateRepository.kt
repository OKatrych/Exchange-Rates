package eu.okatrych.data.source

import eu.okatrych.data.source.local.datasource.ILocalExchangeRateDataSource
import eu.okatrych.data.source.remote.datasource.IRemoteExchangeRateDataSource
import eu.okatrych.domain.model.Currency
import eu.okatrych.domain.model.ExchangeRate
import eu.okatrych.domain.repository.IExchangeRateRepository
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.temporal.ChronoUnit

const val EXCHANGE_RATE_TTL = 10L

class ExchangeRateRepository(
    private val localDataSource: ILocalExchangeRateDataSource,
    private val remoteDataSource: IRemoteExchangeRateDataSource
) : IExchangeRateRepository {

    // TODO add cache
    override suspend fun getExchangeRate(
        baseCurrency: Currency,
        specificCurrencies: List<Currency>,
        startDate: LocalDate,
        endDate: LocalDate
    ): ExchangeRate {
        if (localDataSource.isExchangeRateStored()) {
            val localExchangeRate = localDataSource.getExchangeRate(
                baseCurrency,
                specificCurrencies,
                startDate,
                endDate
            )
            // while exchange rate is not expired (timestamp < 10 minutes) use value from local DB
            if (localExchangeRate.timestamp
                    .plus(EXCHANGE_RATE_TTL, ChronoUnit.MINUTES)
                    .isAfter(LocalDateTime.now())
            ) {
                return localExchangeRate
            }
        }
        return remoteDataSource.getExchangeRate(
            baseCurrency, specificCurrencies, startDate, endDate
        ).also {
            localDataSource.insertExchangeRate(it)
        }
    }
}