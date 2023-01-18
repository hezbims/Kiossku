package com.sbfirebase.kiossku.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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
import com.sbfirebase.kiossku.ui.navigation.replaceAndNavigate
import com.sbfirebase.kiossku.ui.screen.authentication.login.LoginActivity
import com.sbfirebase.kiossku.ui.theme.KiosskuTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KiosskuTheme {
                val navController = rememberNavController()
                val showBottomBar = rememberSaveable { mutableStateOf(true) }

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                showBottomBar.value = when (navBackStackEntry?.destination?.route){
                    AllRoute.SubmitKios.SewaJual.route  , AllRoute.Home.route , AllRoute.Profile.route -> true
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
                            navigate = {
                                navController.replaceAndNavigate(AllRoute.SubmitKios.root)
                            },
                            showButton = showBottomBar.value
                        )
                    }
                ) { innerPadding ->
                    NavigationHost(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding),
                        startLoginActivity = ::startLoginActivity,
                        displayError = ::displayError
                    )
                }
            }
        }
    }

    private fun displayError(elemen : String){
        Toast.makeText(
            this,
            "$elemen tidak boleh kosong!",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun startLoginActivity(){
        startActivity(
            Intent(
                this,
                LoginActivity::class.java
            )
        )
        finish()
    }
}