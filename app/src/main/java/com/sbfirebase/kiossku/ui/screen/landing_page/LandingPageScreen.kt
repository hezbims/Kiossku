package com.sbfirebase.kiossku.ui.screen.landing_page

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.sbfirebase.kiossku.R
import com.sbfirebase.kiossku.domain.apiresponse.AuthorizedApiResponse
import com.sbfirebase.kiossku.route.AllRoute
import com.sbfirebase.kiossku.ui.navigation.replaceAndNavigate
import com.sbfirebase.kiossku.ui.theme.KiosskuTheme

@Composable
fun LandingPageScreen(
    viewModel : LandingPageViewModel = hiltViewModel(),
    navController : NavHostController
){
    val apiResponse = viewModel.apiResponse.collectAsState().value
    when (apiResponse){
        is AuthorizedApiResponse.Unauthorized -> {
            viewModel.doneNavigating()
            navController.replaceAndNavigate(AllRoute.Auth.root)
        }
        is AuthorizedApiResponse.Success -> {
            viewModel.doneNavigating()
            navController.replaceAndNavigate(AllRoute.Home.route)
        }
        is AuthorizedApiResponse.Failure ->
            LandingPageScreen(isApiResponseFailed = true)
        is AuthorizedApiResponse.Loading ->
            LandingPageScreen(isApiResponseFailed = false)
    }
}
@Composable
private fun LandingPageScreen(
    isApiResponseFailed : Boolean
){
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Image(
                painter = painterResource(id = R.drawable.kiossku_header),
                contentDescription = null,
                modifier = Modifier
                    .width(176.dp)
                    .height(44.dp)
            )
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(top = 24.dp)
            )

            Text(
                text =
                if (isApiResponseFailed) "Gagal memuat data, mencoba mengulang kembali!"
                else "Sedang memuat data..."
            )

        }
    }
}

@Composable
@Preview
private fun LandingPageScreenPreview(){
    KiosskuTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {
            LandingPageScreen(
                isApiResponseFailed = false
            )
        }
    }
}