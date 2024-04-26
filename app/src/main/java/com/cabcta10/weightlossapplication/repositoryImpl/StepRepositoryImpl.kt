package com.cabcta10.weightlossapplication.repositoryImpl

import com.cabcta10.weightlossapplication.dao.StepDao
import com.cabcta10.weightlossapplication.entity.StepData
import com.cabcta10.weightlossapplication.repository.StepRepository
import kotlinx.coroutines.flow.Flow

class StepRepositoryImpl(private val stepDao: StepDao) : StepRepository {
    override fun getAllSteps(): Flow<List<StepData>> {
        return stepDao.getAllSteps()
    }

    override suspend fun insert(stepData: StepData) {
        stepDao.insert(stepData)
    }

    override suspend fun update(date: String, steps: Int) {
        stepDao.updateStepsForDate(date, steps)
    }

    override suspend fun updateInformed(date: String, informed: Boolean) {
        stepDao.updateInformedForDate(date, informed)
    }

    override suspend fun deleteAll() {
        stepDao.deleteAll()
    }

    override fun getbyDate(date: String) : Flow<StepData> {
        return stepDao.getByDate(date)
    }


}