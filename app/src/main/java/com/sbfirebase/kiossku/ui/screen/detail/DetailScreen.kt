package com.sbfirebase.kiossku.ui.screen.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.sbfirebase.kiossku.domain.apiresponse.AuthorizedApiResponse
import com.sbfirebase.kiossku.domain.model.KiosData
import com.sbfirebase.kiossku.ui.theme.GreenKiossku
import com.sbfirebase.kiossku.ui.theme.KiosskuTheme

@Composable
fun DetailScreen(
    navController : NavHostController,
    viewModel : DetailViewModel = hiltViewModel()
){
    DetailScreen(
        data = viewModel.staticData,
        uiState = viewModel.uiState.collectAsState().value,
        onEvent = viewModel::onEvent,
        navigateBack = { navController.popBackStack() }
    )
}

@Composable
private fun DetailScreen(
    data : DetailStaticData,
    uiState: DetailScreenUiState,
    onEvent : (DetailScreenEvent) -> Unit,
    navigateBack : () -> Unit
){
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        DetailTopBar(
            navigateBack = navigateBack,
            modifier = Modifier
                .padding(start = 24.dp , end = 24.dp , top = 4.dp)
        )

        FotoFotoProperti(data.images)
        DetailKiosData(
            uiState = uiState,
            kiosData = data.product,
            onEvent = onEvent
        )
    }
}

@Composable
fun DetailKiosData(
    uiState: DetailScreenUiState,
    kiosData: KiosData,
    onEvent: (DetailScreenEvent) -> Unit,
    modifier: Modifier = Modifier
){
    if (uiState.shouldOpenWhatsapp)
        onEvent(DetailScreenEvent.OpenWhatsapp)

    Column(
        modifier = modifier
            .padding(horizontal = 24.dp)
    ) {
        Text(
            text = kiosData.jenisProperti,
            color = GreenKiossku,
            fontSize = 14.sp
        )

        Text(
            text = kiosData.judulPromosi,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
        ){
            Text(
                text = kiosData.harga.toString(),
                fontWeight = FontWeight.Bold,
                color = GreenKiossku
            )

            Button(
                onClick = { } ,
                shape = RoundedCornerShape(5.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = GreenKiossku,
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier
                    .height(21.dp)
                    .width(73.dp)
            ) {
                Text(
                    text = kiosData.negoFix,
                    fontSize = 10.sp
                )
            }
        }

        Card(
            modifier = Modifier
                .padding(vertical = 24.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(0.5.dp , color = Color.Black)
        ){
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(
                    start = 32.dp,
                    top = 32.dp,
                    bottom = 32.dp
            )
            ){
                IconWithData(
                    icon = Icons.Outlined.HomeWork,
                    dataText = "Luas bangunan = ${kiosData.luasBangunan}"
                )
                
                IconWithData(
                    icon = Icons.Outlined.SettingsOverscan,
                    dataText = "Luas lahan = ${kiosData.luasLahan}"
                )
                IconWithData(
                    icon = Icons.Outlined.ElectricBolt,
                    dataText = "Kapasitas listrik = ${kiosData.kapasitasListrik} Watt"
                )
            }
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            IconWithData(
                icon = Icons.Outlined.LocationOn,
                dataText = kiosData.alamat
            )

            if (kiosData.fasilitas?.isNotEmpty() == true)
                IconWithData(
                    icon = Icons.Outlined.RealEstateAgent,
                    dataText = kiosData.fasilitas
                )

            /*IconWithData(
                icon = Icons.Outlined.Contacts,
                dataText =
            )*/
        }

        Button(
            onClick = { onEvent(DetailScreenEvent.GetNomorTelepon) },
            enabled = uiState.getTeleponResponse !is AuthorizedApiResponse.Loading,
            modifier = Modifier
                .padding(top = 32.dp)
                .height(50.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        ) {
            if (uiState.getTeleponResponse is AuthorizedApiResponse.Loading)
                CircularProgressIndicator()
            else
                Text("Hubungi penjual")
        }
    }

}

@Composable
fun IconWithData(
    icon : ImageVector,
    dataText : String,
    modifier: Modifier = Modifier
){
    Row(
        horizontalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier
    ){
        Icon(imageVector = icon, contentDescription = null , tint = GreenKiossku)
        Text(dataText , Modifier.padding(start = 18.dp))
    }
}

@Composable
fun FotoFotoProperti(
    images : List<String>,
    modifier: Modifier = Modifier
){
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        modifier = modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 24.dp),
    ){
        items(images){
            FotoPropertiItem(imageUri = "https://kiossku.com/uploads/$it")
        }
    }
}

@Composable
fun FotoPropertiItem(
    imageUri : String,
    modifier: Modifier = Modifier
){

    Card {
        SubcomposeAsyncImage(
            model = imageUri,
            contentDescription = "Foto properti",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(270.dp)
                .height(241.dp)
                .clip(RoundedCornerShape(12.dp))
        ) {
            when (painter.state) {
                is AsyncImagePainter.State.Loading,
                AsyncImagePainter.State.Empty ->
                    Box {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(48.dp)
                                .align(Alignment.Center)
                        )
                    }
                is AsyncImagePainter.State.Error ->
                    Box {
                        Icon(
                            imageVector = Icons.Outlined.WifiOff,
                            contentDescription = "Internet failed",
                            modifier = Modifier
                                .size(48.dp)
                                .align(Alignment.Center)
                        )
                    }
                is AsyncImagePainter.State.Success ->
                    SubcomposeAsyncImageContent()
            }
        }
    }
}
@Composable
fun DetailTopBar(
    navigateBack: () -> Unit,
    modifier : Modifier = Modifier
){
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        Card {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier
                        .padding(12.dp)
                        .size(24.dp)
                        .clickable { navigateBack() }
                )
            }
        }

        Text(
            text = "Detail Properti",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )

        Card(
            shape = CircleShape,
            backgroundColor = Color(0x33118E49)
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = "Add to wish list",
                    modifier = Modifier
                        .padding(12.dp)
                        .size(24.dp),
                    tint = GreenKiossku
                )
            }
        }
    }
}

@Composable
@Preview
fun DetailTopBarPreview(){
    KiosskuTheme {
        Surface(modifier = Modifier.fillMaxWidth()){
            DetailTopBar(
                navigateBack = {}
            )
        }
    }
}

@Composable
@Preview
fun DetailKiosDataPreview(){
    KiosskuTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            DetailKiosData(kiosData = KiosData(
                images = "",
                jenisProperti = "Lahan",
                judulPromosi = "Dijual Kios di Puri Park View" +
                        " Apartemen Tower C – Lokasi Strategis",
                kapasitasListrik = 900,
                lebar = 3,
                panjang = 3,
                lokasi = "Peguyanngan Kangin, Denpasar Utara, Denpasar, Bali",
                luasBangunan = 9,
                luasLahan = 9,
                negoFix = "Fix",
                productId = 1,
                sistemSewaJual = "Jual",
                status = -1,
                harga = 250000,
                tahunanBulanan = "",
                tingkat = 1,
                userId = 3,
                alamat = "Jl Pertulaka no 32 Q",
                deskripsi = "",
                fasilitas = "2 Kamar mandi, 2 Kamar tidur"
                ),
                onEvent = {},
                uiState = DetailScreenUiState()
            )
        }
    }
}

@Composable
@Preview
fun FotoPropertiItemPreview(){
    KiosskuTheme {
        Surface{
            FotoPropertiItem(imageUri = "C:\\Users\\hezbi\\Documents\\absen_agile.png")
        }
    }
}