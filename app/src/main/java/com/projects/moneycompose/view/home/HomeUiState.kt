package com.projects.moneycompose.view.home

import com.projects.moneycompose.domain.entity.SpentEntity

data class HomeUiState(
    val spends: List<SpentEntity> = emptyList(),
    val dialogState: SpendDialogState = SpendDialogState(),
    val isLoading: Boolean = false,
    val isDateDialogVisible: Boolean = false
)