package com.app.skillcinema.instance

import com.app.skillcinema.data.retrofit.Country
import com.app.skillcinema.data.retrofit.Genre

interface Film {
    val kinopoiskId: Int
    val countries: List<Country>?
    val description: String?
    val filmLength: String?
    val genres: List<Genre>?
    val nameEn: String?
    val nameRu: String?
    val posterUrl: String?
    val posterUrlPreview: String?
    val type: String?
    val year: Int?
}