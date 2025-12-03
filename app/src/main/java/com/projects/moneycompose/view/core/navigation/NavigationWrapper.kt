package com.projects.moneycompose.view.core.navigation

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.projects.moneycompose.R
import com.projects.moneycompose.view.BaseViewModel
import com.projects.moneycompose.view.closeMonth.CloseMonthScreen
import com.projects.moneycompose.view.core.components.TextCompose
import com.projects.moneycompose.view.core.util.DrawerItem
import com.projects.moneycompose.view.core.util.listDrawerItem
import com.projects.moneycompose.view.exportReport.ExportReportScreen
import com.projects.moneycompose.view.history.HistoryScreen
import com.projects.moneycompose.view.home.HomeScreen
import com.projects.moneycompose.view.saving.SavingScreen
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun NavigationWrapper() {

    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val itemList = listDrawerItem()
    var selectedIndex by remember { mutableIntStateOf(0) }

    var showDialog by remember { mutableStateOf(false) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                items = itemList,
                selectedIndex = selectedIndex,
                onItemSelected = { index ->
                    selectedIndex = index
                    scope.launch { drawerState.close() }

                    when (index) {
                        0 -> navController.navigateSafe(Screens.Home.route){
                            popUpTo(Screens.Home.route) { inclusive = false }
                        }
                        1 -> navController.navigateSafe(Screens.CloseMonth.route)
                        2 -> navController.navigateSafe(Screens.Saving.route)
                        3 -> navController.navigateSafe(Screens.ExportReport.route)
                        4 -> navController.navigateSafe(Screens.History.route)
                    }
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
                if (navBackStackEntry?.destination?.route == Screens.Home.route) {
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
                navController = navController,
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
    navController: NavHostController,
    innerPadding: PaddingValues,
    showDialog: Boolean,
    onDismissDialog: () -> Unit
) {
    NavHost(navController = navController, startDestination = Screens.Home.route) {

        composable(Screens.Home.route) { entry ->
            val baseViewModel: BaseViewModel = hiltViewModel(entry)
            HomeScreen(
                baseViewModel = baseViewModel,
                innerPadding = innerPadding,
                showDialog = showDialog,
                onDismissDialog = onDismissDialog
            )
        }

        composable(Screens.Saving.route) {
            SavingScreen(innerPadding)
        }

        composable(Screens.CloseMonth.route) { entry ->
            val parent = remember(entry) { navController.getBackStackEntry(Screens.Home.route) }
            val vm: BaseViewModel = hiltViewModel(parent)
            CloseMonthScreen(
                closeMonthViewModel = vm,
                onNavigateBack = { navController.navigateSafe(Screens.Home.route) },
                innerPadding1 = innerPadding
            )
        }

        composable(Screens.History.route) {
            HistoryScreen(innerPadding1 = innerPadding)
        }

        composable(Screens.ExportReport.route) {
            ExportReportScreen(innerPadding1 = innerPadding)
        }
    }
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

        TextCompose(
            text = stringResource(R.string.title_drawer),
            modifier = Modifier.padding(start = 18.dp)
        )

        Spacer(Modifier.height(20.dp))

        items.forEachIndexed { index, item ->
            NavigationDrawerItem(
                label = { TextCompose(text = item.name) },
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
            TextCompose(
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
