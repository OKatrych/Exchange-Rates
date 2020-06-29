package eu.okatrych.exchangerates.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.okatrych.domain.interactor.GetExchangeRatesUseCase
import eu.okatrych.domain.interactor.GetLatestExchangeRatesUseCase
import eu.okatrych.domain.model.Currency
import eu.okatrych.domain.model.RateValue
import eu.okatrych.domain.util.DEFAULT_BASE_CURRENCY
import eu.okatrych.exchangerates.util.Event
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate

private const val HISTORY_FOR_DAYS = 7L

class ExchangeRateDetailsViewModel(
    private val baseCurrency: Currency,
    private val currency: Currency,
    private val getExchangeRatesUseCase: GetExchangeRatesUseCase
) : ViewModel() {

    private val mutableExchangeRates = MutableLiveData<List<RateValue>>()
    fun exchangeRates(): LiveData<List<RateValue>> = mutableExchangeRates

    private val mutableErrorEvent = MutableLiveData<Event<Exception>>()
    fun errorEvent(): LiveData<Event<Exception>> = mutableErrorEvent

    private val mutableLoadingState = MutableLiveData<Boolean>()
    fun loadingState(): LiveData<Boolean> = mutableLoadingState

    init {
        loadExchangeRates()
    }

    private fun loadExchangeRates() {
        viewModelScope.launch {
            mutableLoadingState.value = true
            try {
                val endDate = LocalDate.now()
                val startDate = endDate.minusDays(HISTORY_FOR_DAYS)
                val exchangeRates: List<RateValue> =
                    getExchangeRatesUseCase(
                        GetExchangeRatesUseCase.Params(
                            baseCurrency = baseCurrency,
                            specificCurrencies = listOf(currency),
                            endDate = endDate,
                            startDate = startDate
                        )
                    )
                mutableLoadingState.value = false
                mutableExchangeRates.value = exchangeRates
            } catch (ex: Exception) {
                mutableLoadingState.value = false
                mutableErrorEvent.value = Event(ex)
            }
        }
    }
}