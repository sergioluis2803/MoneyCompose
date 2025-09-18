package com.projects.moneycompose.domain.use_case

import com.projects.moneycompose.domain.entity.SpentEntity
import com.projects.moneycompose.domain.repository.SpendRepository
import kotlinx.coroutines.flow.Flow

class GetSpend(private val repository: SpendRepository) {

    operator fun invoke(): Flow<List<SpentEntity>> = repository.getSpend()
}