package com.app.skillcinema.instance

interface Human {
    val id: Int
    var kinopoiskId: Int?
    val staffId: Int?
    val description: String?
    val nameEn: String?
    val nameRu: String?
    val posterUrl: String?
    val professionKey: String?
    val professionText: String?
}