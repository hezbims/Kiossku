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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.sbfirebase.kiossku.domain.apiresponse.ApiResponse
import com.sbfirebase.kiossku.route.AllRoute
import com.sbfirebase.kiossku.ui.screen.submitkios.screen.finalsubmit.FinalSubmitEvent
import com.sbfirebase.kiossku.ui.screen.submitkios.screen.finalsubmit.FinalSubmitViewModel
import com.sbfirebase.kiossku.ui.screen.submitkios.screen.langkahkedua.LangkahKeduaViewModel
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
    navController : NavHostController,
    finalSubmitViewModel : FinalSubmitViewModel = hiltViewModel()
){
    val uiState1 = viewModel1.uiState.collectAsState().value
    val uiState2 = viewModel2.uiState.collectAsState().value
    val uriWithIds = viewModel3.photoUriWithId

    val submitState = finalSubmitViewModel.submitState.collectAsState().value

    if (submitState is ApiResponse.Success) {
        navController.navigate(AllRoute.SubmitKios.SubmitDataSucceed.root){
            popUpTo(0){
                inclusive = true
            }
        }
        finalSubmitViewModel.onEvent(FinalSubmitEvent.OnDoneNavigating)
    }

    else
        LangkahKetiga(
            uriWithIds = viewModel3.photoUriWithId,
            onPhotoEvent = viewModel3::onEvent,
            onStartSubmit = {
                finalSubmitViewModel.onEvent(
                    FinalSubmitEvent.OnStartSubmitData(
                        data1 = uiState1,
                        data2 = uiState2,
                        uriWithIds = uriWithIds
                    )
                )
            },
            isSubmitting = submitState is ApiResponse.Loading,
            navigateBack = {
                navController.popBackStack()
            }
        )
}
@Composable
fun LangkahKetiga(
    uriWithIds : List<UriWithId>,
    onPhotoEvent : (LangkahKetigaScreenEvent) -> Unit,
    onStartSubmit : () -> Unit,
    isSubmitting : Boolean,
    navigateBack : () -> Unit
){
    val pickPhotoLauncher = rememberLauncherForActivityResult(
        contract = PickMultiplePhotoContract(),
        onResult = {
            onPhotoEvent(LangkahKetigaScreenEvent.AddPhotoUris(it))
        }
    )

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
            items(items = uriWithIds){ item ->
                DeletableGambar(
                    item = item ,
                    onDeletePhoto = {
                        onPhotoEvent(LangkahKetigaScreenEvent.DeletePhotoUri(it))
                    }
                )
            }
        }

        val isPhotoEmpty = uriWithIds.isEmpty()

        WithError(
            errorMessage = if (isPhotoEmpty) "Upload minimal 1 foto" else null,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            BackAndNextButton(
                onClickNext = { if (!isPhotoEmpty) onStartSubmit() },
                onClickBack = navigateBack,
                isLoading = isSubmitting
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
    onDeletePhoto: (Int) -> Unit,
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
                onDeletePhoto(item.id)
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
@Preview
fun LangkahKetigaPreview(){
    KiosskuTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            LangkahKetiga(
                uriWithIds = emptyList(),
                onPhotoEvent = {},
                isSubmitting = false,
                navigateBack = {},
                onStartSubmit = {}
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