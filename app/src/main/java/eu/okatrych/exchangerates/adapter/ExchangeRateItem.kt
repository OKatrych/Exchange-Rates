package eu.okatrych.exchangerates.adapter

import android.view.View
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import eu.okatrych.domain.model.RateValue
import eu.okatrych.exchangerates.R
import eu.okatrych.exchangerates.util.precisizeTo
import kotlinx.android.synthetic.main.exchange_rate_item.view.*
import java.util.concurrent.atomic.AtomicBoolean

class ExchangeRateItem(
    private val rateValue: RateValue,
    private val onItemClicked: (RateValue) -> Unit
) : Item() {

    companion object {
        private var isBackgroundGrey = AtomicBoolean(true)
    }

    override fun getLayout() = R.layout.exchange_rate_item

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        with(viewHolder.containerView) {
            setOnClickListener { onItemClicked.invoke(rateValue) }
            currency_name.text = rateValue.currency.name
            rate_value.text = rateValue.rate.precisizeTo(2).toString()

            switchBackgroundColor(this)
        }
    }

    private fun switchBackgroundColor(view: View) {
        if (isBackgroundGrey.get()) {
            view.setBackgroundColor(view.context.getColor(R.color.colorGray))
        } else {
            view.setBackgroundColor(view.context.getColor(R.color.colorWhite))
        }
        isBackgroundGrey.set(!isBackgroundGrey.get())
    }
}