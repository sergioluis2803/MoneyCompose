package com.projects.moneycompose.view.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projects.moneycompose.domain.entity.SpentEntity
import com.projects.moneycompose.domain.use_case.MoneyUseCases
import com.projects.moneycompose.core.navigation.Screens
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = HomeViewModel.Factory::class)
class HomeViewModel @AssistedInject constructor(
    @Assisted val navKey: Screens,
    private val moneyUseCases: MoneyUseCases
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(navKey: Screens): HomeViewModel
    }

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        viewModelScope.launch {
            moneyUseCases.getSpend()
                .collect { spends ->
                    _uiState.update {
                        it.copy(spends = spends)
                    }
                }
        }
    }

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            HomeUiEvent.OnAddClick -> {
                _uiState.update {
                    it.copy(dialogState = SpendDialogState(isVisible = true))
                }
            }

            is HomeUiEvent.OnEditClick -> {
                val spent = event.spent
                _uiState.update {
                    it.copy(
                        dialogState = SpendDialogState(
                            isVisible = true,
                            isEdit = true,
                            spentId = spent.id,
                            form = SpendFormState(
                                description = spent.description,
                                date = spent.date,
                                price = spent.money.toString()
                            )
                        )
                    )
                }
            }

            HomeUiEvent.OnDismissDialog -> {
                _uiState.update {
                    it.copy(dialogState = SpendDialogState())
                }
            }

            is HomeUiEvent.OnDescriptionChange -> {
                updateForm { copy(description = event.value) }
            }

            is HomeUiEvent.OnDateChange -> {
                updateForm { copy(date = event.value) }
            }

            is HomeUiEvent.OnPriceChange -> {
                updateForm { copy(price = event.value) }
            }

            HomeUiEvent.OnSaveSpent -> saveSpent()

            is HomeUiEvent.OnDeleteSpent -> {
                viewModelScope.launch {
                    moneyUseCases.deleteSpend(event.spent)
                }
            }

            HomeUiEvent.OnShowDateDialog -> {
                _uiState.update { it.copy(isDateDialogVisible = true) }
            }

            HomeUiEvent.OnDismissDateDialog -> {
                _uiState.update { it.copy(isDateDialogVisible = false) }
            }

            is HomeUiEvent.OnDateSelected -> {
                _uiState.update {
                    it.copy(
                        isDateDialogVisible = false,
                        dialogState = it.dialogState.copy(
                            form = it.dialogState.form.copy(
                                date = event.date
                            )
                        )
                    )
                }
            }

        }
    }

    private fun updateForm(update: SpendFormState.() -> SpendFormState) {
        _uiState.update {
            it.copy(
                dialogState = it.dialogState.copy(
                    form = it.dialogState.form.update()
                )
            )
        }
    }

    private fun saveSpent() {
        Log.d("PRUEBA", "2")
        val dialog = _uiState.value.dialogState
        val form = dialog.form

        if (form.description.isBlank() || form.date.isBlank() || form.price.isBlank()) return
        Log.d("PRUEBA", "3")

        viewModelScope.launch {
            val spent = SpentEntity(
                id = dialog.spentId ?: 0,
                description = form.description,
                date = form.date,
                money = form.price.toDouble()
            )

            if (dialog.isEdit) {
                moneyUseCases.updateSpend(spent)
                _uiState.update {
                    it.copy(
                        spends = it.spends.map { current ->
                            if (current.id == spent.id) spent else current
                        },
                        dialogState = SpendDialogState()
                    )
                }
            } else{
                moneyUseCases.insertSpend(spent)
                _uiState.update {
                    it.copy(
                        spends = it.spends + spent,
                        dialogState = SpendDialogState()
                    )
                }

            }

        }
    }
}
