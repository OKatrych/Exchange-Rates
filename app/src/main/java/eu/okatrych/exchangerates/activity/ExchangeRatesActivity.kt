package eu.okatrych.exchangerates.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import eu.okatrych.domain.model.Currency
import eu.okatrych.domain.model.RateValue
import eu.okatrych.exchangerates.R
import eu.okatrych.exchangerates.adapter.ExchangeRatesListAdapter
import eu.okatrych.exchangerates.viewmodel.ExchangeRatesViewModel
import kotlinx.android.synthetic.main.activity_exchange_rates.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ExchangeRatesActivity : AppCompatActivity() {

    private val viewModel: ExchangeRatesViewModel by viewModel()
    private val exchangeRatesListAdapter by lazy {
        ExchangeRatesListAdapter(::startDetailsScreen)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exchange_rates)
        initViews()
        observeDataChange()
    }

    private fun initViews() {
        exchange_rates_list.adapter = exchangeRatesListAdapter
    }

    private fun observeDataChange() {
        viewModel.exchangeRates().observe(this, Observer { exchangeRates ->
            exchangeRatesListAdapter.updateExchangeRates(exchangeRates)
        })
        viewModel.loadingState().observe(this, Observer { isLoading ->
            loading_view.visibility = if (isLoading) View.VISIBLE else View.GONE
        })
        viewModel.errorEvent().observe(this, Observer { error ->
            error.getContentIfNotHandled()?.let {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

    private fun startDetailsScreen(rateValue: RateValue) {
        startActivity(
            ExchangeRateDetailsActivity.makeIntent(
                this,
                rateValue.baseCurrency,
                rateValue.currency
            )
        )
    }
}