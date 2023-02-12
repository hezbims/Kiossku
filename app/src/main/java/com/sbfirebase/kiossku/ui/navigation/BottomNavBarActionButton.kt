package com.sbfirebase.kiossku.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddToPhotos
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.sbfirebase.kiossku.route.AllRoute
import com.sbfirebase.kiossku.ui.theme.GreenKiossku

@Composable
fun BottomNavBarActionButton(
    navController : NavHostController,
    showButton : Boolean
){
    AnimatedVisibility(
        visible = showButton,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        FloatingActionButton(
            shape = CircleShape,
            backgroundColor = GreenKiossku,
            contentColor = Color.White,
            onClick = {
                navController.replaceAndNavigate(
                    route = AllRoute.SubmitKios.root,
                    saveCurrentState = true,
                    restoreCurrentState = false
                )
            }
        ) {
            Icon(
                imageVector = Icons.Outlined.AddToPhotos,
                contentDescription = "Sewa atau jual kios",
                modifier = Modifier
                    .size(24.dp)
            )
        }
    }
}