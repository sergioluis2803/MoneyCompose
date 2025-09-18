package com.projects.moneycompose.data.repository

import com.projects.moneycompose.data.data_source.SpendDao
import com.projects.moneycompose.domain.entity.ExportEntity
import com.projects.moneycompose.domain.entity.MonthEntity
import com.projects.moneycompose.domain.entity.SpentEntity
import com.projects.moneycompose.domain.repository.SpendRepository
import kotlinx.coroutines.flow.Flow

class SpendRepositoryImpl(private val dao: SpendDao): SpendRepository {
    override fun getSpend(): Flow<List<SpentEntity>> {
        return dao.getSpend()
    }

    override suspend fun insertSpend(spentEntity: SpentEntity) {
        dao.insertSpend(spentEntity)
    }

    override suspend fun deleteSpend(spentEntity: SpentEntity) {
        return dao.deleteSpend(spentEntity)
    }

    override suspend fun updateSpend(spentEntity: SpentEntity) {
        return dao.updateSpend(spentEntity)
    }

    override suspend fun deleteAllSpend() {
        return dao.deleteAllSpend()
    }

    override fun getMonthSaving(): Flow<List<MonthEntity>> {
        return dao.getMonthSaving()
    }

    override suspend fun insertMonthSaving(monthEntity: MonthEntity) {
        dao.insertMonthSaving(monthEntity)
    }

    override suspend fun getReportPDF(month: String, year: String): ExportEntity {
        return dao.getReportPDF(month, year)
    }

    override suspend fun insertReportPDF(exportEntity: ExportEntity) {
        dao.insertReportPDF(exportEntity)
    }
}