package com.sbfirebase.kiossku.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.ui.Modifier
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
                Scaffold(
                    bottomBar = {
                        BottomNavBar(navController = navController)
                    },
                    isFloatingActionButtonDocked = true,
                    floatingActionButtonPosition = FabPosition.Center,
                    floatingActionButton = {
                        BottomNavBarActionButton(
                            navigate = {
                                navController.replaceAndNavigate(AllRoute.SewaJual.route)
                            }
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