package com.projects.moneycompose.view.history

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.projects.moneycompose.R
import com.projects.moneycompose.domain.entity.MonthEntity
import com.projects.moneycompose.view.core.components.TextCompose
import com.projects.moneycompose.view.BaseViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    historyViewModel: BaseViewModel,
    innerPadding: PaddingValues
) {
    val uiState by historyViewModel.uiHistoryState.collectAsStateWithLifecycle()

    if (uiState.history.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            TextCompose(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.title_screen_history),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Light,
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            items(uiState.history) { historySpent ->
                ItemHistory(historySpent)
            }
        }
    }
}

@Composable
fun ItemHistory(itemMonth: MonthEntity) {
    val numberFormat = remember {
        NumberFormat.getNumberInstance(Locale.US).apply {
            minimumFractionDigits = 2
            maximumFractionDigits = 2
        }
    }

    val formattedValue = remember(itemMonth.spent) {
        itemMonth.spent.toDoubleOrNull()?.let { numberFormat.format(it) } ?: ""
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            TextCompose(text = itemMonth.month)
            TextCompose(text = itemMonth.year)
        }
        TextCompose(text = "S/. $formattedValue")
    }
}