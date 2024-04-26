package com.cabcta10.weightlossapplication.repository

import com.cabcta10.weightlossapplication.entity.StepData
import kotlinx.coroutines.flow.Flow

interface StepRepository {
    fun getAllSteps(): Flow<List<StepData>>

    suspend fun insert(stepData: StepData)

    suspend fun update(date : String, steps : Int)

    suspend fun updateInformed(date : String, informed : Boolean)

    suspend fun deleteAll()

    fun getbyDate(date : String) : Flow<StepData>
}