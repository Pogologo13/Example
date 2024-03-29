package com.example.userdata.di

import com.example.userdata.data.UserGitHubApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

class NetworkModel {
    @Module
    @InstallIn(SingletonComponent::class)
    object NetworkModule {

        @Provides
        @Singleton
        fun provideRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
        }
        @Provides
        @Singleton
        fun provideApiService(retrofit: Retrofit): UserGitHubApi {
            return retrofit.create(UserGitHubApi::class.java)
        }
    }

}