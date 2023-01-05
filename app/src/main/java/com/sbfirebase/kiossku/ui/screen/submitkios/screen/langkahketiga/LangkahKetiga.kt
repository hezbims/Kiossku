package com.sbfirebase.kiossku.ui.screen.submitkios.screen.langkahketiga

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.sbfirebase.kiossku.ui.screen.submitkios.uicomponent.BackAndNextButton
import com.sbfirebase.kiossku.ui.screen.submitkios.uicomponent.SubmitHeader
import com.sbfirebase.kiossku.ui.theme.GreenKiossku
import com.sbfirebase.kiossku.ui.theme.KiosskuTheme

@Composable
fun LangkahKetiga(
    getPhotoUri : () -> Unit,
    deletePhoto : (Int) -> Unit,
    photoUris : List<UriWithId>,
    navigateNext : () -> Unit,
    navigateBack : () -> Unit,
    displayError : (String) -> Unit
){
    Column(
        modifier = Modifier
            .padding(
                top = 16.dp,
                start = 24.dp,
                end = 24.dp,
                bottom = 48.dp
            )
    ){
        SubmitHeader(langkah = 3)

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(19.dp),
            verticalArrangement = Arrangement.spacedBy(19.dp),
            modifier = Modifier
                .weight(1f),
            contentPadding = PaddingValues(
                top = 24.dp
            )
        ) {
            item {
                TambahGambar(getPhotoUri)
            }
            items(photoUris){ item ->
                DeletableGambar(item , deletePhoto)
            }
        }
        BackAndNextButton(
            navigateNext = { /*TODO*/ },
            navigateBack = {
                navigateBack()
            },
            displayError = displayError,
            verificationData = listOf()
        )
    }
}

@Composable
fun TambahGambar(
    getPhotoUri : () -> Unit,
    modifier : Modifier = Modifier
){
    Box(
        modifier = modifier
            .size(154.dp)
            .clickable {
                getPhotoUri()
            },
        contentAlignment = Alignment.Center
    ){
        Canvas(modifier = Modifier.matchParentSize()) {
            drawRoundRect(
                color = Color(0xFF595552),
                style = Stroke(
                    width = 2f,
                    pathEffect = PathEffect
                        .dashPathEffect(
                            floatArrayOf(10f, 10f),
                            0f
                        )
                ),
                cornerRadius = CornerRadius(12f , 12f)
            )
        }
        Column {
            Text(
                text = "Masukkan Gambar",
                fontSize = 14.sp
            )
            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = null,
                tint = GreenKiossku,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .size(32.dp)
                    .align(Alignment.CenterHorizontally)

            )
        }
    }
}

@Composable
fun DeletableGambar(
    item : UriWithId,
    deletePhoto: (Int) -> Unit,
    modifier: Modifier = Modifier
){
    Box(
        modifier = modifier
            .size(154.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(item.uri),
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(12f))
        )
        Button(
            onClick = {
                deletePhoto(item.id)
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.DarkGray
            ),
            modifier = Modifier
                .padding(4.dp)
                .size(20.dp)
                .defaultMinSize(1.dp)
                .align(Alignment.TopEnd),
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(10.dp)
            )
        }
        /*
        uri.path :
        /document/image:5367

        uri.
        content://com.android.providers.media-.documents/document/image%3A5368
         */
    }
}

@Composable
@Preview
fun LangkahKetigaPreview(){
    KiosskuTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            LangkahKetiga(
                getPhotoUri = {

                },
                deletePhoto = {

                },
                photoUris = listOf(),
                navigateNext = {

                },
                navigateBack = {

                },
                displayError = {}
            )
        }
    }
}

@Composable
@Preview
fun TambahGambarPreview(){
    KiosskuTheme{
        Surface {
            TambahGambar(
                getPhotoUri = {

                }
            )
        }
    }
}

/*
@Composable
@Preview
fun DeletableGambarPreview(){
    KiosskuTheme{
        DeletableGambar()
    }
}*/