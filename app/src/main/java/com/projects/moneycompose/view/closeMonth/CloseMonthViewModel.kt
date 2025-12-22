package com.projects.moneycompose.view.closeMonth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projects.moneycompose.core.navigation.Screens
import com.projects.moneycompose.core.util.MoneyEvent
import com.projects.moneycompose.domain.entity.SpentEntity
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
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = CloseMonthViewModel.Factory::class)
class CloseMonthViewModel @AssistedInject constructor(
    @Assisted val navKey: Screens,
    private val moneyUseCases: MoneyUseCases
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(navKey: Screens): CloseMonthViewModel
    }

    private val _homeUiState = MutableStateFlow(SpendState())
    val homeUiState: StateFlow<SpendState> = _homeUiState
    private var getSpend: Job? = null

    init {
        getSpends()
    }

    fun onEvent(event: MoneyEvent) {
        when (event) {
            is MoneyEvent.AddMonth -> {
                viewModelScope.launch {
                    moneyUseCases.insertMonthSaving(event.monthEntity)
                }
            }

            is MoneyEvent.DeleteAllSpend -> {
                viewModelScope.launch {
                    moneyUseCases.deleteAllSpend()
                }
            }

            is MoneyEvent.AddReportPDF -> {
                viewModelScope.launch {
                    moneyUseCases.insertReportPDF(event.exportEntity)
                }
            }

            else -> {}
        }
    }

    fun getSpends() {
        getSpend?.cancel()
        getSpend = moneyUseCases.getSpend()
            .onEach { spend ->
                _homeUiState.value = homeUiState.value.copy(
                    spends = spend
                )
            }
            .launchIn(viewModelScope)
    }

    fun closeMonth(){

    }

}

data class SpendState(
    val spends: List<SpentEntity> = emptyList()
)
