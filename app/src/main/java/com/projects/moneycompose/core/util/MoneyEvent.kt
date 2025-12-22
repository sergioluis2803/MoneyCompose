package com.projects.moneycompose.core.util

import com.projects.moneycompose.domain.entity.ExportEntity
import com.projects.moneycompose.domain.entity.MonthEntity
import com.projects.moneycompose.domain.entity.SpentEntity

sealed class MoneyEvent {
    data class AddSpent(val spentEntity: SpentEntity): MoneyEvent()
    data class DeleteSpent(val spentEntity: SpentEntity): MoneyEvent()
    data class EditSpent(val spentEntity: SpentEntity): MoneyEvent()
    data class AddMonth(val monthEntity: MonthEntity): MoneyEvent()
    data object DeleteAllSpend: MoneyEvent()
    data class AddReportPDF(val exportEntity: ExportEntity): MoneyEvent()
}