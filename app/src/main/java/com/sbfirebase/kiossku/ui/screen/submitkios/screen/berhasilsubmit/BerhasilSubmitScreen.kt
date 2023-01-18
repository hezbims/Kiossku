package com.sbfirebase.kiossku.ui.screen.submitkios.screen.berhasilsubmit

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sbfirebase.kiossku.R
import com.sbfirebase.kiossku.route.AllRoute
import com.sbfirebase.kiossku.ui.theme.KiosskuTheme

@Composable
fun BerhasilSubmitScreen(
    navController : NavHostController
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ){
        Column(
            modifier = Modifier
                .align(Alignment.Center)
        ){
            Image(
                painter = painterResource(id = R.drawable.berhasil_submit_image),
                contentDescription = null,
                modifier = Modifier
                    .size(260.dp)
            )

            Text(
                text = "Properti anda berhasil ditambahkan",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(top = 40.dp)
            )
        }

        Button(
            onClick = {
                navController.navigate(AllRoute.Home.route){
                    popUpTo(0){
                        inclusive = true
                    }
                }
            },
            modifier = Modifier
                .padding(bottom = 106.dp)
                .align(Alignment.BottomCenter)
        ) {
            Text("Kembali ke halaman utama")
        }
    }
}

@Composable
@Preview
fun BerhasilSubmitScreenPreview(){
    KiosskuTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
        ){
            BerhasilSubmitScreen(
                navController = rememberNavController()
            )
        }
    }
}