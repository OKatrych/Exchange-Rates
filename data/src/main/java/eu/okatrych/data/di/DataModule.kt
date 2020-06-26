package eu.okatrych.data.di

import com.squareup.moshi.Moshi
import eu.okatrych.data.BuildConfig
import eu.okatrych.data.model.adapter.ExchangeRateAdapter
import eu.okatrych.data.model.mapper.ExchangeRateToDomainMapper
import eu.okatrych.data.source.ExchangeRateRepository
import eu.okatrych.data.source.remote.ExchangeRatesApiService
import eu.okatrych.domain.repository.IExchangeRateRepository
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val dataModule = module {
    single { provideRetrofit(provideMoshi()) }
    single { provideApiService(get()) }

    factory { ExchangeRateToDomainMapper() }
    single<IExchangeRateRepository> { ExchangeRateRepository(get(), get()) }
}

fun provideRetrofit(moshi: Moshi): Retrofit {
    val client = OkHttpClient.Builder().apply {
        addInterceptor(
            HttpLoggingInterceptor().setLevel(
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BASIC
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        ))
        followSslRedirects(false)
        followRedirects(false)
    }.build()

    val contentType = MediaType.get("application/json")
    return Retrofit.Builder()
        .baseUrl(BuildConfig.API_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(client)
        .build()
}

fun provideMoshi(): Moshi {
    return Moshi.Builder()
        .add(ExchangeRateAdapter())
        .build()
}

fun provideApiService(retrofit: Retrofit): ExchangeRatesApiService = retrofit.create(ExchangeRatesApiService::class.java)