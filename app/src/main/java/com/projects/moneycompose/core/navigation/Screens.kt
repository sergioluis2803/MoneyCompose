package com.projects.moneycompose.core.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed class Screens() : NavKey {
    @Serializable
    data object Home : Screens()

    @Serializable
    object Saving : Screens()

    @Serializable
    object CloseMonth : Screens()

    @Serializable
    object History : Screens()

    @Serializable
    object ExportReport : Screens()
}