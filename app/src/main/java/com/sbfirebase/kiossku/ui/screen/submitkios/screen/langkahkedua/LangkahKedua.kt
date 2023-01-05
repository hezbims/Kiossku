package com.sbfirebase.kiossku.ui.screen.submitkios.screen.langkahkedua

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sbfirebase.kiossku.ui.screen.submitkios.uicomponent.BackAndNextButton
import com.sbfirebase.kiossku.ui.screen.submitkios.uicomponent.SubmitHeader
import com.sbfirebase.kiossku.ui.theme.KiosskuTheme

@Composable
fun LangkahKedua(
    uiState : LangkahDuaUiState,
    onChangeLuasLahan : (String) -> Unit,
    onChangePanjang : (String) -> Unit,
    onChangeLebar : (String) -> Unit,
    onChangeLuasBangunan : (String) -> Unit,
    onChangeJumlahLantai : (String) -> Unit,
    onChangeKapasitasListrik : (String) -> Unit,
    onChangeFasilitas : (String) -> Unit,
    onChangeDeskripsi : (String) -> Unit,
    navigateNext : () -> Unit,
    navigateBack : () -> Unit,
    displayError : (String) -> Unit
){
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .padding(
                top = 16.dp,
                start = 24.dp,
                end = 24.dp,
                bottom = 48.dp
            )
    ) {
        SubmitHeader(langkah = 2)

        val meterKuadrat = remember{
            buildAnnotatedString {
                append("m")
                withStyle(
                    SpanStyle(
                        baselineShift = BaselineShift.Superscript,
                        fontSize = 11.sp
                    )
                ){
                    append("2")
                }
            }
        }

        val textFieldShape = RoundedCornerShape(16.dp)
        val textFieldModifier = Modifier.fillMaxWidth()

        OutlinedTextField(
            value = uiState.luasLahan,
            onValueChange = onChangeLuasLahan,
            trailingIcon = {
                Text(meterKuadrat)
            },
            placeholder = {
                Text("Luas lahan")
            },
            modifier = textFieldModifier
                .padding(top = 24.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 50.dp,
                    top = 16.dp
                ),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = uiState.panjang,
                onValueChange = onChangePanjang,
                modifier = Modifier.weight(1f),
                trailingIcon = {
                    Text(meterKuadrat)
                },
                placeholder = {
                    Text("Panjang")
                }
            )
            OutlinedTextField(
                value = uiState.lebar,
                onValueChange = onChangeLebar,
                modifier = Modifier.weight(1f),
                trailingIcon = {
                    Text(meterKuadrat)
                },
                placeholder = {
                    Text("Lebar")
                }
            )
        }
        
        OutlinedTextField(
            value = uiState.luasBangunan,
            onValueChange = onChangeLuasBangunan,
            trailingIcon = {
                Text(meterKuadrat)
            },
            placeholder = {
                Text("Luas bangunan")
            },
            modifier = textFieldModifier
                .padding(top = 16.dp)
        )

        OutlinedTextField(
            value = uiState.jumlahLantai,
            onValueChange = onChangeJumlahLantai,
            placeholder = {
                Text("Jumlah lantai")
            },
            modifier = textFieldModifier
                .padding(top = 16.dp)
        )

        OutlinedTextField(
            value = uiState.kapasitasListrik,
            onValueChange = onChangeKapasitasListrik,
            trailingIcon = {
                Text("Kwh")
            },
            placeholder = {
                Text("Kapasitas listrik")
            },
            modifier = textFieldModifier
                .padding(top = 16.dp)
        )
        
        OutlinedTextField(
            value = uiState.fasilitas,
            onValueChange = onChangeFasilitas,
            placeholder = {
                Text("Fasilitas")
            },
            modifier = textFieldModifier
                .padding(top = 16.dp)
        )
        
        OutlinedTextField(
            value = uiState.deskripsi,
            onValueChange = onChangeDeskripsi,
            placeholder = {
                Text("Deskripsi")
            },
            modifier = textFieldModifier
                .padding(top = 16.dp)
        )

        BackAndNextButton(
            navigateNext = navigateNext,
            navigateBack = navigateBack,
            modifier = Modifier
                .padding(
                    top = 36.dp
                ),
            displayError = displayError,
            verificationData = listOf(
                Pair("Luas lahan" , uiState.luasLahan.isEmpty()),
                Pair("Panjang" , uiState.panjang.isEmpty()),
                Pair("Lebar" , uiState.lebar.isEmpty()),
                Pair("Luas bangunan" , uiState.luasBangunan.isEmpty()),
                Pair("Jumlah lantai" , uiState.jumlahLantai.isEmpty()),
                Pair("Kapasitas listrik" , uiState.kapasitasListrik.isEmpty())
            )
        )
    }
}

@Composable
@Preview
fun LangkahKeduaPreview(){
    KiosskuTheme {
        Surface(modifier = Modifier.fillMaxSize()){
            LangkahKedua(
                uiState = LangkahDuaUiState(),
                onChangeDeskripsi = {},
                onChangeFasilitas = {},
                onChangeJumlahLantai = {},
                onChangeKapasitasListrik = {},
                onChangeLebar = {},
                onChangeLuasBangunan = {},
                onChangeLuasLahan = {},
                onChangePanjang = {},
                navigateNext = {},
                navigateBack = {},
                displayError = {}
            )
        }
    }
}