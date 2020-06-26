package eu.okatrych.domain.model

import java.util.Locale

enum class Currency {
    CAD, HKD, ISK, PHP, DKK, HUF, CZK, GBP, RON, SEK, IDR, INR, BRL, RUB, HRK, JPY, THB, CHF, EUR,
    MYR, BGN, TRY, CNY, NOK, NZD, ZAR, USD, MXN, SGD, AUD, ILS, KRW, PLN,
    ;

    companion object {
        fun fromString(value: String) = valueOf(value.toUpperCase(Locale.US))
    }
}