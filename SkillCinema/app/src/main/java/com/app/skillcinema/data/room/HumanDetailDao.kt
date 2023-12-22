package com.app.skillcinema.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.skillcinema.data.retrofit.HumanDetailDto
import kotlinx.coroutines.flow.Flow

@Dao
interface HumanDetailDao {

    @Query("SELECT * FROM human_detail ")
    fun getAllHumanDetail(): Flow<List<HumanDetailDto>>

    @Query("SELECT * FROM human_detail WHERE personId = :personId ")
    fun getSingleHumanDetail(personId: Int): Flow<HumanDetailDto>
//
//        @Query("SELECT * FROM film WHERE nameRu LIKE '%' || :searchQuery || '%'")
//        fun searchInLocalStore(searchQuery: String): Flow<List<HumanItem>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertHumanDetail(humanDetailDto: HumanDetailDto)
}