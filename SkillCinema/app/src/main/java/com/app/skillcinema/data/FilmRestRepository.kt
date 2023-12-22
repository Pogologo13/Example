package com.app.skillcinema.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.app.skillcinema.data.retrofit.*
import com.app.skillcinema.data.room.FilmDao
import com.app.skillcinema.data.room.HumanDao
import com.app.skillcinema.data.room.PhotoDao
import com.app.skillcinema.data.room.PhotoRemoteMediator
import com.app.skillcinema.utils.ApiKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FilmRestRepository @Inject constructor(
    private val filmApi: FilmApi,
    private val filmLocalRepository: FilmLocalRepository,
    private val humanApi: HumanApi,
    private val serialApi: SerialApi,
    private val apiKey: ApiKey,
    private val photoDao: PhotoDao,
    private val humanDao: HumanDao,
    private val filmDao: FilmDao
) {
    @OptIn(ExperimentalPagingApi::class)
    fun getSeveralFilms(type: String, isAllPage: Boolean): Flow<PagingData<FilmItem>> {
        return Pager(
            config = PagingConfig(20),
            initialKey = null,
            remoteMediator = FilmRemoteMediator(filmApi, apiKey, filmDao).apply {
                this.getPagedFilm(type, isAllPage)
            },
            pagingSourceFactory = {
                val limit = if(isAllPage) 999 else 20
                filmDao.getPagingData(type, limit) }
        ).flow
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
    ): Flow<PagingData<FilmItem>> = Pager(

        config = PagingConfig(20),
        initialKey = null,
        pagingSourceFactory = {
            FilmByKeywordsPagingSource(filmApi, filmLocalRepository, apiKey).apply {
                this.getFilmsByKeywords(
                    countries, genres, order, type, ratingFrom, ratingTo, yearFrom, yearTo, keywords, viewed
                )
            }
        }
    ).flow


    fun getHumansByKeywords(words: String): Flow<PagingData<HumanItem>> {
        return Pager(
            config = PagingConfig(20),
            initialKey = null,
            pagingSourceFactory = {
                HumanByKeywordsPagingSource(humanApi, filmLocalRepository, apiKey).apply {
                    this.getHumansByKeywords(words)
                }
            }
        ).flow
    }


    @OptIn(ExperimentalPagingApi::class)
    fun getPagedPhotos(idFilm: Int, type: String): Flow<PagingData<Photo>> {
        return Pager(
            config = PagingConfig(20),
            initialKey = null,
            remoteMediator = PhotoRemoteMediator(filmApi, apiKey, photoDao).apply {
                this.getPagedPhoto(idFilm, type)
            },
            pagingSourceFactory = { photoDao.getPagingData(idFilm) }
        ).flow
    }

    suspend fun updateOneFilm(id: Int, premiereDate: String?, premiereRu: String?) {
        try {
            val oldFilm = filmLocalRepository.getItemFromId(id).firstOrNull()
            if (oldFilm?.interested == false) filmLocalRepository.updateFilm(oldFilm.apply {
                this.interested = true
                this.premiere = premiereDate
                this.premiereRu = premiereRu
            })
            if (oldFilm?.description == null) {
                runCatching { filmApi.getOneFilm(apiKey.getKey(), id) }.fold(
                    onSuccess = {
                        filmLocalRepository.updateFilm(it.apply {
                            this.interested = true
                            this.collectionName = oldFilm?.collectionName
                            this.premiere = premiereDate
                            this.premiereRu = premiereRu
                        })
                    },
                    onFailure = {
                        apiKey.changeKey()
                        val item = filmApi.getOneFilm(apiKey.getKey(), id)
                        filmLocalRepository.updateFilm(item.apply {
                            this.interested = true
                            this.premiere = premiereDate
                            this.premiereRu = premiereRu
                            this.collectionName = oldFilm?.collectionName
                        })
                    }
                )
            }

        } catch (_: Exception) {
        }
    }

    suspend fun setPremiers(year: Int, month: String) {
        val date = "$year-$month"
        val list = filmLocalRepository.getPremiere(date).first()
        try {
            if (list == emptyList<FilmItem>()) {
                runCatching { filmApi.getPremiers(apiKey.getKey(), year, month) }.fold(
                    onSuccess = {
                        it.items.onEach { filmItem -> filmLocalRepository.insertFilm(filmItem.copy(premiere = date)) }
                    },
                    onFailure = {
                        apiKey.changeKey()
                        val item = filmApi.getPremiers(apiKey.getKey(), year, month)
                        item.items.onEach { filmItem -> filmLocalRepository.insertFilm(filmItem.copy(premiere = date)) }
                    }
                )
            }
        } catch (_: Exception) {
        }
    }

    suspend fun setHuman(id: Int) {
        try {
            val list = filmLocalRepository.getHumanFromId(id).first()
            if (list == emptyList<HumanItem>()) {
                runCatching { humanApi.getHuman(apiKey.getKey(), id) }.fold(
                    onSuccess = { it?.let { it1 -> filmLocalRepository.insertHuman(it1, id) } },
                    onFailure = {
                        apiKey.changeKey()
                        val item = humanApi.getHuman(apiKey.getKey(), id)
                        item?.let { thisItem -> filmLocalRepository.insertHuman(thisItem, id) }
                    }
                )
            }
        } catch (_: Exception) {
        }
    }

    suspend fun setSerial(id: Int) {
        try {
            val serial = filmLocalRepository.getSerialFromId(id).firstOrNull()
            if (serial == null) {
                runCatching { serialApi.getSerials(apiKey.getKey(), id) }.fold(
                    onSuccess = { filmLocalRepository.insertSerial(it, id) },
                    onFailure = {
                        apiKey.changeKey()
                        val item = serialApi.getSerials(apiKey.getKey(), id)
                        filmLocalRepository.insertSerial(item, id)
                    }
                )
            }
        } catch (_: Exception) {
        }
    }

    suspend fun setSimilars(id: Int): Similars {
        runCatching { filmApi.getSimilars(apiKey.getKey(), id) }.fold(
            onSuccess = { return it },
            onFailure = { return Similars(emptyList(), 0) }
        )
    }

    suspend fun setHumanDetail(stuffId: Int) {
        try {
            val humanDetail = filmLocalRepository.getHumanDetailFromId(stuffId).firstOrNull()
            if (humanDetail == null) {
                runCatching { humanApi.getDetailHuman(apiKey.getKey(), stuffId) }.fold(
                    onSuccess = { filmLocalRepository.insertHumanDetail(it) },
                    onFailure = {
                        apiKey.changeKey()
                        val item = humanApi.getDetailHuman(apiKey.getKey(), stuffId)
                        filmLocalRepository.insertHumanDetail(item)
                    }
                )
            }
        } catch (_: Exception) {
        }
    }

    suspend fun setFilmsWithActor(filmsId: Int) {
        try {
            withContext(Dispatchers.IO) {
                if (filmLocalRepository.getItemFromId(filmsId).firstOrNull() == null) {
                    val getFilm = filmApi.getOneFilm(apiKey.getKey(), filmsId)
                    filmLocalRepository.insertFilm(getFilm)
                }
            }

        } catch (_: Exception) {
        }

    }
}