package com.app.skillcinema.data.retrofit



import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface FilmApi {
    @GET("/api/v2.2/films/collections")
    suspend fun getCollections(
        @Header("X-API-KEY") token: String,
        @Query("page") page: Int = 1,
        @Query("type") type: String
    ): FilmsDto

    @GET("/api/v2.2/films/premieres?year=2023&month=DECEMBER")
    suspend fun getPremiers(
        @Header("X-API-KEY") token: String,
        @Query("year") year: Int,
        @Query("month") month: String
    ): FilmsDto

    @GET("/api/v2.2/films/{id}")
    suspend fun getOneFilm(
        @Header("X-API-KEY") token: String,
        @Path("id") id: Int
    ): FilmItem


    @GET("/api/v2.2/films?order=RATING&type=TV_SERIES&ratingFrom=0&ratingTo=10&yearFrom=1000&yearTo=3000")
    suspend fun getSerials(
        @Header("X-API-KEY") token: String,
        @Query("page") page: Int = 1
    ): FilmsDto


    @GET("/api/v2.2/films/{id}/images")
    suspend fun getPhotos(
        @Header("X-API-KEY") token: String,
        @Path("id") id: Int,
        @Query("type") type: String,
        @Query("page") page: Int = 1
    ): Gallery


    @GET("/api/v2.2/films/{id}/similars")
    suspend fun getSimilars(
        @Header("X-API-KEY") token: String,
        @Path("id") id: Int
    ): Similars

    @GET("/api/v2.2/films")
    suspend fun getFilteredFilmsList(
        @Header("X-API-KEY") token: String,
        @Query("countries") countries: Int?,
        @Query("genres") genres: Int? ,
        @Query("order") order: String ,
        @Query("type") type: String,
        @Query("ratingFrom") ratingFrom: Int,
        @Query("ratingTo") ratingTo: Int,
        @Query("yearFrom") yearFrom: Int,
        @Query("yearTo") yearTo: Int,
        @Query("keyword") keyword: String,
        @Query("page") page: Int
    ): FilmsDto



}