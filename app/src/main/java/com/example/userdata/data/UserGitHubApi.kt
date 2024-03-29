package com.example.userdata.data

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserGitHubApi {
    @GET("/search/users?q=a&page=1")
    suspend fun getUserList(
        @Query("page") page:Int
    ): UserList

    @GET("/users/{userName}")
    suspend fun getSingleUser(
        @Path("userName") userName: String
    ): UserDetail
}