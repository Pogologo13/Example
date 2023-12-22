package com.app.skillcinema.data.room

import androidx.paging.PagingSource
import androidx.room.*
import com.app.skillcinema.data.retrofit.FilmItem
import kotlinx.coroutines.flow.Flow

@Dao
interface FilmDao {

    @Query("SELECT * FROM film")
    fun getAllFilms(): Flow<List<FilmItem>>

    @Query("SELECT * FROM film WHERE collectionName LIKE '%' || :collectionName || '%' LIMIT :limit ")
    fun getPagingData(collectionName: String, limit: Int): PagingSource<Int, FilmItem>

    @Query("SELECT * FROM film WHERE collectionName LIKE '%' || :collectionName || '%'")
    fun getAllFilmsCollection(collectionName: String): Flow<List<FilmItem>>

    @Query("SELECT * FROM film WHERE premiere LIKE '%' || :premiereDate || '%' ORDER BY premiereRu ASC ")
    fun getPremiere(premiereDate: String): Flow<List<FilmItem>>

    @Query("SELECT * FROM film WHERE kinopoiskId = :kinopoiskId ")
    fun getSingleFilm(kinopoiskId: Int): Flow<FilmItem>

    @Query("SELECT * FROM film WHERE viewed IS :beenViewed ")
    fun searchInLocalStore(beenViewed: Boolean = true): Flow<List<FilmItem>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSingle(film: FilmItem)

    @Update
    suspend fun updateSingle(film: FilmItem)
}