package com.projects.moneycompose.domain.use_case

import com.projects.moneycompose.domain.entity.MonthEntity
import com.projects.moneycompose.domain.repository.SpendRepository
import kotlinx.coroutines.flow.Flow

class GetMonthSaving(private val repository: SpendRepository) {

    operator fun invoke(): Flow<List<MonthEntity>>{
        return repository.getMonthSaving()
    }
}