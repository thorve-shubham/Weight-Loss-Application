package com.cabcta10.weightlossapplication.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.cabcta10.weightlossapplication.entity.Settings
import kotlinx.coroutines.flow.Flow


@Dao
interface SettingsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Settings)

    @Update
    suspend fun update(item: Settings)

    @Delete
    suspend fun delete(item: Settings)

    @Query(value = "SELECT * from settings")
    fun getSettings(id: Int): Flow<Settings>

}