package com.itpens.kiossku.ui

import android.os.Bundle
import android.view.WindowManager
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
import com.itpens.kiossku.domain.SessionManager
import com.itpens.kiossku.route.AllRoute
import com.itpens.kiossku.ui.navigation.BottomNavBar
import com.itpens.kiossku.ui.navigation.BottomNavBarActionButton
import com.itpens.kiossku.ui.navigation.NavigationHost
import com.itpens.kiossku.ui.navigation.SessionRedirectEffect
import com.itpens.kiossku.ui.screen.profile.update_profile_dialog.ProfileDialogViewModel
import com.itpens.kiossku.ui.theme.KiosskuTheme
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var sessionManager : SessionManager

    @EntryPoint
    @InstallIn(ActivityComponent::class)
    interface ProfileDialogViewModelFactoryProvider{
        fun getFactory() : ProfileDialogViewModel.Factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        setContent {
            KiosskuTheme {
                val navController = rememberNavController()
                val showBottomBar = rememberSaveable { mutableStateOf(true) }

                SessionRedirectEffect(
                    navController = navController,
                    sessionManager = sessionManager
                )

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                showBottomBar.value = when (navBackStackEntry?.destination?.route){
                    AllRoute.SubmitKios.SewaJual.root  ,
                    AllRoute.Home.root , AllRoute.Profile.MainProfile.route ,
                    AllRoute.Profile.UpdateProfile.route -> true
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