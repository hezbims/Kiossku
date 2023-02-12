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
        startDestination = AllRoute.SubmitKios.SewaJual.root
    ){
        composable(
            route = AllRoute.SubmitKios.SewaJual.root
        ){
            val rootBackStackEntry = remember(it) {
                navController.getBackStackEntry(root)
            }

            PilihSewaJualScreen(
                navigate = {
                    navController.navigate(AllRoute.SubmitKios.LangkahPertama.root)
                },
                viewModel = hiltViewModel(rootBackStackEntry)
            )
        }

        composable(
            route = AllRoute.SubmitKios.LangkahPertama.root
        ){
            val rootBackStackEntry = remember(it) {
                navController.getBackStackEntry(root)
            }
            val viewModel = hiltViewModel<LangkahPertamaViewModel>(rootBackStackEntry)

            LangkahPertama(
                navigateNext = {
                    navController.navigate(AllRoute.SubmitKios.LangkahKedua.root)
                    viewModel.doneNavigating()
                },
                navigateBack = {
                    navController.popBackStack()
                },
                viewModel = viewModel
            )
        }

        composable(
            route = AllRoute.SubmitKios.LangkahKedua.root
        ){
            val rootEntry = remember(it){
                navController.getBackStackEntry(root)
            }
            val viewModel = hiltViewModel<LangkahKeduaViewModel>(rootEntry)

            LangkahKedua(
                viewModel = viewModel,
                navigateNext = {
                    navController.navigate(AllRoute.SubmitKios.LangkahKetiga.root)
                    viewModel.doneNavigating()
                },
                navigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = AllRoute.SubmitKios.LangkahKetiga.root
        ){
            val rootEntry = remember(it) {
                navController.getBackStackEntry(root)
            }

            LangkahKetiga(
                viewModel1 = hiltViewModel(rootEntry),
                viewModel2 = hiltViewModel(rootEntry),
                viewModel3 = hiltViewModel(rootEntry),
                navigateNext = {
                    navController.navigate(AllRoute.SubmitKios.SubmitDataSucceed.root){
                        popUpTo(route = AllRoute.SubmitKios.root){
                            inclusive = true
                        }
                    }
                },
                navigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = AllRoute.SubmitKios.SubmitDataSucceed.root
        ){
            BerhasilSubmitScreen(navController = navController)
        }

    }
}