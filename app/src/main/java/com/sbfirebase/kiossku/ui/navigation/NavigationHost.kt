package com.sbfirebase.kiossku.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sbfirebase.kiossku.route.AllRoute
import com.sbfirebase.kiossku.ui.screen.detail.DetailScreen
import com.sbfirebase.kiossku.ui.screen.home.HomeScreen
import com.sbfirebase.kiossku.ui.screen.home.HomeViewModel
import com.sbfirebase.kiossku.ui.screen.landing_page.LandingPageScreen

@Composable
fun NavigationHost(
    navController : NavHostController,
    modifier: Modifier
){
    NavHost(
        navController = navController,
        startDestination = AllRoute.LandingPage.route,
        modifier = modifier
    ) {
        composable(
            route = AllRoute.LandingPage.route
        ){
            LandingPageScreen(navController = navController)
        }

        profileNavGraph(navController = navController)

        composable(
            route = AllRoute.Home.root
        ){
            val viewModel = hiltViewModel<HomeViewModel>()
            HomeScreen(
                uiHomeState = viewModel.uiHomeState.collectAsState().value,
                loadData = viewModel::loadData,
                onItemClick = { kiosData ->
                    navController.navigate(
                        AllRoute.Detail.formatRouteWithArg(kiosData)
                    )
                }
            )
        }

        composable(
            route = AllRoute.Detail.root,
            arguments = AllRoute.Detail.args
        ){
            DetailScreen(
                navController = navController
            )
        }

        submitKiosGraph(navController = navController)

        authNavGraph(navController = navController)
    }
}

fun NavHostController.replaceAndNavigate(
    route : String ,
    saveCurrentState : Boolean = false ,
    restoreCurrentState : Boolean = false
){
    navigate(route) {
        popUpTo(0){
            saveState = saveCurrentState
        }
        launchSingleTop = true
        restoreState = restoreCurrentState
    }
}