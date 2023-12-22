package com.app.skillcinema.di

import android.content.Context
import androidx.room.Room
import com.app.skillcinema.data.room.*
import com.app.skillcinema.utils.*
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StorageService {

    @Provides
    @Singleton
    fun providesDataBase(@ApplicationContext context: Context, moshi: Moshi): FilmDatabase {
        CountriesConverter.initialize(moshi)
        GenreConverter.initialize(moshi)
        CollectionsName.initialize(moshi)
        EpisodeConverter.initialize(moshi)
        FilmItemConverter.initialize(moshi)
        FilmConverter.initialize(moshi)
        SerialDtoConverter.initialize(moshi)
        SpouseConverter.initialize(moshi)
        ListConverter.initialize(moshi)
        HumanFIlmConverter.initialize(moshi)
        return Room.databaseBuilder(
            context = context,
            klass = FilmDatabase::class.java,
            name = "FilmDB"
        ).build()
    }
    @Provides
    @Singleton
    fun providesFilmDao(filmDatabase: FilmDatabase): FilmDao =
        filmDatabase.getFilmCollection()

    @Provides
    @Singleton
    fun providesHumanDao(filmDatabase: FilmDatabase): HumanDao =
        filmDatabase.getHumanDao()

    @Provides
    @Singleton
    fun providesSerialDao(filmDatabase: FilmDatabase): SerialDao =
        filmDatabase.getSerialDao()

    @Provides
    @Singleton
    fun providesSavedListDao(filmDatabase: FilmDatabase): SavedListDao =
        filmDatabase.getSavedListDao()

    @Provides
    @Singleton
    fun providesSavedItemDao(filmDatabase: FilmDatabase): SavedItemDao =
        filmDatabase.getSavedItemDao()

    @Provides
    @Singleton
    fun providesHumanDetailDao(filmDatabase: FilmDatabase): HumanDetailDao =
        filmDatabase.getHumanDetailDao()

    @Provides
    @Singleton
    fun providesPhotoDao(filmDatabase: FilmDatabase): PhotoDao =
        filmDatabase.getPhotoDao()

    @Provides
    @Singleton
    fun providesApp(@ApplicationContext appContext: Context) = appContext

    @Singleton
    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder().build()
    }
}