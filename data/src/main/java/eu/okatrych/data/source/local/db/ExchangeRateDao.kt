package eu.okatrych.data.source.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import eu.okatrych.data.source.local.model.RoomRateValue

@Dao
interface ExchangeRateDao {

    @Query("""
        SELECT * FROM exchange_rates 
        WHERE baseCurrency = :baseCurrency 
        AND currency IN(:specificCurrencies) 
        AND (date BETWEEN :startDate AND :endDate)"""
    )
    suspend fun getExchangeRates(
        startDate: String,
        endDate: String,
        baseCurrency: String,
        specificCurrencies: List<String>
    ): List<RoomRateValue>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExchangeRates(rates: List<RoomRateValue>)
}