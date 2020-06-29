package eu.okatrych.data.source.local

import eu.okatrych.data.source.IExchangeRateDataSource
import eu.okatrych.domain.model.RateValue

interface ILocalExchangeRateDataSource: IExchangeRateDataSource {

    suspend fun insertExchangeRates(exchangeRates: List<RateValue>)
}