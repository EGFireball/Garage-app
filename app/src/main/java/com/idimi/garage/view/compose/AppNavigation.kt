package com.idimi.garage.view.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableChipColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.idimi.garage.R
import com.idimi.garage.view.compose.components.BuildScreenTopBar
import com.idimi.garage.view.ui.theme.getTheme
import com.idimi.garage.view.viewmodel.GarageViewModel

sealed class NavScreen(val route: String, val title: String) {
    object Garage : NavScreen("garage", "Garage")
    object Places : NavScreen("places", "Places")
//    object Favorites : NavScreen("favorites", "Favorites")
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun TabsWithNavHost() {
//    val navController = rememberNavController()
//    val tabs = listOf(NavScreen.Garage, NavScreen.Places, NavScreen.Favorites)
//
//    Scaffold(
//        topBar = {
//            TopAppBar(title = { "My Vacation Garage" })
//        }
//    ) { padding ->
//        val navBackStackEntry by navController.currentBackStackEntryAsState()
//        val currentRoute = navBackStackEntry?.destination?.route ?: NavScreen.Garage.route
//
//        Column(Modifier.padding(padding)) {
//            TabRow(selectedTabIndex = tabs.indexOfFirst {
//                it.route == currentRoute
//            }) {
//                tabs.forEach { screen ->
//                    Tab(
//                        selected = currentRoute == screen.route,
//                        onClick = {
//                            navController.navigate(screen.route) {
//                                launchSingleTop = true
//                                restoreState = true
//                                popUpTo(
//                                    navController.graph.startDestinationId
//                                ) { saveState = true }
//                            }
//                        },
//                        text = { Text(screen.title) }
//                    )
//                }
//            }
//            NavHost (
//                navController = navController,
//                startDestination = NavScreen.Garage.route,
//                modifier = Modifier.fillMaxSize()
//            ) {
//                composable(NavScreen.Garage.route) {
//                    BuildScreenContent("GARAGE")
//                }
//                composable(NavScreen.Places.route) {
//                    BuildScreenContent("PLACES")
//                }
//                composable(NavScreen.Favorites.route) {
//                    BuildScreenContent("FAVORITES")
//                }
//            }
//        }
//    }
//}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomBarWithNavHost(garageViewModel: GarageViewModel) {
    val navController = rememberNavController()
    val tabs = listOf(NavScreen.Garage, NavScreen.Places)

    val topAppBarHeight = remember { mutableIntStateOf(0) }
    val topAppBarWidth = remember { mutableIntStateOf(0) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: NavScreen.Garage.route

    BuildScreenTopBar(
        topBarTitle = when (currentRoute) {
            NavScreen.Garage.route -> NavScreen.Garage.title
            NavScreen.Places.route -> NavScreen.Places.title
            else -> "Unknown"
        },
        topAppBarWidth = topAppBarWidth,
        topAppBarHeight = topAppBarHeight
    ) { pv ->
        Scaffold(
            //modifier = Modifier.padding(pv),
            bottomBar = {
                NavigationBar(
                    contentColor = getTheme().secondary,
                    //modifier = Modifier.navigationBarsPadding()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        tabs.forEach { screen ->
                            NavigationBarItem(
                                modifier = Modifier
                                    .height(80.dp)
                                    .background(color = if (currentRoute == screen.route) getTheme().primary else getTheme().secondary),
                                selected = currentRoute == screen.route,
                                onClick = {
                                    if (currentRoute != screen.route) {
                                        navController.navigate(screen.route) {
                                            launchSingleTop = true
                                            restoreState = true
                                            popUpTo(
                                                navController.graph.startDestinationId
                                            ) { saveState = true }
                                        }
                                    }
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    indicatorColor = getTheme().primary,
                                    selectedIconColor = getTheme().onPrimary,
                                    selectedTextColor = getTheme().onPrimary,
                                    unselectedIconColor = getTheme().onSecondary,
                                    unselectedTextColor = getTheme().onSecondary
                                ),
                                icon = {
                                    Icon(
                                        imageVector = when (screen.route) {
                                            NavScreen.Garage.route -> ImageVector.vectorResource(id = R.drawable.ic_garage)
                                            NavScreen.Places.route -> Icons.Default.Place
                                            else -> Icons.Default.CheckCircle
                                        },
                                        contentDescription = screen.title,
                                    )
                                },
                                label = {
                                    Text(
                                        text = screen.title,
                                        //color = getTheme().onPrimary
                                    )
                                }
                            )
                        }
                    }
                }
            }
        ) { padding ->
            NavHost(
                navController = navController,
                startDestination = NavScreen.Garage.route,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(pv)
            ) {
                composable(NavScreen.Garage.route) {
                    MyGarage(
                        modifier = Modifier.padding(bottom = 80.dp),
                        garageViewModel = garageViewModel
                    )
                }
                composable(NavScreen.Places.route) {
                    PlacesScreen(
                        modifier = Modifier.padding(bottom = 80.dp),
                        garageViewModel = garageViewModel,
                    )
                }
            }
        }
    }
}

