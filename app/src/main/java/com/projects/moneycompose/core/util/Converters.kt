package com.projects.moneycompose.core.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.projects.moneycompose.domain.entity.ItemReport

class Converters {
    @TypeConverter
    fun fromItemReportList(value: List<ItemReport>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toItemReportList(value: String): List<ItemReport> {
        val listType = object : TypeToken<List<ItemReport>>() {}.type
        return Gson().fromJson(value, listType)
    }
}