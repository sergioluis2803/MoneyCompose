package com.projects.moneycompose.view.core.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.projects.moneycompose.R
import com.projects.moneycompose.view.core.components.ButtonCompose
import com.projects.moneycompose.view.core.components.TextCompose
import com.projects.moneycompose.view.core.components.TextFieldCompose

@Composable
fun DialogAddSpend(
    showDialog: Boolean,
    onDismissDialog: () -> Unit,
    onSaveSpent: () -> Unit,
    descriptionSpent: String,
    onChangeDescription: (String) -> Unit,
    dateSpent: String,
    onChangeDate: (String) -> Unit,
    priceSpent: String,
    onChangePrice: (String) -> Unit,
    onShowCalendar: () -> Unit
) {
    if (showDialog) {
        Dialog(onDismissRequest = { onDismissDialog() }) {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.onBackground)
                    .padding(20.dp)
            ) {
                TextCompose(
                    text = stringResource(R.string.title_dialog_add_spent),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
                Spacer(Modifier.height(14.dp))
                TextFieldCompose(
                    value = descriptionSpent,
                    onValueChange = { onChangeDescription(it) },
                    label = {
                        TextCompose(
                            text = stringResource(R.string.label_dialog_add_spent_description)
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        autoCorrectEnabled = true
                    )
                )

                Spacer(Modifier.height(14.dp))

                TextFieldCompose(
                    value = dateSpent,
                    onValueChange = { onChangeDate(it) },
                    label = {
                        TextCompose(text = stringResource(R.string.label_dialog_add_spent_date))
                    },
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "icon_calendar",
                            modifier = Modifier.clickable {
                                onShowCalendar()
                            }
                        )
                    }
                )

                Spacer(Modifier.height(14.dp))

                TextFieldCompose(
                    value = priceSpent,
                    onValueChange = { onChangePrice(it) },
                    label = { TextCompose(text = stringResource(R.string.label_dialog_add_spent_price)) },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "icon_price"
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                ButtonCompose(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 14.dp),
                    onClick = { onSaveSpent() },
                    text = stringResource(R.string.button_dialog_add_spent_save),
                    isEnabledButton = descriptionSpent.isNotEmpty() && dateSpent.isNotEmpty() && priceSpent.isNotEmpty()
                )
            }

        }
    }
}