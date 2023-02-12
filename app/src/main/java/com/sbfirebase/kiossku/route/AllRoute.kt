package com.sbfirebase.kiossku.route

import android.net.Uri
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.sbfirebase.kiossku.R
import com.sbfirebase.kiossku.domain.model.KiosData
import com.sbfirebase.kiossku.domain.model.KiosDataType

sealed interface NavRoute{
    val root : String
}
sealed interface BottomNavItem : NavRoute {
    val icon : ImageVector
    val labelStringId : Int
}

sealed class AllRoute {
    object LandingPage {
        const val route = "LandingPageRoute"
    }

    object Home : BottomNavItem {
        override val root = "HomeRoute"
        override val icon = Icons.Outlined.Home
        override val labelStringId = R.string.home_label
    }

    object Detail : NavRoute{
        private const val baseRoute = "DetailRoute"

        const val argName = "product"
        override val root = "$baseRoute/{$argName}"
        val args = listOf(
            navArgument(name = argName){ type = KiosDataType() }
        )

        fun formatRouteWithArg(kiosData : KiosData) : String{
            val arg = Uri.encode(kiosData.toJsonString())
            return "$baseRoute/$arg"
        }
    }

    object Profile : BottomNavItem {
        override val root = "ProfileRoute"
        override val icon = Icons.Rounded.AccountCircle
        override val labelStringId = R.string.profile_label
        object MainProfile {
            const val route = "MainProfileRoute"
        }
        object UpdateProfile {
            const val route = "UpdateProfileRoute"
        }
        object ChangePassword {
            const val route = "ChangePasswordRoute"
        }
    }

    object Auth {
        const val root = "AuthenticationRoute"

        object Login {
            const val route = "LoginRoute"
        }

        object Register {
            const val route = "RegisterRoute"
        }

        object ConfirmEmail {
            private const val baseRoute = "ConfirmEmailRoute"

            const val argName = "email"
            const val route = "$baseRoute/{$argName}"

            private val args = listOf(
                navArgument(name = argName) { type = NavType.StringType }
            )

            // digunakan untuk navigasi
            fun routeWithArg(arg : String) : String{
                return "$baseRoute/$arg"
            }
        }
    }


    object SubmitKios{
        const val root = "SubmitKiosRoute"

        object SewaJual : NavRoute {
            override val root = "SewaJualRoute"
        }

        object LangkahPertama : NavRoute {
            override val root = "LangkahPertamaRoute"
        }

        object LangkahKedua : NavRoute {
            override val root = "LangkahKeduaRoute"
        }

        object LangkahKetiga : NavRoute {
            override val root = "LangkahKetigaRoute"
        }

        object SubmitDataSucceed : NavRoute{
            override val root = "SubmitDataRoute"
        }
    }
}

val bottomNavItems = listOf(
    AllRoute.Home,
    AllRoute.Profile,
)