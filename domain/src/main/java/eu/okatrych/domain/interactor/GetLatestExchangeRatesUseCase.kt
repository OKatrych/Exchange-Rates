package eu.okatrych.domain.interactor

import eu.okatrych.domain.model.Currency
import eu.okatrych.domain.model.RateValue
import eu.okatrych.domain.repository.IExchangeRateRepository

class GetLatestExchangeRatesUseCase(
    private val exchangeRateRepository: IExchangeRateRepository
) : UseCase<Currency, List<RateValue>>() {

    override suspend fun run(params: Currency): List<RateValue> {
        return exchangeRateRepository.getLatestExchangeRates(params)
    }
}