package com.idimi.garage.api

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    const val BASE_URL = "https://api2.roadtrippers.com/api/v2/pois/"

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideGarageApi(retrofit: Retrofit): GarageAPI =
        retrofit.create(GarageAPI::class.java)
}

