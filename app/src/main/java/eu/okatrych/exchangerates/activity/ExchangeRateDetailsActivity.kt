package eu.okatrych.exchangerates.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import eu.okatrych.domain.model.Currency
import eu.okatrych.domain.model.RateValue
import eu.okatrych.exchangerates.R
import eu.okatrych.exchangerates.viewmodel.ExchangeRateDetailsViewModel
import kotlinx.android.synthetic.main.activity_exchange_rate_details.*
import kotlinx.android.synthetic.main.activity_exchange_rates.loading_view
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ExchangeRateDetailsActivity : AppCompatActivity() {

    companion object {
        private const val BASE_CURRENCY_KEY = "BASE_CURRENCY_KEY"
        private const val CURRENCY_KEY = "CURRENCY_KEY"

        fun makeIntent(
            context: Context,
            baseCurrency: Currency,
            currency: Currency
        ) = Intent(context, ExchangeRateDetailsActivity::class.java).apply {
            putExtra(BASE_CURRENCY_KEY, baseCurrency)
            putExtra(CURRENCY_KEY, currency)
        }
    }

    private val viewModel: ExchangeRateDetailsViewModel by viewModel {
        parametersOf(
            intent.getSerializableExtra(BASE_CURRENCY_KEY),
            intent.getSerializableExtra(CURRENCY_KEY)
        )
    }

    private val chartView by lazy { history_chart }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exchange_rate_details)
        initViews()
        observeDataChange()
    }

    private fun initViews() {
        title = (intent.getSerializableExtra(CURRENCY_KEY) as Currency).name
    }

    private fun observeDataChange() {
        viewModel.exchangeRates().observe(this, Observer { exchangeRates ->
            if (exchangeRates.isNotEmpty()) {
                fillChart(exchangeRates)
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.error_no_exchange_rate_history),
                    Toast.LENGTH_LONG
                ).show()
            }
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

    private fun fillChart(rates: List<RateValue>) {
        val entries = rates.mapIndexed { index: Int, rateValue: RateValue ->
            Entry(index.toFloat(), rateValue.rate.toFloat())
        }
        val dataSet = LineDataSet(entries, "Rates")
        chartView.data = LineData(dataSet)
        chartView.setDrawGridBackground(false)
        chartView.invalidate()
    }
}