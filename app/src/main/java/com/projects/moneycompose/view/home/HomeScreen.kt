package com.projects.moneycompose.view.home

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.projects.moneycompose.R
import com.projects.moneycompose.domain.entity.SpentEntity
import com.projects.moneycompose.core.components.TextApp
import com.projects.moneycompose.core.dialog.DialogAddSpend
import com.projects.moneycompose.core.dialog.DialogDate
import com.projects.moneycompose.ui.theme.MoneyComposeTheme
import java.text.NumberFormat
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    innerPadding: PaddingValues,
    showDialog: Boolean,
    onDismissDialog: () -> Unit
) {
    val state by homeViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(showDialog) {
        if (showDialog) {
            homeViewModel.onEvent(HomeUiEvent.OnAddClick)
            onDismissDialog()
        }
    }

    HomeContent(
        state = state,
        innerPadding = innerPadding,
        onEvent = homeViewModel::onEvent
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeContent(
    state: HomeUiState,
    innerPadding: PaddingValues,
    onEvent: (HomeUiEvent) -> Unit
) {
    if (state.spends.isEmpty()) {
        ScreenEmpty(innerPadding)
    } else {
        LazyColumn(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
                .padding(innerPadding)
        ) {
            items(state.spends) { itemSpent ->
                ItemSpent(
                    itemSpent = itemSpent,
                    onEditSpent = { onEvent(HomeUiEvent.OnEditClick(itemSpent)) },
                    onDeleteSpent = {
                        onEvent(HomeUiEvent.OnDeleteSpent(itemSpent))
                    }
                )
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "")
                        Text(text = "")
                    }
                }
            }
        }
    }

    SpendDialog(
        state = state.dialogState,
        onDismiss = {
            onEvent(HomeUiEvent.OnDismissDialog)
        },
        onDescriptionChange = {
            onEvent(HomeUiEvent.OnDescriptionChange(it))
        },
        onDateChange = {
            onEvent(HomeUiEvent.OnDateChange(it))
        },
        onPriceChange = {
            onEvent(HomeUiEvent.OnPriceChange(it))
        },
        onSave = {
            Log.d("PRUEBA", "1")
            onEvent(HomeUiEvent.OnSaveSpent)
        },
        onEvent = onEvent
    )

    DialogDate(
        showDateDialog = state.isDateDialogVisible,
        onDismissDialog = {
            onEvent(HomeUiEvent.OnDismissDateDialog)
        },
        onSelectedDate = { date ->
            onEvent(HomeUiEvent.OnDateSelected(date))
        }
    )
}

@Composable
fun SpendDialog(
    state: SpendDialogState,
    onDismiss: () -> Unit,
    onDescriptionChange: (String) -> Unit,
    onDateChange: (String) -> Unit,
    onPriceChange: (String) -> Unit,
    onSave: () -> Unit,
    onEvent: (HomeUiEvent) -> Unit
) {
    if (!state.isVisible) return

    DialogAddSpend(
        onDismissDialog = onDismiss,
        descriptionSpent = state.form.description,
        dateSpent = state.form.date,
        priceSpent = state.form.price,
        onChangeDescription = onDescriptionChange,
        onChangeDate = onDateChange,
        onChangePrice = onPriceChange,
        onSaveSpent = onSave,
        onShowCalendar = { onEvent(HomeUiEvent.OnShowDateDialog) }
    )
}

@Composable
private fun ScreenEmpty(innerPadding: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        contentAlignment = Alignment.Center
    ) {
        TextApp(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.title_screen_home_empty_spent),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Light
        )
    }
}

@Composable
fun ItemSpent(
    itemSpent: SpentEntity,
    onEditSpent: () -> Unit,
    onDeleteSpent: () -> Unit
) {
    val numberFormat = remember {
        NumberFormat.getNumberInstance(Locale.US).apply {
            minimumFractionDigits = 2
            maximumFractionDigits = 2
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            TextApp(text = itemSpent.description)
            TextApp(text = itemSpent.date)
        }
        TextApp(
            text = "S/.${numberFormat.format(itemSpent.money)}",
            modifier = Modifier
                .weight(1f)
                .padding(end = 16.dp),
            textAlign = TextAlign.End
        )

        IconButton(
            onClick = { onEditSpent() },
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "edit_spent",
                tint = MaterialTheme.colorScheme.secondary
            )
        }
        Spacer(Modifier.width(12.dp))
        IconButton(
            onClick = { onDeleteSpent() },
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "delete_spent",
                tint = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeContentPreview() {
    MoneyComposeTheme {
        HomeContent(
            state = HomeUiState(
                spends = listOf(
                    SpentEntity(id = 1, description = "Comida", date = "12/10/2024", money = 25.50),
                    SpentEntity(
                        id = 2,
                        description = "Transporte",
                        date = "13/10/2024",
                        money = 10.00
                    )
                )
            ),
            innerPadding = PaddingValues(0.dp),
            onEvent = {}
        )
    }
}
