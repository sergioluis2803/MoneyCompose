package com.projects.moneycompose.domain.use_case

import com.projects.moneycompose.domain.entity.MonthEntity
import com.projects.moneycompose.domain.repository.SpendRepository

class InsertMonthSaving(private val repository: SpendRepository) {

    suspend operator fun invoke(monthEntity: MonthEntity){
        repository.insertMonthSaving(monthEntity)
    }
}