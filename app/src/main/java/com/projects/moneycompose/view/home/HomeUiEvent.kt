package com.projects.moneycompose.view.home

import com.projects.moneycompose.domain.entity.SpentEntity

sealed interface HomeUiEvent {

    data object OnAddClick : HomeUiEvent
    data object OnDismissDialog : HomeUiEvent

    data object OnShowDateDialog : HomeUiEvent
    data object OnDismissDateDialog : HomeUiEvent
    data class OnDateSelected(val date: String) : HomeUiEvent

    data class OnDescriptionChange(val value: String) : HomeUiEvent
    data class OnDateChange(val value: String) : HomeUiEvent
    data class OnPriceChange(val value: String) : HomeUiEvent

    data object OnSaveSpent : HomeUiEvent
    data class OnEditClick(val spent: SpentEntity) : HomeUiEvent
    data class OnDeleteSpent(val spent: SpentEntity) : HomeUiEvent
}
