package eu.okatrych.data.source.local

import eu.okatrych.data.source.local.db.ExchangeRateDatabase
import eu.okatrych.data.source.local.model.RoomRateValue
import eu.okatrych.domain.model.Currency
import eu.okatrych.domain.model.RateValue
import eu.okatrych.domain.repository.IExchangeRateRepository
import eu.okatrych.domain.util.IMapper
import org.threeten.bp.LocalDate
import timber.log.Timber

class LocalExchangeRateDataSource(
    private val exchangeRateDatabase: ExchangeRateDatabase,
    private val roomRateValueToDomainMapper: IMapper<RoomRateValue, RateValue>,
    private val rateValueToRoomMapper: IMapper<RateValue, RoomRateValue>
) : ILocalExchangeRateDataSource {

    override suspend fun getExchangeRates(
        baseCurrency: Currency,
        specificCurrencies: List<Currency>,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<RateValue> {
        Timber.d("getExchangeRates: ebaseCurrency = $baseCurrency, specificCurrencies = $specificCurrencies, startDate = $startDate, endDate = $endDate")
        return wrapExceptions {
            exchangeRateDatabase.exchangeRateDao
                .getExchangeRates(
                    startDate.toString(),
                    endDate.toString(),
                    baseCurrency.name,
                    specificCurrencies.map { it.name }
                ).map(roomRateValueToDomainMapper::map)
        }
    }

    override suspend fun getLatestExchangeRates(baseCurrency: Currency): List<RateValue> {
        return wrapExceptions {
            exchangeRateDatabase.exchangeRateDao
                .getLatestExchangeRates(
                    baseCurrency.name
                ).map(roomRateValueToDomainMapper::map)
        }
    }

    override suspend fun insertExchangeRates(exchangeRates: List<RateValue>) {
        Timber.d("insertExchangeRates: exchangeRates = $exchangeRates")
        wrapExceptions {
            exchangeRateDatabase.exchangeRateDao.insertExchangeRates(
                exchangeRates.map(rateValueToRoomMapper::map)
            )
        }
    }

    private inline fun <T> wrapExceptions(block: () -> T): T {
        return try {
            block.invoke()
        } catch (exception: Throwable) {
            throw IExchangeRateRepository.Error.LocalDataSourceError(exception)
        }
    }
}