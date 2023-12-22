package com.app.skillcinema.di

import com.app.skillcinema.data.retrofit.FilmApi
import com.app.skillcinema.data.retrofit.HumanApi
import com.app.skillcinema.data.retrofit.SerialApi
import com.app.skillcinema.utils.ApiKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetService {
    private const val BASE_URL = "https://kinopoiskapiunofficial.tech"

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }
    @Provides
    @Singleton
    fun provideFilmApiService( retrofit: Retrofit): FilmApi {
       return retrofit.create(FilmApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSerialApiService( retrofit: Retrofit): SerialApi {
       return retrofit.create(SerialApi::class.java)
    }

    @Provides
    @Singleton
    fun provideHumanApiService(retrofit: Retrofit): HumanApi {
        return retrofit.create(HumanApi::class.java)
    }

    @Provides
    @Singleton
    fun provideApiKey(): ApiKey = ApiKey

}