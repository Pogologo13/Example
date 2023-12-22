package com.app.skillcinema.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.skillcinema.data.retrofit.HumanItem
import kotlinx.coroutines.flow.Flow

@Dao
interface HumanDao {

    @Query("SELECT * FROM human ")
    fun getAllHuman(): Flow<List<HumanItem>>

    @Query("SELECT * FROM human WHERE idHumanItem = :kinopoiskId ")
    fun getSingleFilm(kinopoiskId: Int): Flow<List<HumanItem?>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertHuman(film: HumanItem)
}