@Composable
fun PlacesScreen(
    modifier: Modifier,
    garageViewModel: GarageViewModel,
) {

    val items = garageViewModel.poisStateFlow.collectAsStateWithLifecycle()

    val isFavoriteSelected = remember {
        mutableStateOf(false)
    }

    val triggerListRecomposition = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        garageViewModel.getAllPois()
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilterChip(
                modifier = Modifier
                    .wrapContentWidth()
                    .height(40.dp),
                colors = SelectableChipColors(
                    containerColor = getTheme().secondary,
                    labelColor = getTheme().onPrimary,
                    selectedContainerColor = getTheme().primary,
                    selectedLabelColor = getTheme().onPrimary,
                    leadingIconColor = Color.Transparent,
                    trailingIconColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    disabledLabelColor = Color.Transparent,
                    disabledLeadingIconColor = Color.Transparent,
                    disabledTrailingIconColor = Color.Transparent,
                    disabledSelectedContainerColor = Color.Transparent,
                    selectedLeadingIconColor = Color.Transparent,
                    selectedTrailingIconColor = Color.Transparent
                ),
                selected = !isFavoriteSelected.value,
                onClick = {
                    garageViewModel.showAllPlaces()
                    triggerListRecomposition.value = true
                    isFavoriteSelected.value = false
                },
                label = {
                    Text(text = "All",
                        color = getTheme().onPrimary,
                        maxLines = 1) },
            )
            Spacer(Modifier.width(12.dp))
            FilterChip(
                modifier = Modifier
                    .wrapContentWidth()
                    .height(40.dp),
                colors = SelectableChipColors(
                    containerColor = getTheme().secondary,
                    labelColor = getTheme().onPrimary,
                    selectedContainerColor = getTheme().primary,
                    selectedLabelColor = getTheme().onPrimary,
                    leadingIconColor = Color.Transparent,
                    trailingIconColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    disabledLabelColor = Color.Transparent,
                    disabledLeadingIconColor = Color.Transparent,
                    disabledTrailingIconColor = Color.Transparent,
                    disabledSelectedContainerColor = Color.Transparent,
                    selectedLeadingIconColor = Color.Transparent,
                    selectedTrailingIconColor = Color.Transparent
                ),
                selected = isFavoriteSelected.value,
                onClick = {
                    garageViewModel.showOnlyFavoritePlaces()
                    triggerListRecomposition.value = true
                    isFavoriteSelected.value = true
                },
                label = {
                    Text(
                        text = "Favorites",
                        color = getTheme().onPrimary,
                        maxLines = 1
                    )
                },
            )
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(items.value) { item ->
                PlaceCard(
                    garageViewModel = garageViewModel,
                    place = item,
                    triggerRecomposition = triggerListRecomposition
                )
            }
        }
    }
}