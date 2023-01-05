package com.sbfirebase.kiossku.ui.screen.submitkios.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sbfirebase.kiossku.R
import com.sbfirebase.kiossku.ui.screen.submitkios.screen.langkahpertama.LangkahPertamaViewModel
import com.sbfirebase.kiossku.ui.theme.KiosskuTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sbfirebase.kiossku.ui.screen.submitkios.screen.langkahpertama.TipeLangkahPertamaData

@Composable
fun PilihSewaJualScreen(
    navigate : () -> Unit,
    modifier: Modifier = Modifier,
    viewModel : LangkahPertamaViewModel = viewModel()
){
    Column(
        modifier = modifier
            .padding(24.dp)
    ){
        Image(
            painter = painterResource(id = R.drawable.kiossku_header),
            contentDescription = null,
            modifier = Modifier
                .height(24.dp)
                .width(94.dp)
        )

        Text(
            text = "Property usaha unggulan",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier
                .padding(
                    top = 36.dp
                )
        )
        Text(
            text = "Bantu kembangkan umkm",
            fontSize = 10.sp
        )

        GambarSewaJual(
            textTitle = "Jual kios terbaikmu di kiossku.com",
            textButton = "Jual",
            image = painterResource(id = R.drawable.jual_kios_image),
            onClick = {
                viewModel.onDataChange(TipeLangkahPertamaData.IsSewa(false))
                navigate()
            },
            modifier = Modifier
                .padding(top = 16.dp)
        )

        GambarSewaJual(
            textTitle = "Sewakan kios terbaikmu di kiossku.com",
            textButton = "Sewa",
            image = painterResource(id = R.drawable.sewa_kios_image),
            onClick = {
                viewModel.onDataChange(TipeLangkahPertamaData.IsSewa(true))
                navigate()
            },
            modifier = Modifier
                .padding(top = 16.dp)
        )


    }
}

@Composable
fun GambarSewaJual(
    textTitle : String,
    textButton : String,
    image : Painter,
    onClick : () -> Unit,
    modifier: Modifier = Modifier
){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(145.dp)
    ){
        Image(
            painter = image,
            contentDescription = null,
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop,
            colorFilter = ColorFilter.tint(
                color = Color(0x991E1E1E),
                blendMode = BlendMode.Darken
            )
        )

        Column(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterStart)
        ){
            Text(
                text = textTitle,
                fontSize = 18.sp,
                modifier = Modifier
                    .width(132.dp),
                color = Color.White
            )
            Text(
                text = "#RealProperty",
                fontSize = 10.sp,
                color = Color.White
            )
        }

        OutlinedButton(
            onClick = onClick,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomEnd)
                .width(85.dp)
                .height(34.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Transparent,
                contentColor = Color.White
            ),
            border = BorderStroke(
                width = 1.dp,
                color = Color.White
            )
        ) {
            Text(textButton)
        }

    }
}

@Composable
@Preview
fun PilihSewaJualPreview(){
    KiosskuTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            PilihSewaJualScreen(navigate = {})
        }
    }
}