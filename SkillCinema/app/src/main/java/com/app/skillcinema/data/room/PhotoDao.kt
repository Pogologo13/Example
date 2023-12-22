package com.app.skillcinema.data.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.skillcinema.data.retrofit.Photo
import kotlinx.coroutines.flow.Flow


@Dao
interface PhotoDao {

    @Query("SELECT * FROM photo WHERE kinopoiskId = :kinopoiskId ")
    fun getPhotoById(kinopoiskId: Int): Flow<List<Photo>>

    @Query("SELECT * FROM photo WHERE kinopoiskId = :kinopoiskId ")
    fun getPagingData(kinopoiskId: Int): PagingSource<Int, Photo>

    @Query("SELECT * FROM photo WHERE type = :type AND kinopoiskId = :kinopoiskId ")
    fun getPhotoByType(kinopoiskId: Int, type: String): Flow<List<Photo>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPhoto(photo: List<Photo>)
}