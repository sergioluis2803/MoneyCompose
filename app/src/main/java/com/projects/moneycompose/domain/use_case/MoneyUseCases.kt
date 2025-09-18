package com.projects.moneycompose.domain.use_case

data class MoneyUseCases(
    val getSpend: GetSpend,
    val insertSpend: InsertSpend,
    val updateSpend: UpdateSpend,
    val deleteSpend: DeleteSpend,
    val deleteAllSpend: DeleteAllSpend,
    val getMonthSaving: GetMonthSaving,
    val insertMonthSaving: InsertMonthSaving,
    val getReportPDF: GetReportPDF,
    val insertReportPDF: InsertReportPDF
)
