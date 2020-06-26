package eu.okatrych.exchangerates

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import eu.okatrych.data.di.dataModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)

        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidContext(applicationContext)
            modules(
                listOf(
                    dataModule
                )
            )
        }
    }
}