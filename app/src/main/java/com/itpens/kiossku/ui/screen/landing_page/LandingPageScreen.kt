package com.itpens.kiossku.ui.screen.landing_page

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
import com.itpens.kiossku.constant.ApiCode
import com.itpens.kiossku.domain.apiresponse.ApiResponse
import com.itpens.kiossku.route.AllRoute
import com.itpens.kiossku.ui.navigation.replaceAndNavigate
import com.itpens.kiossku.ui.theme.KiosskuTheme
import com.itpens.kiossku.R

@Composable
fun LandingPageScreen(
    viewModel : LandingPageViewModel = hiltViewModel(),
    navController : NavHostController
){
    val apiResponse = viewModel.apiResponse.collectAsState().value
    when (apiResponse){
        is ApiResponse.Success -> {
            navController.replaceAndNavigate(AllRoute.Home.root)
            viewModel.doneNavigating()
        }
        is ApiResponse.Failure -> {
            if (apiResponse.errorCode != null){
                assert(apiResponse.errorCode == ApiCode.UNAUTHORIZED)
                viewModel.doneNavigating()
                navController.replaceAndNavigate(AllRoute.Auth.root)
            }
            else
                LandingPageScreen(isApiResponseFailed = true)
        }
        is ApiResponse.Loading ->
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