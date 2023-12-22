package com.app.skillcinema.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.app.skillcinema.data.retrofit.*
import com.app.skillcinema.utils.*

@Database(
    entities = [FilmItem::class, HumanItem::class, SerialDto::class, SavedList::class, SavedItem::class,HumanDetailDto::class, Photo::class],
    version = 1
)
@TypeConverters(
    CountriesConverter::class,
    GenreConverter::class,
    CollectionsName::class,
    EpisodeConverter::class,
    FilmItemConverter::class,
    FilmConverter::class,
    SerialDtoConverter::class,
    SpouseConverter::class,
    HumanFIlmConverter::class,
    ListConverter::class
)
abstract class FilmDatabase: RoomDatabase() {
    abstract fun getFilmCollection() : FilmDao

    abstract fun getHumanDao(): HumanDao

    abstract fun getSerialDao(): SerialDao

    abstract fun getSavedListDao(): SavedListDao

    abstract fun getSavedItemDao():SavedItemDao

    abstract fun getHumanDetailDao():HumanDetailDao

    abstract fun getPhotoDao():PhotoDao
}