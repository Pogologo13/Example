package com.example.userdata.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import javax.inject.Inject

class UserDataPagingSource @Inject constructor(
    private val userGitHubApi: UserGitHubApi
) : PagingSource<Int, User>() {

    //при сбросе загружаем данные нвчиная с первой стрницы
    override fun getRefreshKey(state: PagingState<Int, User>): Int = FIRST_PAGE

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        val page = params.key ?: FIRST_PAGE // устанавливаем страницу
        return kotlin.runCatching {
            // асинхронный запрос к данным
            userGitHubApi.getUserList(page)
        }.fold(
            // есл запрос прошел - взвращаем LoadResult.Page
            onSuccess = {
                LoadResult.Page(
                    data = it.items,
                    prevKey = null ,
                    nextKey = if (it.items == emptyList<User>()) null else page + 1
                )
            },
            // если запрос не прошел
            onFailure = {
                LoadResult.Error(it)
            }
        )
    }
    companion object{
        const val FIRST_PAGE = 1
    }
}