package eu.okatrych.exchangerates.di

import eu.okatrych.domain.model.Currency
import eu.okatrych.exchangerates.viewmodel.ExchangeRateDetailsViewModel
import eu.okatrych.exchangerates.viewmodel.ExchangeRatesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        ExchangeRatesViewModel(
            getLatestExchangeRatesUseCase = get()
        )
    }

    viewModel { (baseCurrency: Currency, currency: Currency) ->
        ExchangeRateDetailsViewModel(
            baseCurrency,
            currency,
            getExchangeRatesUseCase = get()
        )
    }
}