package com.app.skillcinema.data.retrofit

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface HumanApi {
    @GET("api/v1/staff")
    suspend fun getHuman(
        @Header("X-API-KEY") token: String,
        @Query("filmId") id: Int
    ): List<HumanItem>?

    @GET("/api/v1/staff/{id}")
    suspend fun getDetailHuman(
        @Header("X-API-KEY") token: String,
        @Path("id") id: Int
    ): HumanDetailDto


    @GET("/api/v1/persons")
    suspend fun getHumansByKeywords(
        @Header("X-API-KEY") token: String,
        @Query("name") name: String,
        @Query("page") page: Int =1
    ): HumanByKeywordDto
}