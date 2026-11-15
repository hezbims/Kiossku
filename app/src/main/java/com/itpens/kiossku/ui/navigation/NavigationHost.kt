package com.itpens.kiossku.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.itpens.kiossku.route.AllRoute
import com.itpens.kiossku.ui.screen.detail.DetailScreen
import com.itpens.kiossku.ui.screen.home.HomeScreen
import com.itpens.kiossku.ui.screen.landing_page.LandingPageScreen

@Composable
fun NavigationHost(
    navController : NavHostController,
    modifier: Modifier
){
    NavHost(
        navController = navController,
        startDestination = AllRoute.Home.root,
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
            HomeScreen(navController = navController)
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