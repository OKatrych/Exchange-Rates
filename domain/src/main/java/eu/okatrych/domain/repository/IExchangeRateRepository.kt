package eu.okatrych.domain.repository

import eu.okatrych.domain.model.Currency
import eu.okatrych.domain.model.ExchangeRate
import eu.okatrych.domain.util.DEFAULT_BASE_CURRENCY
import org.threeten.bp.LocalDate

interface IExchangeRateRepository {

    suspend fun getExchangeRate(
        baseCurrency: Currency = DEFAULT_BASE_CURRENCY,
        specificCurrencies: List<Currency> = Currency.values().toList(),
        startDate: LocalDate = LocalDate.now(),
        endDate: LocalDate = startDate
    ): ExchangeRate

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