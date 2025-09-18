package com.projects.moneycompose.domain.repository

import com.projects.moneycompose.domain.entity.ExportEntity
import com.projects.moneycompose.domain.entity.MonthEntity
import com.projects.moneycompose.domain.entity.SpentEntity
import kotlinx.coroutines.flow.Flow

interface SpendRepository {
    fun getSpend(): Flow<List<SpentEntity>>

    suspend fun insertSpend(spentEntity: SpentEntity)

    suspend fun deleteSpend(spentEntity: SpentEntity)

    suspend fun updateSpend(spentEntity: SpentEntity)

    suspend fun deleteAllSpend()

    fun getMonthSaving(): Flow<List<MonthEntity>>

    suspend fun insertMonthSaving(monthEntity: MonthEntity)

    suspend fun getReportPDF(month: String, year: String): ExportEntity

    suspend fun insertReportPDF(exportEntity: ExportEntity)
}