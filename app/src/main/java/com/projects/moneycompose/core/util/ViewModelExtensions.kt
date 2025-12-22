package com.projects.moneycompose.core.util

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.NavKey
import com.projects.moneycompose.core.navigation.Screens
import com.projects.moneycompose.view.home.HomeViewModel

@Composable
fun navKeyViewModel(
    key: NavKey
): HomeViewModel {
    return hiltViewModel<HomeViewModel, HomeViewModel.Factory>(
        creationCallback = { factory ->
            factory.create(key as Screens)
        }
    )
}
