package com.projects.moneycompose.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SpentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int  = 0,
    @ColumnInfo("description")
    val description : String,
    @ColumnInfo("money")
    val money: Double,
    @ColumnInfo("date")
    val date: String
)