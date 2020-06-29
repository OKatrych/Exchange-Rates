package eu.okatrych.exchangerates.util

import java.math.BigDecimal
import java.math.RoundingMode

fun Double.precisizeTo(numberOfDigits: Int): Double {
    return BigDecimal(this).setScale(numberOfDigits, RoundingMode.HALF_EVEN).toDouble()
}