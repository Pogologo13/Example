package com.app.skillcinema.domain

import com.app.skillcinema.data.FilmLocalRepository
import com.app.skillcinema.data.FilmRestRepository
import com.app.skillcinema.data.retrofit.FilmItem
import javax.inject.Inject

class
UseCase @Inject constructor(
    private val restRepository: FilmRestRepository,
    private val localRepository: FilmLocalRepository
) {

    fun getAllFilms() = localRepository.getAllFilms()

    fun getAllHumans() = localRepository.getAllHumans()
    fun execute(type: String, isAllPage: Boolean) = restRepository.getSeveralFilms(type, isAllPage)

    suspend fun setHumansFromRest(id: Int) = restRepository.setHuman(id)

    suspend fun setFilmForIdFromRest(id: Int, premiereDate: String?, premiereRu: String?) = restRepository.updateOneFilm(id,premiereDate,premiereRu)
    suspend fun setPremiere(year: Int, month: String) = restRepository.setPremiers(year, month)

    suspend fun setSerialForIdFromRest(id: Int) = restRepository.setSerial(id)

    fun getPhoto(kinopoiskId: Int, type: String) =
        restRepository.getPagedPhotos(kinopoiskId, type)

    suspend fun getSimilars(kinopoiskId: Int) = restRepository.setSimilars(kinopoiskId)

    suspend fun setHumanDetail(stuffId: Int) = restRepository.setHumanDetail(stuffId)

    suspend fun updateFilm(filmItem: FilmItem) = localRepository.updateFilm(filmItem)

    fun getFilmFromLocal(id: Int) = localRepository.getItemFromId(id)


    fun getHumansFromLocal(id: Int) = localRepository.getHumanFromId(id)

    fun getSerialFromLocal(id: Int) = localRepository.getSerialFromId(id)

    fun getHumanDetailFromLocal(stuffId: Int) = localRepository.getHumanDetailFromId(stuffId)

    suspend fun setForPrefs(isDone: Int) = localRepository.setForPrefs(isDone)

    fun getFromPrefs() = localRepository.getFromPrefs()

    fun getSavedListAll() = localRepository.savedListAll()

    fun getSingleSavedItem(listName: String) = localRepository.savedListSingle(listName)


    suspend fun insertSavedList(name: String) = localRepository.insertSaveList(name)

    suspend fun isChecked(kinopoiskId: Int, name: String) = localRepository.isChecked(kinopoiskId, name)

    suspend fun insertSingleSavedItem(name: String, filmItem: FilmItem) =
        localRepository.insertSavedItem(name, filmItem)

    suspend fun deleteSingleSavedItem(filmItem: FilmItem, name: String) =
        localRepository.deleteSaveListItem(filmItem, name)

    suspend fun setFilmsWithActor(filmsId: Int) = restRepository.setFilmsWithActor(filmsId)

    suspend fun deleteSavedList(savedListName: String) = localRepository.deleteSavedList(savedListName)

    fun getListItemByName(name: String) = localRepository.getListSavedItem(name)

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
    ) = restRepository.getFilmsByKeywords(
        countries,
        genres,
        order,
        type,
        ratingFrom,
        ratingTo,
        yearFrom,
        yearTo,
        keywords,
        viewed
    )

    fun getPremiere(date:String) = localRepository.getPremiere(date)

    fun getHumansByKeywords(name: String) = restRepository.getHumansByKeywords(name)

    fun getGalleryByKinopoiskId(kinopoiskId: Int) = localRepository.getGalleryByKinopoiskId(kinopoiskId)

    fun getPhotoByType(kinopoiskId: Int, type: String) = localRepository.getPhotoByType(kinopoiskId, type)
}