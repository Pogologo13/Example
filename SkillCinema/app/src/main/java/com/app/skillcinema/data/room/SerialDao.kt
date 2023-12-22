package com.app.skillcinema.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.skillcinema.data.retrofit.SerialDto
import kotlinx.coroutines.flow.Flow

@Dao
interface SerialDao {

    @Query("SELECT * FROM serialList ")
    fun getAllSerials(): Flow<SerialDto>

    @Query("SELECT * FROM serialList WHERE kinopoiskId = :kinopoiskId ")
    fun getSingleSerial(kinopoiskId: Int): Flow<SerialDto?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSerial(film: SerialDto)
}