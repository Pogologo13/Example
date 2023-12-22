package com.app.skillcinema.data.retrofit

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface SerialApi {

    @GET("/api/v2.2/films/{id}/seasons")
    suspend fun getSerials(
        @Header("X-API-KEY") token: String,
        @Path("id") id: Int
    ): SerialDto
}