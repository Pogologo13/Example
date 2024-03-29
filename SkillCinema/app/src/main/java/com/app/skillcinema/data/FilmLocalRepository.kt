package com.app.skillcinema.data

import com.app.skillcinema.data.retrofit.*
import com.app.skillcinema.data.room.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FilmLocalRepository @Inject constructor(
    private val filmDao: FilmDao,
    private val humanDao: HumanDao,
    private val serialDao: SerialDao,
    private val savedListDao: SavedListDao,
    private val savedItemDao: SavedItemDao,
    private val humanDetailDao: HumanDetailDao,
    private val photoDao: PhotoDao,
    private val sharedPrefsRepository: SharedPrefsRepository
) {

    fun getAllFilms() = filmDao.getAllFilms()

    fun getPremiere(date:String) = filmDao.getPremiere(date)

    fun getItemFromId(id: Int) = filmDao.getSingleFilm(id)

    fun getHumanFromId(id: Int): Flow<List<HumanItem?>> {
        return humanDao.getSingleFilm(id)
    }

    fun getSerialFromId(id: Int): Flow<SerialDto?> {
        return serialDao.getSingleSerial(id)
    }

    fun getItemByViewed(): Flow<List<FilmItem>> {
        return filmDao.searchInLocalStore()
    }

    suspend fun insertCollectionFilm(film: FilmsDto, type: String) {
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

    suspend fun insertFoundedHumans(film: HumanByKeywordDto) {
        withContext(Dispatchers.IO) {
            film.items.onEach { humanDao.insertHuman(it.copy(staffId = it.kinopoiskId, kinopoiskId = null)) }
        }
    }

    suspend fun updateFilm(film: FilmItem) =
        withContext(Dispatchers.IO) {
            filmDao.updateSingle(film)
        }


    suspend fun insertFilm(film: FilmItem) =
        withContext(Dispatchers.IO) {
            filmDao.insertSingle(film)
        }


    suspend fun insertHuman(human: List<HumanItem>, id: Int) =
        withContext(Dispatchers.IO) {
            human.forEach {
                val item = it
                item.kinopoiskId = id
                humanDao.insertHuman(item)
            }
        }

    suspend fun insertSerial(serial: SerialDto, id: Int) =
        withContext(Dispatchers.IO) {
            val item = serial.copy(kinopoiskId = id)
            serialDao.insertSerial(item)
        }

    suspend fun setForPrefs(isDone: Int) = sharedPrefsRepository.setValueForDataStore(isDone)

    fun getFromPrefs() = sharedPrefsRepository.isFirstTimeFlow

    fun savedListAll() = savedListDao.getSavedList()

    fun savedListSingle(listName: String) = savedListDao.getSingleSavedList(listName)

    fun getAllHumans() = humanDetailDao.getAllHumanDetail()

    fun getListSavedItem(name: String) = savedItemDao.getListSavedItemByName(name)

    fun getHumanDetailFromId(stuffId: Int) = humanDetailDao.getSingleHumanDetail(stuffId)

    fun getGalleryByKinopoiskId(kinopoiskId: Int) = photoDao.getPhotoById(kinopoiskId)

    fun getPhotoByType(kinopoiskId: Int, type: String) = photoDao.getPhotoByType(kinopoiskId, type)

    suspend fun insertSaveList(name: String) {
        withContext(Dispatchers.IO) {
            val item = SavedList(name)
            savedListDao.insertSavedList(item)
        }
    }

    suspend fun deleteSavedList(name: String) {
        withContext(Dispatchers.IO) {
            val item = savedListDao.getSingleSavedList(name)
            item?.let { savedListDao.deleteSavedList(it) }
        }
    }

    suspend fun isChecked(kinopoiskId: Int, name: String): Boolean {
        var flag: Boolean
        withContext(Dispatchers.IO) {
            val check = savedItemDao.getSingleSavedItemById(kinopoiskId, name)
            flag = check != null
        }
        return flag
    }

    suspend fun insertSavedItem(name: String, film: FilmItem) {
        withContext(Dispatchers.IO) {
            val check = isChecked(film.kinopoiskId, name)
            if (!check) {
                val item = SavedItem(film, name, film.kinopoiskId)
                savedItemDao.insertSavedItem(item)
            }
        }
    }

    suspend fun deleteSaveListItem(film: FilmItem, name: String) {
        withContext(Dispatchers.IO) {
            val item = savedItemDao.getSingleSavedItemById(film.kinopoiskId, name)
            item?.let { savedItemDao.deleteSavedItem(it) }
        }
    }


    suspend fun insertHumanDetail(humanDetail: HumanDetailDto) {
        withContext(Dispatchers.IO) {
            humanDetailDao.insertHumanDetail(humanDetail)
        }
    }

}



