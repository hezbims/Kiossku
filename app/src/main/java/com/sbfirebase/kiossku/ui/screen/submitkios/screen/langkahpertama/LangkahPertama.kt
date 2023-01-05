package com.sbfirebase.kiossku.ui.screen.submitkios.screen.langkahpertama

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sbfirebase.kiossku.ui.screen.submitkios.uicomponent.BackAndNextButton
import com.sbfirebase.kiossku.ui.screen.submitkios.uicomponent.DropDownDaerah
import com.sbfirebase.kiossku.ui.screen.submitkios.uicomponent.DropDownTextField
import com.sbfirebase.kiossku.ui.screen.submitkios.uicomponent.SubmitHeader
import com.sbfirebase.kiossku.ui.theme.KiosskuTheme
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun LangkahPertama(
    navigateNext : () -> Unit,
    navigateBack : () -> Unit,
    displayError : (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel : LangkahPertamaViewModel = viewModel()
){
    val uiState = viewModel.uiState.collectAsState().value
    val daerahUiState = viewModel.daerahuiState.collectAsState().value
    val onChangeData = viewModel::onDataChange
    val onLoadDaerah = viewModel::onLoadDaerah

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

            DropDownTextField(
                value = uiState.jenisProperti,
                onChangeValue = { onChangeData(TipeLangkahPertamaData.JenisProperti(it)) },
                placeholder = "Pilih jenis properti",
                itemList = remember {
                    listOf("Lapak", "Kios", "Lahan", "Gudang")
                },
            )

            var expandWaktuPembayaran by rememberSaveable {
                mutableStateOf(false)
            }

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
                            for (item in remember{listOf("/tahun" , "/bulan")})
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
                    .fillMaxWidth()
            )


            DropDownTextField(
                value = uiState.tipePenawaran,
                onChangeValue = { onChangeData(TipeLangkahPertamaData.TipePenawaran(it)) },
                placeholder = "Pilih tipe penawaran",
                itemList = remember {
                    listOf("Fix", "Nego")
                }
            )

            DropDownDaerah(
                value = uiState.provinsi,
                onValueChange = { onChangeData(TipeLangkahPertamaData.Provinsi(it)) },
                onLoadData = { onLoadDaerah(TipeLoadedDaerah.Provinsi) },
                response = daerahUiState.provinsi,
                placeholder = "provinsi"
            )

            DropDownDaerah(
                value = uiState.kabupaten,
                onValueChange = { onChangeData(TipeLangkahPertamaData.Kabupaten(it)) },
                onLoadData = {
                    onLoadDaerah(TipeLoadedDaerah.Kabupaten(uiState.provinsi?.id ?: ""))
                 },
                response = daerahUiState.kabupaten,
                placeholder = "kabupaten"
            )

            DropDownDaerah(
                value = uiState.kecamatan,
                onValueChange = { onChangeData(TipeLangkahPertamaData.Kecamatan(it)) },
                onLoadData = {
                    onLoadDaerah(TipeLoadedDaerah.Kecamatan(uiState.kabupaten?.id ?: ""))
                },
                response = daerahUiState.kecamatan,
                placeholder = "kecamatan"
            )

            DropDownDaerah(
                value = uiState.kelurahan,
                onValueChange = { onChangeData(TipeLangkahPertamaData.Kelurahan(it)) },
                onLoadData = {
                    onLoadDaerah(TipeLoadedDaerah.Kelurahan(uiState.kecamatan?.id ?: ""))
                },
                response = daerahUiState.kelurahan,
                placeholder = "kelurahan"
            )

            BackAndNextButton(
                navigateNext = navigateNext,
                navigateBack = navigateBack,
                verificationData = listOf(
                    Pair("Judul promosi", uiState.judulPromosi.isEmpty()),
                    Pair("Jenis properti", uiState.jenisProperti.isEmpty()),
                    Pair("Harga", uiState.harga.isEmpty()),
                    Pair("Tipe penawaran", uiState.tipePenawaran.isEmpty()),
                    Pair("Provinsi", uiState.provinsi == null),
                    Pair("Kabupaten", uiState.kabupaten == null),
                    Pair("Kecamatan", uiState.kecamatan == null),
                    Pair("Kelurahan", uiState.kelurahan == null)
                ),
                displayError = displayError,
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
                displayError = {}
            )
        }
    }
}