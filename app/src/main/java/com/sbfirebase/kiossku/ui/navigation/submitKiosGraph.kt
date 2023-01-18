package com.sbfirebase.kiossku.ui.navigation

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.sbfirebase.kiossku.route.AllRoute
import com.sbfirebase.kiossku.ui.screen.submitkios.screen.PilihSewaJualScreen
import com.sbfirebase.kiossku.ui.screen.submitkios.screen.berhasilsubmit.BerhasilSubmitScreen
import com.sbfirebase.kiossku.ui.screen.submitkios.screen.langkahkedua.LangkahKedua
import com.sbfirebase.kiossku.ui.screen.submitkios.screen.langkahkedua.LangkahKeduaViewModel
import com.sbfirebase.kiossku.ui.screen.submitkios.screen.langkahketiga.LangkahKetiga
import com.sbfirebase.kiossku.ui.screen.submitkios.screen.langkahpertama.LangkahPertama
import com.sbfirebase.kiossku.ui.screen.submitkios.screen.langkahpertama.LangkahPertamaViewModel

fun NavGraphBuilder.submitKiosGraph(
    navController : NavHostController
){
    val root = AllRoute.SubmitKios.root
    navigation(
        route = root,
        startDestination = AllRoute.SubmitKios.SewaJual.route
    ){
        composable(
            route = AllRoute.SubmitKios.SewaJual.route
        ){
            val rootBackStackEntry = remember(it) {
                navController.getBackStackEntry(root)
            }

            PilihSewaJualScreen(
                navigate = {
                    navController.navigate(AllRoute.SubmitKios.LangkahPertama.route)
                },
                viewModel = hiltViewModel(rootBackStackEntry)
            )
        }

        composable(
            route = AllRoute.SubmitKios.LangkahPertama.route
        ){
            val rootBackStackEntry = remember(it) {
                navController.getBackStackEntry(root)
            }
            val viewModel = hiltViewModel<LangkahPertamaViewModel>(rootBackStackEntry)

            LangkahPertama(
                navigateNext = {
                    navController.navigate(AllRoute.SubmitKios.LangkahKedua.route)
                    viewModel.doneNavigating()
                },
                navigateBack = {
                    navController.popBackStack()
                },
                viewModel = viewModel
            )
        }

        composable(
            route = AllRoute.SubmitKios.LangkahKedua.route
        ){
            val rootEntry = remember(it){
                navController.getBackStackEntry(root)
            }
            val viewModel = hiltViewModel<LangkahKeduaViewModel>(rootEntry)

            LangkahKedua(
                viewModel = viewModel,
                navigateNext = {
                    navController.navigate(AllRoute.SubmitKios.LangkahKetiga.route)
                    viewModel.doneNavigating()
                },
                navigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = AllRoute.SubmitKios.LangkahKetiga.route
        ){
            val rootEntry = remember(it) {
                navController.getBackStackEntry(root)
            }

            LangkahKetiga(
                viewModel1 = hiltViewModel(rootEntry),
                viewModel2 = hiltViewModel(rootEntry),
                viewModel3 = hiltViewModel(rootEntry),
                navigateNext = {
                    navController.navigate(AllRoute.SubmitKios.SubmitDataSucceed.route){
                        popUpTo(route = AllRoute.SubmitKios.root){
                            inclusive = true
                        }
                    }
                },
                navigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = AllRoute.SubmitKios.SubmitDataSucceed.route
        ){
            BerhasilSubmitScreen(navController = navController)
        }

    }
}