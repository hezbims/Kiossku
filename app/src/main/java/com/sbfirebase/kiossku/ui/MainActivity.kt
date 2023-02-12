package com.sbfirebase.kiossku.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sbfirebase.kiossku.route.AllRoute
import com.sbfirebase.kiossku.ui.navigation.BottomNavBar
import com.sbfirebase.kiossku.ui.navigation.BottomNavBarActionButton
import com.sbfirebase.kiossku.ui.navigation.NavigationHost
import com.sbfirebase.kiossku.ui.screen.profile.update_profile_dialog.ProfileDialogViewModel
import com.sbfirebase.kiossku.ui.theme.KiosskuTheme
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.components.ActivityComponent

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @EntryPoint
    @InstallIn(ActivityComponent::class)
    interface ProfileDialogViewModelFactoryProvider{
        fun getFactory() : ProfileDialogViewModel.Factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KiosskuTheme {
                val navController = rememberNavController()
                val showBottomBar = rememberSaveable { mutableStateOf(true) }

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                showBottomBar.value = when (navBackStackEntry?.destination?.route){
                    AllRoute.SubmitKios.SewaJual.root  , AllRoute.Home.root , AllRoute.Profile.root -> true
                    else -> false
                }

                Scaffold(
                    bottomBar = {
                        BottomNavBar(
                            navController = navController,
                            showBottomBar = showBottomBar.value
                        )
                    },
                    isFloatingActionButtonDocked = true,
                    floatingActionButtonPosition = FabPosition.Center,
                    floatingActionButton = {
                        BottomNavBarActionButton(
                            navController = navController,
                            showButton = showBottomBar.value
                        )
                    }
                ) { innerPadding ->
                    NavigationHost(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }
}