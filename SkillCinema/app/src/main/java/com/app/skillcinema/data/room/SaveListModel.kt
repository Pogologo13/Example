package com.app.skillcinema.data.room

import androidx.room.*
import com.app.skillcinema.data.retrofit.FilmItem


@Entity(tableName = "saved_list_item")
data class SavedList(
    @ColumnInfo(name = "list_item_name")
    val listItemName: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)

@Entity(tableName = "saved_item")
data class SavedItem(
    @ColumnInfo(name = "list_item")
    val listFilms: FilmItem,
    @ColumnInfo(name = "list_item_name")
    val listItemName: String,
    @ColumnInfo(name = "kinopoiskId")
    val kinopoiskId: Int,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)

data class SavedListWithItem(
    @Embedded
    val savedList: SavedList,
    @Relation(entity = SavedItem::class, parentColumn = "list_item_name", entityColumn = "list_item_name")
    val savedItem: List<SavedItem>?

)