package com.app.skillcinema.ui.profile

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.skillcinema.domain.UseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val useCase: UseCase
) : ViewModel() {

    private val _isCreated = MutableStateFlow(false)
    val isCreated get() = _isCreated.asStateFlow()

    val savedListAll = useCase.getSavedListAll()
    fun createSavedList(listName: String, context: Context) {
        viewModelScope.launch {
            _isCreated.value = false
            val list = emptyList<String>().toMutableList()
            savedListAll.first()?.forEach {
                list.add(it.savedList.listItemName)
            }
            if (list.contains(listName)) {
                Toast.makeText(context, "Список уже существует", Toast.LENGTH_SHORT).show()
            } else {
                useCase.insertSavedList(listName)
                _isCreated.value = true
            }
        }
    }

    fun deleteSavedList(savedListName: String) {
        viewModelScope.launch {
            useCase.deleteSavedList(savedListName)
        }
    }

    suspend fun getViewedFilms() = flow {
        emit(useCase.getAllFilms().first().filter { it.viewed })
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    suspend fun getInterestedFilms() = flow {
        emit(useCase.getAllFilms().first().filter { it.interested })
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun getInterestedHumans() = flow {
        emit(useCase.getAllHumans().first())
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)
}