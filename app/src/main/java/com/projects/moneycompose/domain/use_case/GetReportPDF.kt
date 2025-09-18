package com.projects.moneycompose.domain.use_case

import com.projects.moneycompose.domain.entity.ExportEntity
import com.projects.moneycompose.domain.repository.SpendRepository

class GetReportPDF(private val repository: SpendRepository) {

    suspend operator fun invoke(month: String, year: String): ExportEntity = repository.getReportPDF(month, year)

}