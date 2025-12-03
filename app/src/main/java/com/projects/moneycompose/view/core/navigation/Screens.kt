package com.projects.moneycompose.view.core.navigation

sealed class Screens(val route: String) {
    object Home: Screens("home")
    object Saving: Screens("saving")
    object CloseMonth: Screens("close_month")
    object History: Screens("history")
    object ExportReport: Screens("export_route")
}