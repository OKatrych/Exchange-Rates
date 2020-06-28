package eu.okatrych.data.source.local.datasource

import eu.okatrych.data.source.IExchangeRateDataSource
import eu.okatrych.domain.model.ExchangeRate

interface ILocalExchangeRateDataSource: IExchangeRateDataSource {

    /**
     * @return true if [eu.okatrych.domain.model.ExchangeRate] is stored in DB, false otherwise
     */
    suspend fun isExchangeRateStored(): Boolean

    suspend fun insertExchangeRate(exchangeRate: ExchangeRate)
}