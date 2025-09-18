package com.projects.moneycompose.view.core.components

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ButtonCompose(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        contentColor = MaterialTheme.colorScheme.onSurface,
        containerColor = MaterialTheme.colorScheme.primary,
        disabledContentColor = MaterialTheme.colorScheme.surface,
        disabledContainerColor = MaterialTheme.colorScheme.onError
    ),
    text: String,
    isEnabledButton: Boolean = false
) {

    val color =
        if (isEnabledButton) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.surface

    Button(
        modifier = modifier,
        onClick = { onClick() },
        colors = colors,
        enabled = isEnabledButton
    ) {
        TextCompose(
            text = text,
            color = color
        )
    }
}