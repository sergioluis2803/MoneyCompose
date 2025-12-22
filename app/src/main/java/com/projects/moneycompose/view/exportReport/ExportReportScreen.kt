package com.projects.moneycompose.view.exportReport

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.projects.moneycompose.R
import com.projects.moneycompose.core.components.ButtonApp
import com.projects.moneycompose.core.components.TextApp
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExportReportScreen(
    exportViewModel: ExportReportViewModel,
    innerPadding: PaddingValues
) {
    val context = LocalContext.current
    var selectedMonth by remember { mutableStateOf("Julio") }
    var selectedYear by remember { mutableStateOf("2025") }

    Column(
        modifier = Modifier
            .padding(innerPadding)
            .padding(horizontal = 16.dp)
    ) {
        TextApp(
            text = stringResource(R.string.title_screen_export_report),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )

        Spacer(Modifier.height(12.dp))
        VerticalWheelPickers(
            selectedMonth = selectedMonth,
            selectedYear = selectedYear,
            onMonthSelected = { selectedMonth = it },
            onYearSelected = { selectedYear = it }
        )
        Spacer(Modifier.height(12.dp))

        TextApp(
            text = stringResource(R.string.body_screen_export_report),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )

        Spacer(Modifier.height(8.dp))
        ItemReport()

        Spacer(Modifier.weight(1f))

        ButtonApp(
            modifier = Modifier.fillMaxWidth(),
            onClick = { exportViewModel.generatePDF(context, selectedMonth, selectedYear) },
            text = stringResource(R.string.title_top_bar_export_report),
            isEnabledButton = true
        )

    }
}

@Composable
fun ItemReport() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            TextApp(text = stringResource(R.string.footer_screen_export_report))
            TextApp(text = stringResource(R.string.item_screen_export_report))
        }
        TextApp(text = stringResource(R.string.item_screen_export_report))
    }
}

@Composable
fun VerticalWheelPickers(
    selectedMonth: String,
    selectedYear: String,
    onMonthSelected: (String) -> Unit,
    onYearSelected: (String) -> Unit
) {
    val months = listOf(
        "Enero","Febrero","Marzo","Abril","Mayo","Junio",
        "Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"
    )
    val years = listOf("2025","2026","2027","2028","2029","2030")

    val startMonthIndex = months.indexOf(selectedMonth).coerceAtLeast(0)
    val startYearIndex = years.indexOf(selectedYear).coerceAtLeast(0)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        WheelPicker(
            items = months,
            startIndex = startMonthIndex,
            onItemSelected = onMonthSelected
        )

        WheelPicker(
            items = years,
            startIndex = startYearIndex,
            onItemSelected = onYearSelected
        )
    }
}

@Composable
fun WheelPicker(
    items: List<String>,
    startIndex: Int = 0,
    onItemSelected: (String) -> Unit
) {
    val repetitions = 500
    val totalItems = items.size * repetitions
    val initialIndex = (repetitions / 2) * items.size + startIndex.coerceIn(0, items.lastIndex)

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = initialIndex)
    val coroutineScope = rememberCoroutineScope()

    val centerGlobalIndex = remember { mutableIntStateOf(initialIndex) }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .width(120.dp)
            .height(150.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(vertical = 48.dp)
    ) {
        items(totalItems) { index ->
            val actualIndex = index % items.size
            val item = items[actualIndex]
            val isSelected = index == centerGlobalIndex.intValue

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp)
                    .clickable {
                        coroutineScope.launch {
                            listState.animateScrollToItem(index)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                TextApp(
                    text = item,
                    fontSize = if (isSelected) 20.sp else 16.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo }
            .map { visibleItems ->
                if (visibleItems.isEmpty()) null
                else {
                    val viewportStart = listState.layoutInfo.viewportStartOffset
                    val viewportEnd = listState.layoutInfo.viewportEndOffset
                    val viewportCenter = (viewportStart + viewportEnd) / 2
                    visibleItems.minByOrNull { info ->
                        val itemCenter = info.offset + info.size / 2
                        kotlin.math.abs(itemCenter - viewportCenter)
                    }?.index
                }
            }
            .distinctUntilChanged()
            .collect { centerIdx ->
                centerIdx?.let { idx ->
                    centerGlobalIndex.intValue = idx
                    onItemSelected(items[idx % items.size])
                }
            }
    }
}
