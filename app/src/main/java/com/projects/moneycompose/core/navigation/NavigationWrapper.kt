package com.projects.moneycompose.core.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.projects.moneycompose.R
import com.projects.moneycompose.view.home.HomeViewModel
import com.projects.moneycompose.view.closeMonth.CloseMonthScreen
import com.projects.moneycompose.core.components.TextApp
import com.projects.moneycompose.core.util.DrawerItem
import com.projects.moneycompose.core.util.listDrawerItem
import com.projects.moneycompose.view.closeMonth.CloseMonthViewModel
import com.projects.moneycompose.view.exportReport.ExportReportScreen
import com.projects.moneycompose.view.exportReport.ExportReportViewModel
import com.projects.moneycompose.view.history.HistoryScreen
import com.projects.moneycompose.view.history.HistoryViewModel
import com.projects.moneycompose.view.home.HomeScreen
import com.projects.moneycompose.core.util.navKeyViewModel
import com.projects.moneycompose.ui.theme.MoneyComposeTheme
import com.projects.moneycompose.view.saving.SavingScreen
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun NavigationWrapper() {
    val navBackStack = rememberNavBackStack(Screens.Home)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val itemList = listDrawerItem()
    var selectedIndex by remember { mutableIntStateOf(0) }

    var showDialog by remember { mutableStateOf(false) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                items = itemList,
                selectedIndex = selectedIndex,
                onItemSelected = { index ->
                    selectedIndex = index
                    when (index) {
                        0 -> navBackStack.navigateTo(Screens.Home)
                        1 -> navBackStack.navigateTo(Screens.CloseMonth)
                        2 -> navBackStack.navigateTo(Screens.Saving)
                        3 -> navBackStack.navigateTo(Screens.ExportReport)
                        4 -> navBackStack.navigateTo(Screens.History)
                    }
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopBar(onMenuClick = {
                    scope.launch { drawerState.open() }
                })
            },
            floatingActionButton = {
                if (navBackStack.screenIsHome()) {
                    FloatingActionButton(
                        onClick = { showDialog = true },
                        contentColor = MaterialTheme.colorScheme.onSurface,
                        containerColor = MaterialTheme.colorScheme.primary
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "add")
                    }
                }
            }
        ) { innerPadding ->
            NavigationGraph(
                backStack = navBackStack,
                innerPadding = innerPadding,
                showDialog = showDialog,
                onDismissDialog = { showDialog = false }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun NavigationGraph(
    backStack: NavBackStack<NavKey>,
    innerPadding: PaddingValues,
    showDialog: Boolean,
    onDismissDialog: () -> Unit
) {
    NavDisplay(
        backStack = backStack,
        onBack = { backStack.back() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<Screens.Home> { key ->
                val homeViewModel: HomeViewModel = navKeyViewModel(key)

                HomeScreen(
                    homeViewModel = homeViewModel,
                    innerPadding = innerPadding,
                    showDialog = showDialog,
                    onDismissDialog = onDismissDialog
                )
            }

            entry<Screens.Saving> {
                SavingScreen(innerPadding)
            }

            entry<Screens.CloseMonth> { key ->
                val closeMonthViewModel = hiltViewModel<CloseMonthViewModel, CloseMonthViewModel.Factory>(
                    creationCallback = { factory ->
                        factory.create(key)
                    }
                )

                CloseMonthScreen(
                    closeMonthViewModel = closeMonthViewModel,
                    onNavigateBack = { backStack.navigateTo(Screens.Home) },
                    innerPadding = innerPadding
                )

            }

            entry<Screens.History> { key ->
                val historyViewModel = hiltViewModel<HistoryViewModel, HistoryViewModel.Factory>(
                    creationCallback = { factory ->
                        factory.create(key)
                    }
                )

                HistoryScreen(
                    historyViewModel = historyViewModel,
                    innerPadding = innerPadding
                )
            }

            entry<Screens.ExportReport> { key ->
                val exportReportViewModel = hiltViewModel<ExportReportViewModel, ExportReportViewModel.Factory>(
                    creationCallback = { factory ->
                        factory.create(key)
                    }
                )

                ExportReportScreen(
                    exportViewModel = exportReportViewModel,
                    innerPadding = innerPadding
                )
            }
        }
    )
}

@Composable
private fun DrawerContent(
    items: List<DrawerItem>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit
) {
    ModalDrawerSheet(
        drawerContainerColor = MaterialTheme.colorScheme.onBackground,
        drawerContentColor = MaterialTheme.colorScheme.secondary
    ) {
        Spacer(Modifier.height(14.dp))

        TextApp(
            text = stringResource(R.string.title_drawer),
            modifier = Modifier.padding(start = 18.dp)
        )

        Spacer(Modifier.height(20.dp))

        items.forEachIndexed { index, item ->
            NavigationDrawerItem(
                label = { TextApp(text = item.name) },
                icon = { Icon(item.icon, contentDescription = null) },
                selected = index == selectedIndex,
                onClick = { onItemSelected(index) },
                colors = NavigationDrawerItemDefaults.colors(
                    unselectedContainerColor = MaterialTheme.colorScheme.onBackground,
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    unselectedTextColor = MaterialTheme.colorScheme.secondary,
                    selectedTextColor = MaterialTheme.colorScheme.surface,
                    unselectedIconColor = MaterialTheme.colorScheme.secondary,
                    selectedIconColor = MaterialTheme.colorScheme.surface,
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(onMenuClick: () -> Unit) {
    TopAppBar(
        title = {
            TextApp(
                text = stringResource(R.string.title_top_bar_home),
                modifier = Modifier.fillMaxWidth(),
                fontSize = 18.sp
            )
        },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(Icons.Default.Menu, contentDescription = "menu")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.onBackground,
            titleContentColor = MaterialTheme.colorScheme.secondary,
            navigationIconContentColor = MaterialTheme.colorScheme.secondary
        )
    )
}

@RequiresApi(Build.VERSION_CODES.Q)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NavigationWrapperPreview(){
    MoneyComposeTheme {
        NavigationWrapper()
    }
}
