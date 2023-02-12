package com.sbfirebase.kiossku.ui.navigation

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.navigation
import com.sbfirebase.kiossku.route.AllRoute
import com.sbfirebase.kiossku.ui.screen.profile.ProfileScreen
import com.sbfirebase.kiossku.ui.screen.profile.ProfileScreenEvent
import com.sbfirebase.kiossku.ui.screen.profile.ProfileViewModel
import com.sbfirebase.kiossku.ui.screen.profile.update_profile_dialog.UpdateProfileDialog

fun NavGraphBuilder.profileNavGraph(navController : NavHostController){
    navigation(
        route = AllRoute.Profile.root,
        startDestination = AllRoute.Profile.MainProfile.route
    ){
        composable(route = AllRoute.Profile.MainProfile.route){
            val rootBackStackEntry = remember(it) {
                navController.getBackStackEntry(AllRoute.Profile.root)
            }

            ProfileScreen(
                navController = navController,
                viewModel = hiltViewModel(rootBackStackEntry)
            )
        }

        dialog(route = AllRoute.Profile.UpdateProfile.route){
            val rootBackStackEntry = remember(it) {
                navController.getBackStackEntry(AllRoute.Profile.root)
            }
            val mainProfileViewModel : ProfileViewModel = hiltViewModel(rootBackStackEntry)
            Log.e("qqq" , mainProfileViewModel
                .uiState
                .collectAsState()
                .value
                .getUserResponse
                .data?.toString() ?: "null")
            val data = mainProfileViewModel
                .uiState
                .collectAsState()
                .value
                .getUserResponse
                .data!!.let{ currentData ->
                    currentData.copy(
                        nomorTelepon = currentData
                            .nomorTelepon
                            .substring(startIndex = 2)
                    )
                }


            UpdateProfileDialog(
                initialData = data,
                doneUpdatingProfile = { newData ->
                    mainProfileViewModel
                        .onEvent(ProfileScreenEvent.DoneUpdatingProfile(newData))
                },
                navController = navController
            )
        }
    }
}