package com.projects.moneycompose.view.core.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder

fun NavController.currentRoute(): String? =
    this.currentBackStackEntry?.destination?.route

fun NavController.navigateSafe(
    route: String,
    builder: NavOptionsBuilder.() -> Unit = {}
) {
    val current = currentRoute()
    if (current != route) {
        this.navigate(route) {
            launchSingleTop = true
            builder()
        }
    }
}