package eu.okatrych.data.source.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import eu.okatrych.data.source.local.model.RoomExchangeRate
import eu.okatrych.domain.model.Currency

@Dao
interface ExchangeRateDao {

    @Query("SELECT * FROM exchange_rates WHERE baseCurrency = :baseCurrency")
    suspend fun getExchangeRateByBaseCurrency(baseCurrency: Currency): RoomExchangeRate

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExchangeRate(rate: RoomExchangeRate)
}