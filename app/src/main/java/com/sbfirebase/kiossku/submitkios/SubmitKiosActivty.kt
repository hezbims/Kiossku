package com.sbfirebase.kiossku.submitkios

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Surface
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sbfirebase.kiossku.R
import com.sbfirebase.kiossku.ui.theme.KiosskuTheme

class SubmitKiosActivty : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KiosskuTheme {
                Surface {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = stringResource(
                            id = R.string.pilih_sewa_jual
                        )
                    ) {
                        composable(
                            route = getString(R.string.pilih_sewa_jual)
                        ){
                            PilihSewaJual()
                        }
                    }
                }
            }
        }
    }
}

