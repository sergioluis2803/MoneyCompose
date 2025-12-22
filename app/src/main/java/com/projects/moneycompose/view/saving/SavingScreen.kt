package com.projects.moneycompose.view.saving

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.projects.moneycompose.R
import com.projects.moneycompose.data.data_source.DataStoreManager
import com.projects.moneycompose.core.components.ButtonApp
import com.projects.moneycompose.core.components.TextApp
import com.projects.moneycompose.core.components.TextFieldApp
import com.projects.moneycompose.ui.theme.MoneyComposeTheme
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavingScreen(
    innerPadding: PaddingValues
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var tfIn by remember { mutableStateOf("") }
    val dataStore = remember { DataStoreManager(context) }
    val saveInDataStore by dataStore.deposit.collectAsStateWithLifecycle(initialValue = "")

    val numberFormat = remember {
        NumberFormat.getNumberInstance(Locale.US).apply {
            minimumFractionDigits = 2
            maximumFractionDigits = 2
        }
    }

    val formattedValue = remember(saveInDataStore) {
        saveInDataStore.toDoubleOrNull()?.let { numberFormat.format(it) } ?: ""
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(horizontal = 14.dp)
    ) {
        TextApp(
            text = stringResource(R.string.title_screen_saving)
        )

        TextFieldApp(
            value = formattedValue,
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            readOnly = true
        )

        Spacer(Modifier.height(16.dp))

        TextApp(text = stringResource(R.string.title_top_bar_saving))

        TextFieldApp(
            value = tfIn,
            onValueChange = { tfIn = it },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(Modifier.weight(1f))

        ButtonApp(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                scope.launch {
                    var newSave: String
                    when {
                        saveInDataStore.isNotEmpty()-> {
                            val sum = tfIn.toDouble() + saveInDataStore.toDouble()
                            newSave = sum.toString()
                            dataStore.saveIn(newSave)
                        }
                        saveInDataStore.isEmpty()->{
                            newSave = tfIn
                            dataStore.saveIn(newSave)
                        }
                    }
                    tfIn = ""
                }
            },
            text = stringResource(R.string.button_screen_saving),
            isEnabledButton = tfIn.isNotEmpty()
        )
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SavingScreenPreview(){
    MoneyComposeTheme {
        SavingScreen(innerPadding = PaddingValues(12.dp))
    }
}
