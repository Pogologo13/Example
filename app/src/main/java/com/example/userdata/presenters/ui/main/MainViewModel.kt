package com.example.userdata.presenters.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.userdata.data.MainRepository
import com.example.userdata.data.UserDetail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


class MainViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private var _singleUser = MutableStateFlow<UserDetail?>(null)
    val singleUser = _singleUser.asStateFlow()

    //пейджер постраничной загрузки данных
    fun pagedUser() = repository.pagedUser.cachedIn(viewModelScope)

    fun getSingle(login: String) {
        viewModelScope.launch {
            try {
                val response = repository.getSingleUser(login)
                _singleUser.value = response
            }catch (e:Exception){
             Log.d("REST Single User EXCEPTION", e.message.toString())
            }
        }
    }
}
