package com.projects.moneycompose.domain.use_case

import com.projects.moneycompose.domain.entity.SpentEntity
import com.projects.moneycompose.domain.repository.SpendRepository

class DeleteSpend(private val repository: SpendRepository) {

    suspend operator fun invoke(spentEntity: SpentEntity){
        repository.deleteSpend(spentEntity)
    }
}