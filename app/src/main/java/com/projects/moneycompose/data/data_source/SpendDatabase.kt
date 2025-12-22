package com.projects.moneycompose.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.projects.moneycompose.domain.entity.ExportEntity
import com.projects.moneycompose.domain.entity.MonthEntity
import com.projects.moneycompose.domain.entity.SpentEntity
import com.projects.moneycompose.core.util.Converters

@Database(
    entities = [SpentEntity::class, MonthEntity::class, ExportEntity::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class SpendDatabase : RoomDatabase() {

    abstract fun dao(): SpendDao

    companion object {
        const val DATABASE_NAME = "spend_db"
    }
}