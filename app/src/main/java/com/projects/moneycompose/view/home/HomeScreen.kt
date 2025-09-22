package com.projects.moneycompose.view.home

import android.widget.Toast
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.projects.moneycompose.R
import com.projects.moneycompose.domain.entity.SpentEntity
import com.projects.moneycompose.view.BaseViewModel
import com.projects.moneycompose.view.core.components.TextCompose
import com.projects.moneycompose.view.core.dialog.DialogAddSpend
import com.projects.moneycompose.view.core.dialog.DialogDate
import com.projects.moneycompose.view.core.util.MoneyEvent
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    baseViewModel: BaseViewModel,
    innerPadding: PaddingValues,
    showDialog: Boolean,
    onDismissDialog: () -> Unit
) {
    val uiState by baseViewModel.homeUiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var showDialogCalendar by remember { mutableStateOf(false) }
    var descriptionSpent by remember { mutableStateOf("") }
    var dateSpent by remember { mutableStateOf("") }
    var priceSpent by remember { mutableStateOf("") }
    var descriptionUpdateSpent by remember { mutableStateOf("") }
    var dateUpdateSpent by remember { mutableStateOf("") }
    var priceUpdateSpent by remember { mutableStateOf("") }
    var isUpdateSpent by remember { mutableStateOf(false) }
    var idSpent by remember { mutableStateOf(0) }

    if (isUpdateSpent) {
        DialogAddSpend(
            showDialog = true,
            onDismissDialog = {
                isUpdateSpent = false
            },
            onSaveSpent = {
                if (descriptionUpdateSpent.isNotEmpty() && dateUpdateSpent.isNotEmpty() && priceUpdateSpent.isNotEmpty()) {
                    baseViewModel.onEvent(
                        MoneyEvent.EditSpent(
                            SpentEntity(
                                id = idSpent,
                                description = descriptionUpdateSpent,
                                date = dateUpdateSpent,
                                money = priceUpdateSpent.toDouble()
                            )
                        )
                    )
                }
                isUpdateSpent = false
            },
            descriptionSpent = descriptionUpdateSpent,
            onChangeDescription = {
                descriptionUpdateSpent = it
            },
            dateSpent = dateUpdateSpent,
            onChangeDate = { dateUpdateSpent = it },
            priceSpent = priceUpdateSpent,
            onChangePrice = { priceUpdateSpent = it },
            onShowCalendar = { showDialogCalendar = true }
        )
    }

    DialogAddSpend(
        showDialog = showDialog,
        onDismissDialog = {
            onDismissDialog()
            descriptionSpent = ""
            dateSpent = ""
            priceSpent = ""
        },
        onSaveSpent = {
            if (descriptionSpent.isNotEmpty() && dateSpent.isNotEmpty() && priceSpent.isNotEmpty()) {
                baseViewModel.onEvent(
                    MoneyEvent.AddSpent(
                        SpentEntity(
                            description = descriptionSpent,
                            date = dateSpent,
                            money = priceSpent.toDouble()
                        )
                    )
                )
            }
            descriptionSpent = ""
            dateSpent = ""
            priceSpent = ""
            onDismissDialog()
        },
        descriptionSpent = descriptionSpent,
        onChangeDescription = {
            descriptionSpent = it
        },
        dateSpent = dateSpent,
        onChangeDate = { dateSpent = it },
        priceSpent = priceSpent,
        onChangePrice = { priceSpent = it },
        onShowCalendar = { showDialogCalendar = true }
    )

    DialogDate(
        showDateDialog = showDialogCalendar,
        onDismissDialog = { showDialogCalendar = false },
        onSelectedDate = { date ->
            if (isUpdateSpent) dateUpdateSpent = date else dateSpent = date
            showDialogCalendar = false
        }
    )

    if (uiState.spends.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            TextCompose(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.title_screen_home_empty_spent),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Light
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp)
                .padding(innerPadding)
        ) {
            baseViewModel.sumTot(uiState.spends)
            items(uiState.spends) { itemSpent ->
                ItemSpent(
                    itemSpent = itemSpent,
                    onEditSpent = {
                        isUpdateSpent = true
                        idSpent = itemSpent.id
                        descriptionUpdateSpent = itemSpent.description
                        dateUpdateSpent = itemSpent.date
                        priceUpdateSpent = itemSpent.money.toString()
                    },
                    onDeleteSpent = {
                        baseViewModel.onEvent(
                            MoneyEvent.DeleteSpent(
                                SpentEntity(
                                    id = itemSpent.id,
                                    description = itemSpent.description,
                                    date = itemSpent.date,
                                    money = itemSpent.money
                                )
                            )
                        )
                        Toast.makeText(context, "GASTO ELIMINADO", Toast.LENGTH_SHORT)
                            .show()
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

    val formattedValue = remember(itemSpent.money) {
        itemSpent.money.let { numberFormat.format(it) } ?: ""
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            TextCompose(text = itemSpent.description)
            TextCompose(text = itemSpent.date)
        }
        TextCompose(
            text = "S/.$formattedValue",
            modifier = Modifier
                .weight(1f)
                .padding(end = 16.dp),
            textAlign = TextAlign.End
        )
        Row {
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
}