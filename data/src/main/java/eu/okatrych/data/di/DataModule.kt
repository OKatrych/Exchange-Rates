package eu.okatrych.data.di

import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import eu.okatrych.data.BuildConfig
import eu.okatrych.data.source.remote.model.adapter.RateValueAdapter
import eu.okatrych.data.source.ExchangeRateRepository
import eu.okatrych.data.source.local.ILocalExchangeRateDataSource
import eu.okatrych.data.source.local.LocalExchangeRateDataSource
import eu.okatrych.data.source.local.db.ExchangeRateDatabase
import eu.okatrych.data.source.local.model.RoomRateValue
import eu.okatrych.data.source.local.model.mapper.RateValueToRoomMapper
import eu.okatrych.data.source.local.model.mapper.RoomRateValueToDomainMapper
import eu.okatrych.data.source.remote.IRemoteExchangeRateDataSource
import eu.okatrych.data.source.remote.RemoteExchangeRateDataSource
import eu.okatrych.data.source.remote.service.ExchangeRatesApiService
import eu.okatrych.domain.model.RateValue
import eu.okatrych.domain.repository.IExchangeRateRepository
import eu.okatrych.domain.util.IMapper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val dataModule = module {
    single { provideRetrofit(provideMoshi()) }
    single { provideApiService(get()) }

    factory<IMapper<RoomRateValue, RateValue>>(named<RoomRateValueToDomainMapper>()) {
        RoomRateValueToDomainMapper()
    }
    factory<IMapper<RateValue, RoomRateValue>>(named<RateValueToRoomMapper>()) {
        RateValueToRoomMapper()
    }


    single { ExchangeRateDatabase.getInstance(androidContext()) }
    single<IRemoteExchangeRateDataSource> { RemoteExchangeRateDataSource(get()) }
    single<ILocalExchangeRateDataSource> {
        LocalExchangeRateDataSource(
            exchangeRateDatabase = get(),
            rateValueToRoomMapper = get(named<RateValueToRoomMapper>()),
            roomRateValueToDomainMapper = get(named<RoomRateValueToDomainMapper>())
        )
    }
    single<IExchangeRateRepository> { ExchangeRateRepository(get(), get()) }
}

fun provideRetrofit(moshi: Moshi): Retrofit {
    val client = OkHttpClient.Builder().apply {
        addNetworkInterceptor(FlipperOkhttpInterceptor(NetworkFlipperPlugin()))
        addInterceptor(
            HttpLoggingInterceptor().setLevel(
                if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BASIC
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            )
        )
        followSslRedirects(false)
        followRedirects(false)
    }.build()

    return Retrofit.Builder()
        .baseUrl(BuildConfig.API_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(client)
        .build()
}

fun provideMoshi(): Moshi {
    return Moshi.Builder()
        .add(RateValueAdapter())
        .add(KotlinJsonAdapterFactory())
        .build()
}

fun provideApiService(retrofit: Retrofit): ExchangeRatesApiService = retrofit.create(
    ExchangeRatesApiService::class.java
)