package com.app.skillcinema.instance

import com.app.skillcinema.data.retrofit.FilmWithActor
import com.app.skillcinema.data.retrofit.Spouse

interface HumanDetail {
    val age: Int?
    val birthday: String?
    val birthplace: String?
    val death: String?
    val deathplace: String?
    val facts: List<String>?
    val films: List<FilmWithActor>?
    val growth: Int?
    val hasAwards: Int?
    val nameEn: String?
    val nameRu: String?
    val personId: Int?
    val posterUrl: String?
    val profession: String?
    val sex: String?
    val spouses: List<Spouse>?
    val webUrl: String?
}