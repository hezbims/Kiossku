package com.sbfirebase.kiossku.submitkios.uicomponent

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sbfirebase.kiossku.R
import com.sbfirebase.kiossku.ui.theme.GreenKiossku
import com.sbfirebase.kiossku.ui.theme.KiosskuTheme

@Composable
fun SubmitHeader(
    langkah : Int,
    modifier : Modifier = Modifier
){
    Row{
        Image(
            painter = painterResource(
                id = R.drawable.submit_decoration_image
            ),
            contentDescription = null,
            modifier = Modifier
                .width(116.dp)
                .height(110.dp)
        )
        Box{
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .align(Alignment.Center)
            ){
                Text(
                    text = "Langkah ke $langkah dari 3",
                    style = TextStyle(
                        color = GreenKiossku,
                        fontSize = 10.sp
                    )
                )
                Text(
                    text = "Formulir spesifikasi properti!",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    ),
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .width(164.dp)
                )
            }

        }
    }
}

@Composable
@Preview
fun SubmitHeaderPreview(){
    KiosskuTheme{
        Surface{
            SubmitHeader(langkah = 1)
        }
    }
}