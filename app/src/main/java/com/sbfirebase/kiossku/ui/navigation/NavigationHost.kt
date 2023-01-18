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
import com.sbfirebase.kiossku.ui.screen.profile.ProfileScreen
import com.sbfirebase.kiossku.ui.screen.profile.ProfileViewModel

@Composable
fun NavigationHost(
    navController : NavHostController,
    modifier: Modifier,
    startLoginActivity : () -> Unit,
    displayError : (String) -> Unit
){
    NavHost(
        navController = navController,
        startDestination = AllRoute.Profile.route,
        modifier = modifier
    ) {
        composable(
            route = AllRoute.Profile.route
        ) {
            val viewModel : ProfileViewModel = hiltViewModel()
            val uiState = viewModel
                .uiState
                .collectAsState()
                .value
            if (uiState.isLoggedOut)
                startLoginActivity()
            ProfileScreen(
                uiState = uiState,
                logout = viewModel::logout
            )
        }
        composable(
            route = AllRoute.Home.route
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
            route = AllRoute.Detail.route,
            arguments = AllRoute.Detail.args
        ){
            DetailScreen()
        }

        submitKiosGraph(navController = navController)


    }
}

fun NavHostController.replaceAndNavigate(route : String){
    navigate(route) {
        popUpTo(0){
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}