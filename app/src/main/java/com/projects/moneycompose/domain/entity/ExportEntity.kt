package com.projects.moneycompose.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ExportEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo("yearReport")
    val yearReport: String,
    @ColumnInfo("monthReport")
    val monthReport: String,
    @ColumnInfo("listSpent")
    val listSpent: List<ItemReport>
)

data class ItemReport(
    val description: String,
    val money: String,
    val data: String
)