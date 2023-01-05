package com.sbfirebase.kiossku.ui.navigation

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.sbfirebase.kiossku.route.bottomNavItems

@Composable
fun BottomNavBar(
    navController : NavHostController
){
    BottomAppBar(
        cutoutShape = CircleShape,
        backgroundColor = Color.White
    ) {
        BottomNavigation(
            backgroundColor = Color.White
        ){
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            bottomNavItems.forEach { screen ->
                BottomNavigationItem(
                    icon = {
                        Icon(
                            imageVector = screen.icon,
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(
                            stringResource(
                                screen.labelStringId
                            )
                        )
                    },
                    selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                    onClick = {
                        navController.replaceAndNavigate(screen.route)
                    }
                )
            }
        }
    }

}

