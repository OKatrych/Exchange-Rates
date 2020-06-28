package eu.okatrych.data.source.local

import eu.okatrych.data.source.local.db.ExchangeRateDatabase
import eu.okatrych.data.source.local.model.RoomExchangeRate
import eu.okatrych.domain.model.Currency
import eu.okatrych.domain.model.ExchangeRate
import eu.okatrych.domain.repository.IExchangeRateRepository
import eu.okatrych.domain.util.IMapper
import org.threeten.bp.LocalDate

class LocalExchangeRateDataSource(
    private val exchangeRateDatabase: ExchangeRateDatabase,
    private val roomExchangeRateToDomainMapper: IMapper<RoomExchangeRate, ExchangeRate>,
    private val exchangeRateToRoomMapper: IMapper<ExchangeRate, RoomExchangeRate>
) : ILocalExchangeRateDataSource {

    override suspend fun getExchangeRate(
        baseCurrency: Currency,
        specificCurrencies: List<Currency>,
        startDate: LocalDate,
        endDate: LocalDate
    ): ExchangeRate? {
        return wrapExceptions {
            val dbRate = exchangeRateDatabase.exchangeRateDao
                .getExchangeRateByBaseCurrency(baseCurrency)
                .let { roomExchangeRateToDomainMapper.map(it) }
            return if (dbRate.meetsExpectations(specificCurrencies, startDate, endDate)) {
                dbRate
            } else {
                null
            }
        }
    }

    override suspend fun insertExchangeRate(exchangeRate: ExchangeRate) {
        wrapExceptions {
            exchangeRateDatabase.exchangeRateDao.insertExchangeRate(
                exchangeRateToRoomMapper.map(exchangeRate)
            )
        }
    }

    private fun ExchangeRate.meetsExpectations(
        expectedSpecificCurrencies: List<Currency>,
        expectedStartDate: LocalDate,
        expectedEndDate: LocalDate
    ): Boolean {
        return startDate == expectedStartDate &&
                endDate == expectedEndDate &&
                rates.map { it.currency }.containsAll(expectedSpecificCurrencies)
    }

    private inline fun <T> wrapExceptions(block: () -> T?): T? {
        return try {
            block.invoke()
        } catch (exception: Throwable) {
            throw IExchangeRateRepository.Error.LocalDataSourceError(exception)
        }
    }
}