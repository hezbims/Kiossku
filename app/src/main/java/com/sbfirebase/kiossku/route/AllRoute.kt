package com.sbfirebase.kiossku.route

import android.net.Uri
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.navArgument
import com.sbfirebase.kiossku.R
import com.sbfirebase.kiossku.domain.model.KiosData
import com.sbfirebase.kiossku.domain.model.KiosDataType

sealed interface NavRoute{
    val route : String
}
sealed interface BottomNavItem : NavRoute {
    val icon : ImageVector
    val labelStringId : Int
}

sealed class AllRoute {
    object Home : BottomNavItem {
        override val route = "HomeRoute"
        override val icon = Icons.Outlined.Home
        override val labelStringId = R.string.home_label
    }

    object Detail : NavRoute{
        const val argName = "product"
        override val route = "DetailRoute/{$argName}"
        val args = listOf(
            navArgument(name = argName){ type = KiosDataType() }
        )

        fun formatRouteWithArg(kiosData : KiosData) : String{
            val arg = Uri.encode(kiosData.toJsonString())
            return "DetailRoute/$arg"
        }
    }

    object Profile : BottomNavItem {
        override val route = "ProfileRoute"
        override val icon = Icons.Rounded.AccountCircle
        override val labelStringId = R.string.profile_label
    }


    object SubmitKios{
        val root = "SubmitKiosRoute"

        object SewaJual : NavRoute {
            override val route = "SewaJualRoute"
        }

        object LangkahPertama : NavRoute {
            override val route = "LangkahPertamaRoute"
        }

        object LangkahKedua : NavRoute {
            override val route = "LangkahKeduaRoute"
        }

        object LangkahKetiga : NavRoute {
            override val route = "LangkahKetigaRoute"
        }

        object SubmitDataSucceed : NavRoute{
            override val route = "SubmitDataRoute"
        }
    }
}

val bottomNavItems = listOf(
    AllRoute.Home,
    AllRoute.Profile,
)