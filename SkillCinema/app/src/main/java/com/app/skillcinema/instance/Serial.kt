package com.app.skillcinema.instance

import com.app.skillcinema.data.retrofit.SerialItem

interface Serial {
    val items: List<SerialItem>
    val total: Int
    val id: Int?
    val kinopoiskId: Int?
}