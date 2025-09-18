package com.projects.moneycompose.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MonthEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo("year")
    val year: String,
    @ColumnInfo("month")
    val month: String,
    @ColumnInfo("spent")
    val spent: String,
    @ColumnInfo("saving")
    val saving: String
)
