package com.app.skillcinema.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.app.skillcinema.data.retrofit.FilmItem
import com.app.skillcinema.domain.UseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val useCase: UseCase
) : ViewModel() {
    private val _premiereFilms = MutableStateFlow<List<FilmItem>?>(null)
    val premiereFilms get() = _premiereFilms.asStateFlow()

    fun getFilmCollections(type: String, isAllPage: Boolean) = useCase.execute(type, isAllPage).cachedIn(viewModelScope)

    fun setPremiere(year: Int, month: String) {
        viewModelScope.launch {
            useCase.setPremiere(year, month)
            val date = "$year-$month"
            _premiereFilms.value = useCase.getPremiere(date).firstOrNull()
        }
    }

    fun setBoolean(isFirstTime: Int) {
        viewModelScope.launch {
            useCase.setForPrefs(isFirstTime)
        }
    }

    fun getBoolean() = useCase.getFromPrefs()


    fun setDefaultSaveList() {
        viewModelScope.launch {
            useCase.insertSavedList("Любимые")
            useCase.insertSavedList("Хочу посмотреть")
        }
    }
}