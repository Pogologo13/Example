package com.app.skillcinema.data.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedListDao {
    @Transaction
    @Query("SELECT * FROM saved_list_item ")
    fun getSavedList(): Flow<List<SavedListWithItem>?>

    @Query("SELECT * FROM saved_list_item WHERE list_item_name = :listName ")
    fun getSingleSavedList(listName: String): SavedList?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedList(savedList: SavedList)
    @Update
    suspend fun updateSavedList(savedList: SavedList)

    @Delete
    suspend fun deleteSavedList(savedList: SavedList)
}

@Dao
interface SavedItemDao {

    @Query("SELECT * FROM saved_item WHERE kinopoiskId = :kinopoiskId  AND list_item_name = :name")
    fun getSingleSavedItemById(kinopoiskId: Int, name: String): SavedItem?

    @Query("SELECT * FROM saved_item WHERE  list_item_name = :listName")
    fun getListSavedItemByName(listName: String): Flow<List<SavedItem>>?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSavedItem(savedItem: SavedItem)

    @Delete
    suspend fun deleteSavedItem(savedItem: SavedItem)
}