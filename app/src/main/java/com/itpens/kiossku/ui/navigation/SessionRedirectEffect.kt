package com.itpens.kiossku.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.itpens.kiossku.route.AllRoute
import com.itpens.kiossku.domain.SessionManager
import kotlinx.coroutines.flow.collectLatest

/**
 * Mengamati status validitas sesi dari [SessionManager].
 * Ketika sesi diketahui tidak valid (refresh token gagal permanen),
 * seluruh back stack dibersihkan dan user diarahkan ke halaman login.
 *
 * Dipisahkan dari MainActivity sebagai composable agar mudah diuji.
 */
@Composable
fun SessionRedirectEffect(
    navController : NavHostController,
    sessionManager : SessionManager
){
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(navController, sessionManager){
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
            sessionManager.isSessionValid.collectLatest{ isSessionValid ->
                if (!isSessionValid &&
                    navController.currentDestination?.route != AllRoute.Auth.Login.route
                ){
                    navController.replaceAndNavigate(AllRoute.Auth.Login.route)
                }
            }
        }
    }
}
