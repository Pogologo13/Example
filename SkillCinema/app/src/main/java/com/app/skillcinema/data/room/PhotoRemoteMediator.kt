package com.app.skillcinema.data.room

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.app.skillcinema.data.retrofit.FilmApi
import com.app.skillcinema.data.retrofit.Photo
import com.app.skillcinema.utils.ApiKey
import javax.inject.Inject

@ExperimentalPagingApi
class PhotoRemoteMediator @Inject constructor(
    private val filmApi: FilmApi,
    private val apiKey: ApiKey,
    private val photoDao: PhotoDao
) : RemoteMediator<Int, Photo>() {

    private var kinopoiskId = 305
    private var queryType = ""
    private var pageIndex = FIRST_PAGE

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Photo>
    ): MediatorResult {

        pageIndex = getPageIndex(loadType) ?: return MediatorResult.Success(endOfPaginationReached = true)


        return try {
            val gallery = runCatching {
                filmApi.getPhotos(apiKey.getKey(), id = kinopoiskId, type = queryType, page = pageIndex)
            }.fold(
                onSuccess = { it },
                onFailure = {
                    apiKey.changeKey()
                    filmApi.getPhotos(apiKey.getKey(), id = kinopoiskId, type = queryType, page = pageIndex)
                })

            val list = emptyList<Photo>().toMutableList()

            gallery.items.onEach {
                list.add(it.copy(kinopoiskId = kinopoiskId, type = queryType))
            }
            photoDao.insertPhoto(list)

            MediatorResult.Success(
                endOfPaginationReached = gallery.totalPages == pageIndex
            )
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private fun getPageIndex(loadType: LoadType): Int? {
        pageIndex = when (loadType) {
            LoadType.REFRESH -> 1
            LoadType.PREPEND -> return null
            LoadType.APPEND -> ++pageIndex
        }
        return pageIndex
    }

    fun getPagedPhoto(idFilm: Int, type: String) {
        kinopoiskId = idFilm
        queryType = type
    }

    companion object {
        const val FIRST_PAGE = 1
    }
}