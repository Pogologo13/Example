package com.app.skillcinema.utils

sealed class State {
    data object Success: State()
    data object Loading: State()
    data class Error(val message: String): State()
}