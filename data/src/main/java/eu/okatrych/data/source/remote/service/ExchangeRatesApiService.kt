package eu.okatrych.data.source.remote.service

import eu.okatrych.data.model.ExchangeRate
import eu.okatrych.domain.model.Currency
import org.threeten.bp.LocalDate
import retrofit2.http.GET
import retrofit2.http.Query

interface ExchangeRatesApiService {

    @GET("history")
    suspend fun getExchangeRate(
        @Query("base") baseCurrency: Currency,
        @Query("symbols") specificCurrencies: String, // USD,EUR,PLN
        @Query("start_at") startDate: LocalDate,
        @Query("end_at") endDate: LocalDate
    ): ExchangeRate
}