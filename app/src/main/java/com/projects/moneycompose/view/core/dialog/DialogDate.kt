package com.projects.moneycompose.view.core.dialog

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.projects.moneycompose.R
import com.projects.moneycompose.view.core.components.TextCompose
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogDate(
    showDateDialog: Boolean,
    onDismissDialog: () -> Unit,
    onSelectedDate: (String) -> Unit
) {
    val calendar = Calendar.getInstance()
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = calendar.timeInMillis,
        initialDisplayedMonthMillis = calendar.timeInMillis
    )

    if (showDateDialog) {
        DatePickerDialog(
            onDismissRequest = { onDismissDialog() },
            confirmButton = {
                TextButton(
                    onClick = {
                        val millis = datePickerState.selectedDateMillis
                        if (millis != null) {
                            val selectedDate = Date(millis)
                            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            val formattedDate = formatter.format(selectedDate)
                            onSelectedDate(formattedDate)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    TextCompose(
                        text = stringResource(R.string.button_dialog_date_confirm)
                    )
                }
            },
            colors = DatePickerDefaults.colors(containerColor = MaterialTheme.colorScheme.onBackground)
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.onBackground,
                    titleContentColor = MaterialTheme.colorScheme.secondary,
                    headlineContentColor = MaterialTheme.colorScheme.secondary,
                    weekdayContentColor = MaterialTheme.colorScheme.secondary,
                    subheadContentColor = MaterialTheme.colorScheme.secondary,
                    navigationContentColor = MaterialTheme.colorScheme.secondary,
                    yearContentColor = MaterialTheme.colorScheme.secondary,
                    selectedYearContentColor = MaterialTheme.colorScheme.secondary,
                    selectedYearContainerColor = MaterialTheme.colorScheme.primary,
                    dayContentColor = MaterialTheme.colorScheme.secondary,
                    selectedDayContentColor = MaterialTheme.colorScheme.secondary,
                    selectedDayContainerColor = MaterialTheme.colorScheme.primary,
                    dividerColor = MaterialTheme.colorScheme.secondary
                )
            )
        }
    }
}