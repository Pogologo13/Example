package com.app.skillcinema.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.app.skillcinema.data.retrofit.FilmItem
import com.app.skillcinema.data.retrofit.HumanItem
import com.app.skillcinema.domain.UseCase
import com.app.skillcinema.utils.State
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val useCase: UseCase
) : ViewModel() {

    private val errorText2 = "Содержит недопустимые символы"

    private val _state = MutableStateFlow<State>(State.Success)
    val state = _state.asStateFlow()

    private val _humans = MutableStateFlow<PagingData<HumanItem>?>(null)
    val humans get() = _humans.asStateFlow()

    private val _films = MutableStateFlow<PagingData<FilmItem>?>(null)
    val films get() = _films.asStateFlow()


    fun findingFilms(
        countries: Int?,
        genres: Int?,
        order: String,
        type: String,
        ratingFrom: Int,
        ratingTo: Int,
        yearFrom: Int,
        yearTo: Int,
        keywords: String,
        viewed: Boolean,
    ) {
        viewModelScope.coroutineContext.cancelChildren()
        viewModelScope.launch {
            delay(300)
            if (keywords.contains("[!\"#$%&'()*+,-./:;\\\\<=>?@\\[\\]^_`{|}~]".toRegex())) {
                _state.value = State.Error(errorText2)
            } else {
                _state.value = State.Loading

                useCase.getFilmsByKeywords(
                    countries, genres, order, type, ratingFrom, ratingTo, yearFrom, yearTo, keywords, viewed
                ).onEach { _films.value = it }.launchIn(viewModelScope)
                _films.value = null

                useCase.getHumansByKeywords(keywords).onEach {
                    _humans.value = it
                }.launchIn(viewModelScope)
                _humans.value = null

                _state.value = State.Success
            }
        }
    }

    fun setHumanDetail(stuffId: Int) {
        viewModelScope.launch {
            useCase.setHumanDetail(stuffId)
        }
    }
}