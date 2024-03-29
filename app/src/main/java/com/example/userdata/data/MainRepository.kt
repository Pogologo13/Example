package com.example.userdata.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import javax.inject.Inject

class MainRepository @Inject constructor(private val userGitHubApi: UserGitHubApi) {

    // Асинхронный запрос данных
    suspend fun getUserList(page: Int) = userGitHubApi.getUserList(page = page)

    //пейджер постраничной загрузки данных
    val pagedUser = Pager(
        config = PagingConfig(30),
        null,
        pagingSourceFactory = { UserDataPagingSource(userGitHubApi) }
    ).flow

    suspend fun getSingleUser(login: String) = userGitHubApi.getSingleUser(login)


}