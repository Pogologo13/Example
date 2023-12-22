package com.app.skillcinema.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.app.skillcinema.data.retrofit.FilmItem
import com.app.skillcinema.data.retrofit.FilmWithActor
import com.app.skillcinema.data.retrofit.Photo
import com.app.skillcinema.domain.UseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailViewModel @Inject constructor(
    private val useCase: UseCase
) : ViewModel() {


    fun setPremiere(date:String) = useCase.getPremiere(date)



    private val _filmWithActor = MutableStateFlow<List<FilmItem>>(emptyList())
    val filmWithActor get() = _filmWithActor.asStateFlow()

    private val _photos =MutableStateFlow<PagingData<Photo>?>(null)
    val photos get() = _photos.asStateFlow()

    private val _similarStateLoading = MutableStateFlow(false)
    val similarStateLoading get() = _similarStateLoading.asStateFlow()

    val savedListAll = useCase.getSavedListAll()

    fun setFilm(kinopoiskId: Int, premiereDate:String?, premiereRu: String?) {
        viewModelScope.launch {
            useCase.setFilmForIdFromRest(kinopoiskId, premiereDate, premiereRu)
        }
    }

    fun getItemFilm(id: Int): Flow<FilmItem?> = useCase.getFilmFromLocal(id)

    fun updateFilm(filmItem: FilmItem) {
        viewModelScope.launch {
            useCase.updateFilm(filmItem)
        }
    }

    fun getHumans(id: Int) = useCase.getHumansFromLocal(id)

    fun getSerial(id: Int) = useCase.getSerialFromLocal(id)

    fun setSerial(id: Int) {
        viewModelScope.launch {
            useCase.setSerialForIdFromRest(id)
        }
    }

    fun setHumanDetail(stuffId: Int) {
        viewModelScope.launch {
            useCase.setHumanDetail(stuffId)
        }
    }

    fun createSavedList(listName: String) {
        viewModelScope.launch {
            useCase.insertSavedList(listName)
        }
    }

    fun getHumanDetail(stuffId: Int) =
        useCase.getHumanDetailFromLocal(stuffId).stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(5000L), null
        )


    fun getPhoto(kinopoiskId: Int, type: String)= useCase.getPhoto(kinopoiskId, type)

    suspend fun getSimilar(kinopoiskId: Int): MutableList<FilmItem> {
        val list = emptyList<FilmItem>().toMutableList()
        val response = useCase.getSimilars(kinopoiskId)
        response.items.forEach {
            useCase.setFilmsWithActor(it.filmId)
            val item = getItemFilm(it.filmId)
            item.first()?.let { it1 -> list.add(it1) }
        }
        _similarStateLoading.value = false
        return list
    }


    suspend fun buttonIsChecked(kinopoiskId: Int, name: String) = useCase.isChecked(kinopoiskId, name)

    fun updateInSavedList(isChecked: Boolean, filmItem: FilmItem, listName: String) {
        viewModelScope.launch {
            if (isChecked) useCase.insertSingleSavedItem(listName, filmItem)
            else useCase.deleteSingleSavedItem(filmItem, listName)
        }
    }

    fun setFilmsWithActorList(filmsIdList: List<FilmWithActor>) {
        viewModelScope.launch {
            val listFilm = emptyList<FilmItem>().toMutableList()
            filmsIdList.forEach {
                useCase.setFilmsWithActor(it.filmId)
                val item = useCase.getFilmFromLocal(it.filmId).firstOrNull()
                item?.let { film -> listFilm.add(film) }
            }
            _filmWithActor.value = listFilm

        }
    }

    fun uploadFilm(kinopoiskId: Int, premiereDate: String?, premiereRu: String?, isSerial: Boolean) {
        viewModelScope.launch {
            useCase.setFilmForIdFromRest(kinopoiskId,premiereDate,premiereRu)
            useCase.setHumansFromRest(kinopoiskId)
            if (isSerial) useCase.setSerialForIdFromRest(kinopoiskId)
        }
    }

    fun getFilmCollections(type: String, isAllPage: Boolean) = useCase.execute(type, isAllPage).cachedIn(viewModelScope)

    fun getListByName(name: String) = useCase.getListItemByName(name)


    fun getGalleryById(kinopoiskId: Int) = useCase.getGalleryByKinopoiskId(kinopoiskId)

    fun getPhotoByType(kinopoiskId: Int, type: String) = useCase.getPhotoByType(kinopoiskId, type)
}


