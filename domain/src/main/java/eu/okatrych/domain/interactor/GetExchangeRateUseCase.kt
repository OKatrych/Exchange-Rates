package eu.okatrych.domain.interactor

import eu.okatrych.domain.model.Currency
import eu.okatrych.domain.model.RateValue
import eu.okatrych.domain.repository.IExchangeRateRepository
import eu.okatrych.domain.util.DEFAULT_BASE_CURRENCY
import org.threeten.bp.LocalDate

class GetExchangeRateUseCase(
    private val exchangeRateRepository: IExchangeRateRepository
) : UseCase<GetExchangeRateUseCase.Params, List<RateValue>>() {

    data class Params(
        val baseCurrency: Currency = DEFAULT_BASE_CURRENCY,
        val specificCurrencies: List<Currency> = Currency.values().toList(),
        val startDate: LocalDate = LocalDate.now(),
        val endDate: LocalDate = startDate
    )

    override suspend fun run(params: Params): List<RateValue> {
        return exchangeRateRepository.getExchangeRates()
    }
}