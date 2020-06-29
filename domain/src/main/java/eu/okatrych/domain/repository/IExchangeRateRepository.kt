package eu.okatrych.domain.repository

import eu.okatrych.domain.model.Currency
import eu.okatrych.domain.model.RateValue
import eu.okatrych.domain.util.DEFAULT_BASE_CURRENCY
import org.threeten.bp.LocalDate

interface IExchangeRateRepository {

    /**
     * Load exchange rates for specific date
     */
    suspend fun getExchangeRates(
        baseCurrency: Currency = DEFAULT_BASE_CURRENCY,
        specificCurrencies: List<Currency> = Currency.values().toList(),
        startDate: LocalDate = LocalDate.now(),
        endDate: LocalDate = startDate
    ): List<RateValue>

    /**
     * Load latest available exchange rates
     */
    suspend fun getLatestExchangeRates(
        baseCurrency: Currency = DEFAULT_BASE_CURRENCY
    ): List<RateValue>

    sealed class Error(
        cause: Throwable? = null
    ) : Exception(cause) {
        sealed class RemoteDataSourceError(cause: Throwable? = null) : Error(cause) {
            object ApiNotRespondingError : RemoteDataSourceError()
            data class ApiError(val responseCode: Int) : RemoteDataSourceError()
            data class UnknownError(override val cause: Throwable) : RemoteDataSourceError(cause)
        }

        data class LocalDataSourceError(override val cause: Throwable) : Error(cause)
    }
}