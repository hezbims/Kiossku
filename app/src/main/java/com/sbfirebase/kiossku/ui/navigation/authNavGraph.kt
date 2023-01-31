package com.sbfirebase.kiossku.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.sbfirebase.kiossku.route.AllRoute
import com.sbfirebase.kiossku.ui.screen.authentication.login.LoginScreen
import com.sbfirebase.kiossku.ui.screen.authentication.register.RegisterScreen

fun NavGraphBuilder.authNavGraph(
    navController : NavHostController
){
    navigation(
        startDestination = AllRoute.Auth.Login.route,
        route = AllRoute.Auth.root
    ){
        composable(
            route = AllRoute.Auth.Login.route
        ){
            LoginScreen(navController = navController)
        }

        composable(
            route = AllRoute.Auth.Register.route
        ){
            RegisterScreen(navController = navController)
        }
    }
}