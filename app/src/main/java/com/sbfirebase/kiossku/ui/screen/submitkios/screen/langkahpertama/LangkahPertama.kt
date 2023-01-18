package com.sbfirebase.kiossku.ui.screen.submitkios.screen.langkahpertama

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sbfirebase.kiossku.ui.screen.submitkios.uicomponent.*
import com.sbfirebase.kiossku.ui.theme.KiosskuTheme

@Composable
fun LangkahPertama(
    navigateNext : () -> Unit,
    navigateBack : () -> Unit,
    viewModel : LangkahPertamaViewModel,
    modifier: Modifier = Modifier,

){
    val uiState = viewModel.uiState.collectAsState().value
    val daerahUiState = viewModel.daerahuiState.collectAsState().value
    val onChangeData = viewModel::onDataChange
    val onLoadDaerah = viewModel::onLoadDaerah

    if (viewModel.canNavigate.value)
        navigateNext()

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(
                top = 16.dp,
                start = 24.dp,
                end = 24.dp,
                bottom = 48.dp
            )
    ) {
        SubmitHeader(langkah = 1)

        val textFieldShape = RoundedCornerShape(16.dp)

        Column(
            modifier = Modifier
                .padding(
                    top = 24.dp,
                    bottom = 32.dp
                ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            WithError(
                errorMessage = uiState.judulPromosiError,
                modifier = Modifier
                    .fillMaxWidth()
            ){
                OutlinedTextField(
                    value = uiState.judulPromosi,
                    onValueChange = { onChangeData(TipeLangkahPertamaData.JudulPromosi(it)) },
                    placeholder = {
                        Text("Judul promosi")
                    },
                    shape = textFieldShape,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }

            DropDownTextField(
                value = uiState.jenisProperti,
                onChangeValue = { onChangeData(TipeLangkahPertamaData.JenisProperti(it)) },
                placeholder = "Pilih jenis properti",
                itemList = remember {
                    listOf("kios" , "ruko", "lapak", "lahan", "gudang")
                },
                errorMessage = uiState.jenisPropertiError
            )

            var expandWaktuPembayaran by rememberSaveable {
                mutableStateOf(false)
            }

            WithError(
                errorMessage = uiState.hargaError,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    placeholder = {
                        Text("Harga")
                    },
                    shape = textFieldShape,
                    value = uiState.harga,
                    onValueChange = { onChangeData(TipeLangkahPertamaData.Harga(it)) },
                    trailingIcon = if (uiState.isSewa) {
                        {
                            DropdownMenu(
                                expanded = expandWaktuPembayaran,
                                onDismissRequest = {
                                    expandWaktuPembayaran = false
                                }
                            ) {
                                for (item in remember { listOf("tahunan", "bulanan") })
                                    DropdownMenuItem(onClick = {
                                        onChangeData(
                                            TipeLangkahPertamaData.WaktuPembayaran(item)
                                        )
                                        expandWaktuPembayaran = false
                                    }) {
                                        Text(item)
                                    }
                            }

                            Row(
                                modifier = Modifier.clickable {
                                    expandWaktuPembayaran = !expandWaktuPembayaran
                                }
                            ) {
                                Text(
                                    text = uiState.waktuPembayaran,
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                )
                                Icon(
                                    imageVector =
                                    if (expandWaktuPembayaran)
                                        Icons.Outlined.ExpandLess
                                    else
                                        Icons.Outlined.ExpandMore,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .padding(end = 14.dp)
                                )
                            }
                        }
                    } else null,
                    modifier = Modifier
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )
            }


            DropDownTextField(
                value = uiState.tipePenawaran,
                onChangeValue = { onChangeData(TipeLangkahPertamaData.TipePenawaran(it)) },
                placeholder = "Pilih tipe penawaran",
                itemList = remember {
                    listOf("fix", "nego")
                },
                errorMessage = uiState.tipePenawaranError
            )

            DropDownDaerah(
                value = uiState.provinsi,
                onValueChange = { onChangeData(TipeLangkahPertamaData.Provinsi(it)) },
                onLoadData = { onLoadDaerah(TipeLoadedDaerah.Provinsi) },
                response = daerahUiState.provinsi,
                placeholder = "provinsi",
                errorMessage = uiState.provinsiError
            )

            DropDownDaerah(
                value = uiState.kabupaten,
                onValueChange = { onChangeData(TipeLangkahPertamaData.Kabupaten(it)) },
                onLoadData = {
                    onLoadDaerah(TipeLoadedDaerah.Kabupaten(uiState.provinsi?.id ?: ""))
                 },
                response = daerahUiState.kabupaten,
                placeholder = "kabupaten",
                errorMessage = uiState.kabupatenError
            )

            DropDownDaerah(
                value = uiState.kecamatan,
                onValueChange = { onChangeData(TipeLangkahPertamaData.Kecamatan(it)) },
                onLoadData = {
                    onLoadDaerah(TipeLoadedDaerah.Kecamatan(uiState.kabupaten?.id ?: ""))
                },
                response = daerahUiState.kecamatan,
                placeholder = "kecamatan",
                errorMessage = uiState.kecamatanError
            )

            DropDownDaerah(
                value = uiState.kelurahan,
                onValueChange = { onChangeData(TipeLangkahPertamaData.Kelurahan(it)) },
                onLoadData = {
                    onLoadDaerah(TipeLoadedDaerah.Kelurahan(uiState.kecamatan?.id ?: ""))
                },
                response = daerahUiState.kelurahan,
                placeholder = "kelurahan",
                errorMessage = uiState.kelurahanError
            )

            BackAndNextButton(
                onClickBack = navigateBack,
                onClickNext = viewModel::validate,
            )
        }



    }
}

@Composable
@Preview
fun LangkahPertamaPreview(){
    KiosskuTheme {
        Surface(modifier = Modifier.fillMaxSize()){
            LangkahPertama(
                navigateNext = {},
                navigateBack = {},
                viewModel = hiltViewModel()
            )
        }
    }
}