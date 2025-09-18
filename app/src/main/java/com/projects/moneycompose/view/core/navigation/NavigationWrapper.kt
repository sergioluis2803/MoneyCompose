package com.projects.moneycompose.view.core.navigation

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.projects.moneycompose.R
import com.projects.moneycompose.view.BaseViewModel
import com.projects.moneycompose.view.closeMonth.CloseMonthScreen
import com.projects.moneycompose.view.core.components.TextCompose
import com.projects.moneycompose.view.core.util.listDrawerItem
import com.projects.moneycompose.view.exportReport.ExportReportScreen
import com.projects.moneycompose.view.history.HistoryScreen
import com.projects.moneycompose.view.home.HomeScreen
import com.projects.moneycompose.view.saving.SavingScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationWrapper() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val itemList = listDrawerItem()
    var selectedIndex by remember { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.onBackground,
                drawerContentColor = MaterialTheme.colorScheme.secondary,
            ) {
                Spacer(Modifier.height(14.dp))
                TextCompose(
                    text = stringResource(R.string.title_drawer),
                    modifier = Modifier.padding(start = 18.dp),
                )
                Spacer(Modifier.height(20.dp))

                itemList.forEachIndexed { index, item ->
                    NavigationDrawerItem(
                        label = { TextCompose(text = item.name) },
                        onClick = {
                            selectedIndex = index
                            scope.launch { drawerState.close() }
                                        when (index) {
                                            0 -> navController.navigate(Home)
                                            1 -> navController.navigate(CloseMonth)
                                            2 -> navController.navigate(Saving)
                                            3 -> navController.navigate(ExportReport)
                                            4 -> navController.navigate(History)

                                        }
                        },
                        icon = { Icon(imageVector = item.icon, contentDescription = "icon") },
                        selected = selectedIndex == index,
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
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        TextCompose(
                            text = stringResource(R.string.title_top_bar_home),
                            modifier = Modifier.fillMaxWidth(),
                            fontSize = 18.sp
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "menu"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.onBackground,
                        titleContentColor = MaterialTheme.colorScheme.secondary,
                        navigationIconContentColor = MaterialTheme.colorScheme.secondary,
                    )
                )
            },
            floatingActionButton = {
                if (navBackStackEntry?.destination?.hasRoute<Home>() == true) {
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

            NavHost(navController = navController, startDestination = Home) {
                composable<Home> { backStackEntry ->
                    val baseViewModel: BaseViewModel = hiltViewModel(backStackEntry)

                    HomeScreen(
                        baseViewModel = baseViewModel,
                        innerPadding,
                        showDialog,
                        onDismissDialog = { showDialog = false }
                    )
                }
                composable<Saving> {
                    SavingScreen(
                        innerPadding
                    )
                }
                composable<CloseMonth> { backStackEntry ->
                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry(Home)
                    }
                    val baseViewModel: BaseViewModel = hiltViewModel(parentEntry)
                    CloseMonthScreen(
                        closeMonthViewModel = baseViewModel,
                        onNavigateBack = { navController.navigate(Home) },
                        innerPadding
                    )
                }
                composable<History> {
                    HistoryScreen(
                        innerPadding1 = innerPadding
                    )
                }
                composable<ExportReport> {
                    ExportReportScreen(
                        innerPadding1 = innerPadding
                    )
                }
            }
        }
    }

}