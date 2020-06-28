package eu.okatrych.data.source.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import eu.okatrych.data.source.local.model.RoomExchangeRate

@Database(entities = [RoomExchangeRate::class], version = 1)
abstract class ExchangeRateDatabase : RoomDatabase() {

    abstract val exchangeRateDao: ExchangeRateDao

    companion object {
        @Volatile
        private var INSTANCE: ExchangeRateDatabase? = null

        fun getInstance(context: Context): ExchangeRateDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ExchangeRateDatabase::class.java, "ExchangeRateDatabase.db"
            ).build()
    }
}