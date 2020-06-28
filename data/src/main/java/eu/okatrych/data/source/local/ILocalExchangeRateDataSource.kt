package eu.okatrych.data.source.local

import eu.okatrych.data.source.IExchangeRateDataSource
import eu.okatrych.domain.model.ExchangeRate

interface ILocalExchangeRateDataSource: IExchangeRateDataSource {

    suspend fun insertExchangeRate(exchangeRate: ExchangeRate)
}