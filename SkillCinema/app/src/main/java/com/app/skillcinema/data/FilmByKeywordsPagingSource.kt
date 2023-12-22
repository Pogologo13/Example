package com.app.skillcinema.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.app.skillcinema.data.retrofit.FilmApi
import com.app.skillcinema.data.retrofit.FilmItem
import com.app.skillcinema.data.retrofit.FilmsDto
import com.app.skillcinema.utils.ApiKey
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.firstOrNull
import java.util.stream.Collectors
import javax.inject.Inject
import kotlin.properties.Delegates

private const val FIRST_PAGE = 1

class FilmByKeywordsPagingSource @Inject constructor(
    private val filmApi: FilmApi,
    private val localRepository: FilmLocalRepository,
    private val apiKey: ApiKey
) : PagingSource<Int, FilmItem>(
) {

    private var countriesKey: Int? = null
    private var genresKey: Int? = null
    private lateinit var orderKey: String
    private lateinit var typeKey: String
    private var ratingFromKey by Delegates.notNull<Int>()
    private var ratingToKey by Delegates.notNull<Int>()
    private var yearFromKey by Delegates.notNull<Int>()
    private var yearToKey by Delegates.notNull<Int>()
    private lateinit var keywordsKey: String
    private var beenViewed by Delegates.notNull<Boolean>()
    override fun getRefreshKey(state: PagingState<Int, FilmItem>) = FIRST_PAGE
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FilmItem> {
        val page = params.key ?: FIRST_PAGE
        return kotlin.runCatching {
            getList(
                countriesKey,
                genresKey,
                orderKey,
                typeKey,
                ratingFromKey,
                ratingToKey,
                yearFromKey,
                yearToKey,
                keywordsKey,
                page
            )
        }.fold(
            onSuccess = {
                LoadResult.Page(
                    checkIn(it.items),
                    prevKey = null,
                    nextKey = if (it.totalPages > page) page + 1 else null
                )
            },
            onFailure = { LoadResult.Error(it) }
        )
    }

    fun getFilmsByKeywords(
        countries: Int?,
        genres: Int?,
        order: String,
        type: String,
        ratingFrom: Int,
        ratingTo: Int,
        yearFrom: Int,
        yearTo: Int,
        keywords: String,
        viewed: Boolean
    ) {
        countriesKey = countries
        genresKey = genres
        orderKey = order
        typeKey = type
        ratingFromKey = ratingFrom
        ratingToKey = ratingTo
        yearFromKey = yearFrom
        yearToKey = yearTo
        keywordsKey = keywords
        beenViewed = viewed
    }

    // подмена данных из базы и данных из сети
    private suspend fun checkIn(innerList: List<FilmItem>): List<FilmItem> {
        var storeFilmList: List<FilmItem>

        coroutineScope {
            storeFilmList =
                localRepository.getItemByViewed().firstOrNull() ?: emptyList() //список всех просмотренных фильмов
        }

        return if (!beenViewed) {  // списпок из kinopoiskId просмотренных фильмов
            val ids: MutableSet<Int?> =
                storeFilmList.stream().map { film -> film.kinopoiskId }.collect(Collectors.toSet())!!

            val common: List<FilmItem> = innerList.stream()   // общие елементы списков из сети и просмотренных фильмов
                .filter { film -> ids.contains(film.kinopoiskId) }
                .collect(Collectors.toList())

            val finishList = innerList.toMutableList()
            finishList.removeAll(common)  // удалить все просмотренные из списка из сети

            finishList
        } else innerList

    }

    private suspend fun getList(
        countries: Int?,
        genres: Int?,
        order: String,
        type: String,
        ratingFrom: Int,
        ratingTo: Int,
        yearFrom: Int,
        yearTo: Int,
        keywords: String,
        page: Int
    ): FilmsDto {

        return runCatching {
            filmApi.getFilteredFilmsList(
                apiKey.getKey(), countries, genres, order, type, ratingFrom, ratingTo, yearFrom, yearTo, keywords, page
            )
        }.fold(
            onSuccess = {
                localRepository.insertCollectionFilm(it, "")
                it
            },
            onFailure = {
                apiKey.changeKey()
                val item = filmApi.getFilteredFilmsList(
                    apiKey.getKey(),
                    countries,
                    genres,
                    order,
                    type,
                    ratingFrom,
                    ratingTo,
                    yearFrom,
                    yearTo,
                    keywords,
                    page
                )
                localRepository.insertCollectionFilm(item, "")
                item
            }
        )
    }
}