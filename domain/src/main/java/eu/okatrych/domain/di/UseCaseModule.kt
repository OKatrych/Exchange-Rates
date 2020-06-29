package eu.okatrych.domain.di

import eu.okatrych.domain.interactor.GetExchangeRatesUseCase
import eu.okatrych.domain.interactor.GetLatestExchangeRatesUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory {
        GetExchangeRatesUseCase(
            exchangeRateRepository = get()
        )
    }

    factory {
        GetLatestExchangeRatesUseCase(
            exchangeRateRepository = get()
        )
    }
}