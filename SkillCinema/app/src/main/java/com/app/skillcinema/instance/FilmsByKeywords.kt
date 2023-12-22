package com.app.skillcinema.instance

import com.app.skillcinema.data.retrofit.FilmItem

interface FilmsByKeywords {
    val films: List<FilmItem>
    val keyword: String
    val pagesCount: Int
    val searchFilmsCountResult: Int
}