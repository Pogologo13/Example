package com.app.skillcinema.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.app.skillcinema.data.retrofit.FilmApi
import com.app.skillcinema.data.retrofit.FilmItem
import com.app.skillcinema.data.retrofit.FilmsDto
import com.app.skillcinema.data.room.FilmDao
import com.app.skillcinema.utils.ApiKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ExperimentalPagingApi
class FilmRemoteMediator @Inject constructor(
    private val filmApi: FilmApi,
    private val apiKey: ApiKey,
    private val filmDao: FilmDao
) : RemoteMediator<Int, FilmItem>() {

    private lateinit var filmCollections: String
    private var pageCountAll = false
    private var pageIndex = FIRST_PAGE

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, FilmItem>
    ): MediatorResult {

        pageIndex = getPageIndex(loadType) ?: return MediatorResult.Success(endOfPaginationReached = true)

        return try {
            val filmsDto = getCollections(page = pageIndex, type = filmCollections)
            insertCollectionFilm(filmsDto, filmCollections)
            MediatorResult.Success(endOfPaginationReached = filmsDto.total == pageIndex)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private fun getPageIndex(loadType: LoadType): Int? {
        pageIndex = when (loadType) {
            LoadType.REFRESH -> 1
            LoadType.PREPEND -> return null
            LoadType.APPEND -> if(pageCountAll)++pageIndex else {
                pageIndex = 1
                1
            }
        }
        return pageIndex
    }

    // перед запросом в сеть инициалиризуем квери параметры
    fun getPagedFilm(type: String, isAllPage: Boolean) {
        filmCollections = type
        pageCountAll = isAllPage
    }

    private suspend fun getCollections(page: Int, type: String): FilmsDto {
        return if (type == "SERIALS") {
            runCatching { filmApi.getSerials(apiKey.getKey(), page) }.fold(
                onSuccess = { it },
                onFailure = {
                    apiKey.changeKey()
                    filmApi.getSerials(apiKey.getKey(), page)
                },
            )
        } else {
            runCatching { filmApi.getCollections(apiKey.getKey(), page, type) }.fold(
                onSuccess = { it },
                onFailure = {
                    apiKey.changeKey()
                    filmApi.getCollections(apiKey.getKey(), page, type)
                }
            )
        }
    }

    private suspend fun insertCollectionFilm(film: FilmsDto, type: String) {
        withContext(Dispatchers.IO) {
            film.items.forEach {
                val item = it.apply {
                    val set = emptySet<String>().toMutableSet()
                    set.add(type)
                    this.collectionName = if (this.collectionName == null) {
                        emptySet<String>().toMutableSet().plus(set)
                    } else this.collectionName!!.plus(set)
                }
                filmDao.insertSingle(item)
            }
        }
    }

    companion object {
        const val FIRST_PAGE = 1
    }
}