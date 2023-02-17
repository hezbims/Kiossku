package com.sbfirebase.kiossku.ui.screen.submitkios.screen.langkahketiga

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.sbfirebase.kiossku.R
import com.sbfirebase.kiossku.data.model.postproduct.PostKiosData
import com.sbfirebase.kiossku.domain.apiresponse.ApiResponse
import com.sbfirebase.kiossku.ui.screen.submitkios.screen.langkahkedua.LangkahKeduaUiState
import com.sbfirebase.kiossku.ui.screen.submitkios.screen.langkahkedua.LangkahKeduaViewModel
import com.sbfirebase.kiossku.ui.screen.submitkios.screen.langkahpertama.LangkahPertamaUiState
import com.sbfirebase.kiossku.ui.screen.submitkios.screen.langkahpertama.LangkahPertamaViewModel
import com.sbfirebase.kiossku.ui.screen.submitkios.uicomponent.BackAndNextButton
import com.sbfirebase.kiossku.ui.screen.submitkios.uicomponent.SubmitHeader
import com.sbfirebase.kiossku.ui.screen.submitkios.uicomponent.WithError
import com.sbfirebase.kiossku.ui.theme.GreenKiossku
import com.sbfirebase.kiossku.ui.theme.KiosskuTheme

@Composable
fun LangkahKetiga(
    viewModel1 : LangkahPertamaViewModel,
    viewModel2 : LangkahKeduaViewModel,
    viewModel3 : LangkahKetigaViewModel,
    navigateNext : () -> Unit,
    navigateBack : () -> Unit
){
    if (viewModel3.submitState.collectAsState().value is ApiResponse.Success) {
        viewModel3.doneNavigating()
        navigateNext()
    }

    val pickPhotoLauncher = rememberLauncherForActivityResult(
        contract = PickMultiplePhotoContract(),
        onResult = {
            viewModel3.addPhotoUris(it)
        }
    )
    val photoUris = viewModel3.photoUris

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

        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(19.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(
                vertical = 24.dp
            )
        ) {
            item {
                TambahGambar(getPhotoUri = {
                    pickPhotoLauncher.launch("image/*")
                })
            }
            items(photoUris){ item ->
                DeletableGambar(item , viewModel3::deletePhotoUri)
            }
        }

        var shouldSubmit by remember { mutableStateOf(false) }
        if (shouldSubmit){
            shouldSubmit = false
            SubmitData(
                data1 = viewModel1.uiState.collectAsState().value,
                data2 = viewModel2.uiState.collectAsState().value,
                data3 = photoUris,
                submitData = viewModel3::submitData
            )
        }

        WithError(
            errorMessage = if (photoUris.isEmpty()) "Upload minimal 1 foto" else null,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            BackAndNextButton(
                onClickNext = { if (photoUris.isNotEmpty()) shouldSubmit = true },
                onClickBack = navigateBack
            )
        }
    }
}

@Composable
fun TambahGambar(
    getPhotoUri : () -> Unit,
    modifier : Modifier = Modifier
){
    Box(
        modifier = modifier
            .width(270.dp)
            .height(241.dp)
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
            .width(270.dp)
            .height(241.dp)
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

        uri.toString :
        content://com.android.providers.media-.documents/document/image%3A5368
         */
    }
}

@Composable
private fun SubmitData(
    data1 : LangkahPertamaUiState,
    data2 : LangkahKeduaUiState,
    data3 : List<UriWithId>,
    submitData : (PostKiosData) -> Unit
){
    submitData(
        PostKiosData(
            judulPromosi = data1.judulPromosi,
            tipeProperti = data1.jenisProperti,
            harga = data1.harga.toInt(),
            waktuPembayaran = data1.waktuPembayaran,
            fixNego = data1.tipePenawaran,
            sewaJual = if (data1.isSewa) "sewa" else "jual",
            lokasi = stringResource(
                R.string.format_lokasi,
                data1.provinsi!!.nama,
                data1.kabupaten!!.nama,
                data1.kecamatan!!.nama,
                data1.kelurahan!!.nama
            ),
            luasLahan = data2.luasLahan.toInt(),
            luasBangunan = data2.luasBangunan.toInt(),
            tingkat = data2.jumlahLantai.toInt(),
            kapasitasListrik = data2.kapasitasListrik.toInt(),
            alamat = "Field alamat tidak ada",
            deskripsi = data2.deskripsi,
            fasilitas = "air",
            panjang = data2.panjang.toInt(),
            lebar = data2.lebar.toInt(),
            images = data3.map{ it.uri }
        )
    )
}

@Composable
@Preview
fun LangkahKetigaPreview(){
    KiosskuTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            LangkahKetiga(
                viewModel1 = hiltViewModel(),
                viewModel2 = hiltViewModel(),
                viewModel3 = hiltViewModel(),
                navigateNext = {

                },
                navigateBack = {

                }
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