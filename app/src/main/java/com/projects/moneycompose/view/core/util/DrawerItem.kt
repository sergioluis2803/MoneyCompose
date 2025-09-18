package com.projects.moneycompose.view.core.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.projects.moneycompose.R

data class DrawerItem(
    val icon: ImageVector,
    val name: String
)

@Composable
fun listDrawerItem(): List<DrawerItem>{
    return listOf(
        DrawerItem(
            name = stringResource(R.string.title_top_bar_screen_home),
            icon = Icons.Default.Home
        ),
        DrawerItem(
            name = stringResource(R.string.title_top_bar_screen_month),
            icon = Icons.Default.DateRange
        ),
        DrawerItem(
            name = stringResource(R.string.title_top_bar_saving),
            icon = Icons.Default.Add
        ),
        DrawerItem(
            name = stringResource(R.string.title_top_bar_export_report),
            icon = Icons.Default.MailOutline
        ),
        DrawerItem(
            name = stringResource(R.string.title_top_bar_history),
            icon = Icons.Default.Refresh
        )
    )
}