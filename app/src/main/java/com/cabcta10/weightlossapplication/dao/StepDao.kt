package com.cabcta10.weightlossapplication.dao

import android.database.sqlite.SQLiteConstraintException
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.cabcta10.weightlossapplication.entity.StepData
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface StepDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(stepData: StepData)

    @Query("SELECT * FROM stepdata ORDER BY date DESC")
    fun getAllSteps(): Flow<List<StepData>>

    @Query("DELETE FROM stepdata")
    suspend fun deleteAll()

    // Update query that matches on the primary key
    @Query("UPDATE stepdata SET steps = :steps WHERE date = :date")
    suspend fun updateStepsForDate(date: String, steps: Int)



    @Query("UPDATE stepdata SET informed = :informed WHERE date = :date")
    suspend fun updateInformedForDate(date: String, informed: Boolean)

    @Query("SELECT * from stepdata where date = :date limit 1")
    fun getByDate(date : String) : Flow<StepData>
}