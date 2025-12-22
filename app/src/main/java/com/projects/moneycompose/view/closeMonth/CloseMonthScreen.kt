package com.projects.moneycompose.view.closeMonth

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.projects.moneycompose.R
import com.projects.moneycompose.domain.entity.ExportEntity
import com.projects.moneycompose.domain.entity.ItemReport
import com.projects.moneycompose.domain.entity.MonthEntity
import com.projects.moneycompose.core.components.ButtonApp
import com.projects.moneycompose.core.components.TextApp
import com.projects.moneycompose.data.data_source.DataStoreManager
import com.projects.moneycompose.core.util.MoneyEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CloseMonthScreen(
    closeMonthViewModel: CloseMonthViewModel,
    onNavigateBack: () -> Unit,
    innerPadding: PaddingValues
) {

    LaunchedEffect(Unit) {
        closeMonthViewModel.getSpends()
    }

    val listSpent by closeMonthViewModel.homeUiState.collectAsStateWithLifecycle()
    val currentDate = LocalDate.now()
    val year = currentDate.year
    val month = currentDate.month.getDisplayName(
        java.time.format.TextStyle.FULL,
        Locale("es", "ES")
    )
    val formattedMonth = month.replaceFirstChar { it.uppercase() }

    val context = LocalContext.current
    val dataStore = remember { DataStoreManager(context) }

    val savedIngreso by dataStore.deposit.collectAsStateWithLifecycle(initialValue = "")
    val scope = rememberCoroutineScope()

    var result = 0.0
    var isActiveCloseMonth = false
    var total = ""
    if (listSpent.spends.isEmpty()) {
        total = "Sin gastos registrados "
    } else {
        result = listSpent.spends.sumOf { it.money }
        total = "S/. $result"
        isActiveCloseMonth = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(horizontal = 14.dp)
    ) {
        TextApp(
            text = stringResource(R.string.title_screen_month),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )

        Spacer(Modifier.height(8.dp))

        TextApp(
            text = stringResource(R.string.body_screen_month)
        )

        Spacer(Modifier.height(16.dp))

        TextApp(
            text = stringResource(R.string.footer_screen_month),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        Spacer(Modifier.height(12.dp))

        TextApp(text = total)

        Spacer(Modifier.weight(1f))

        ButtonApp(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                if (savedIngreso.isEmpty()) {
                    Toast.makeText(context, "No hay un ingreso registrado", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    scope.launch {
                        val saving = savedIngreso.toDouble() - result
                        val monthEntity = MonthEntity(
                            year = year.toString(),
                            month = formattedMonth,
                            spent = result.toString(),
                            saving = saving.toString()
                        )

                        dataStore.saveIn(saving.toString())

                        val list: MutableList<ItemReport> = mutableListOf()
                        listSpent.spends.forEach { item ->
                            list.add(
                                ItemReport(
                                    description = item.description,
                                    money = item.money.toString(),
                                    data = item.date
                                )
                            )
                        }

                        val exportEntity = ExportEntity(
                            yearReport = year.toString(),
                            monthReport = formattedMonth,
                            listSpent = list
                        )

                        closeMonthViewModel.onEvent(MoneyEvent.AddReportPDF(exportEntity))
                        closeMonthViewModel.onEvent(MoneyEvent.DeleteAllSpend)
                        closeMonthViewModel.onEvent(MoneyEvent.AddMonth(monthEntity))

                        delay(1500)
                        onNavigateBack()
                    }
                }
            },
            text = stringResource(R.string.title_top_bar_screen_month),
            isEnabledButton = isActiveCloseMonth
        )
    }

}