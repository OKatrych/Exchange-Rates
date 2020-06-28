package eu.okatrych.data.source

import eu.okatrych.data.source.local.ILocalExchangeRateDataSource
import eu.okatrych.data.source.remote.IRemoteExchangeRateDataSource
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
        val localExchangeRate: ExchangeRate? = localDataSource.getExchangeRate(
            baseCurrency,
            specificCurrencies,
            startDate,
            endDate
        )
        // use local exchange rate when it's present and is not expired (timestamp < 10 minutes)
        return if (localExchangeRate != null && localExchangeRate.timestamp
                .plus(EXCHANGE_RATE_TTL, ChronoUnit.MINUTES)
                .isAfter(LocalDateTime.now())
        ) {
            localExchangeRate
        } else {
            remoteDataSource.getExchangeRate(
                baseCurrency, specificCurrencies, startDate, endDate
            )?.also {
                localDataSource.insertExchangeRate(it)
            } ?: throw IExchangeRateRepository.Error.RemoteDataSourceError.UnknownError(
                NullPointerException("remoteDataSource.getExchangeRate() returned null value")
            )
        }
    }
}