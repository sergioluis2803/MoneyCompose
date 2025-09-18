package com.projects.moneycompose.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.projects.moneycompose.domain.entity.ExportEntity
import com.projects.moneycompose.domain.entity.MonthEntity
import com.projects.moneycompose.domain.entity.SpentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SpendDao {
    @Query("SELECT * FROM spententity")
    fun getSpend(): Flow<List<SpentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpend(spendEntity: SpentEntity)

    @Delete
    suspend fun deleteSpend(spendEntity: SpentEntity)

    @Update
    suspend fun updateSpend(spendEntity: SpentEntity)

    @Query("DELETE FROM SpentEntity")
    suspend fun deleteAllSpend()

    @Query("SELECT * FROM monthentity")
    fun getMonthSaving(): Flow<List<MonthEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMonthSaving(monthEntity: MonthEntity)

    @Query("SELECT * FROM exportentity WHERE yearReport = :year AND monthReport = :month LIMIT 1")
    suspend fun getReportPDF(month: String, year: String): ExportEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReportPDF(exportEntity: ExportEntity)
}