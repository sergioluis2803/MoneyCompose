package com.projects.moneycompose.view.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projects.moneycompose.core.navigation.Screens
import com.projects.moneycompose.domain.entity.MonthEntity
import com.projects.moneycompose.domain.use_case.MoneyUseCases
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@HiltViewModel(assistedFactory = HistoryViewModel.Factory::class)
class HistoryViewModel @AssistedInject constructor(
    @Assisted val navKey: Screens,
    private val moneyUseCases: MoneyUseCases
): ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(navKey: Screens): HistoryViewModel
    }

    private var getMonth: Job? = null

    init {
        getListMonth()
    }

    private val _uiHistoryState = MutableStateFlow(HistoryState())
    val uiHistoryState: StateFlow<HistoryState> = _uiHistoryState

    fun getListMonth() {
        getMonth?.cancel()
        getMonth = moneyUseCases.getMonthSaving()
            .onEach { month ->
                _uiHistoryState.value = uiHistoryState.value.copy(
                    history = month
                )
            }
            .launchIn(viewModelScope)
    }

}

data class HistoryState(
    val history: List<MonthEntity> = emptyList()
)
