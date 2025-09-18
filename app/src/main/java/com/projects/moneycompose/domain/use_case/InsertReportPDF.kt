package com.projects.moneycompose.domain.use_case

import com.projects.moneycompose.domain.entity.ExportEntity
import com.projects.moneycompose.domain.repository.SpendRepository

class InsertReportPDF(private val repository: SpendRepository) {

    suspend operator fun invoke(exportEntity: ExportEntity) {
        repository.insertReportPDF(exportEntity)
    }
}