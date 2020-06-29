package eu.okatrych.exchangerates.adapter

import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import eu.okatrych.domain.model.RateValue

class ExchangeRatesListAdapter(
    private val onItemClicked: (RateValue) -> Unit
): GroupAdapter<GroupieViewHolder>() {

    private val exchangeRatesSection = Section()

    init {
        add(exchangeRatesSection)
    }

    fun updateExchangeRates(exchangeRates: List<RateValue>) {
        exchangeRatesSection.update(
            exchangeRates.sortedBy { it.currency.name }
                .map { rateValue ->
                    ExchangeRateItem(rateValue, onItemClicked)
                }
        )
    }
}