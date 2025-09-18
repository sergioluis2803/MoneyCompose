package com.projects.moneycompose.domain.use_case

import com.projects.moneycompose.domain.repository.SpendRepository

class DeleteAllSpend(private val repository: SpendRepository)  {

    suspend operator fun invoke(){
        repository.deleteAllSpend()
    }
}