package com.app.skillcinema.instance

import com.app.skillcinema.data.retrofit.FilmItem

interface Films {
    val items: List<FilmItem>
    val total: Int
    val totalPages: Int
}

