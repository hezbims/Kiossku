package com.sbfirebase.kiossku.ui.screen.submitkios.screen.langkahkedua

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sbfirebase.kiossku.ui.screen.submitkios.uicomponent.BackAndNextButton
import com.sbfirebase.kiossku.ui.screen.submitkios.uicomponent.SubmitHeader
import com.sbfirebase.kiossku.ui.screen.submitkios.uicomponent.WithError
import com.sbfirebase.kiossku.ui.theme.KiosskuTheme

@Composable
fun LangkahKedua(
    viewModel : LangkahKeduaViewModel,
    navigateNext : () -> Unit,
    navigateBack : () -> Unit
){
    val uiState = viewModel.uiState.collectAsState().value
    if (viewModel.canNavigate.value)
        navigateNext()

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
        val numberKeyboard = KeyboardOptions(
            keyboardType = KeyboardType.Number
        )

        WithError(
            errorMessage = uiState.luasLahanError,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                value = uiState.luasLahan,
                onValueChange = viewModel::onChangeLuasLahan,
                trailingIcon = {
                    Text(meterKuadrat)
                },
                placeholder = {
                    Text("Luas lahan")
                },
                modifier = textFieldModifier
                    .padding(top = 24.dp),
                shape = textFieldShape,
                keyboardOptions = numberKeyboard
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 50.dp,
                    top = 16.dp
                ),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            WithError(
                errorMessage = uiState.panjangError ,
                modifier = Modifier
                    .weight(1f)
            ) {
                OutlinedTextField(
                    value = uiState.panjang,
                    onValueChange = viewModel::onChangePanjang,
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        Text(meterKuadrat)
                    },
                    placeholder = {
                        Text("Panjang")
                    },
                    keyboardOptions = numberKeyboard
                )
            }
            
            WithError(
                errorMessage = uiState.lebarError,
                modifier = Modifier
                    .weight(1f)
            ) {
                OutlinedTextField(
                    value = uiState.lebar,
                    onValueChange = viewModel::onChangeLebar,
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        Text(meterKuadrat)
                    },
                    placeholder = {
                        Text("Lebar")
                    },
                    keyboardOptions = numberKeyboard
                )
            }
        }
        
        WithError(
            errorMessage = uiState.luasBangunanError,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                value = uiState.luasBangunan,
                onValueChange = viewModel::onChangeLuasBangunan,
                trailingIcon = {
                    Text(meterKuadrat)
                },
                placeholder = {
                    Text("Luas bangunan")
                },
                modifier = textFieldModifier
                    .padding(top = 16.dp),
                keyboardOptions = numberKeyboard
            )
        }

        WithError(
            errorMessage = uiState.jumlahLantaiError,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                value = uiState.jumlahLantai,
                onValueChange = viewModel::onChangeJumlahLantai,
                placeholder = {
                    Text("Jumlah lantai")
                },
                modifier = textFieldModifier
                    .padding(top = 16.dp),
                keyboardOptions = numberKeyboard
            )
        }

        WithError(
            errorMessage = uiState.kapasitasListrikError,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                value = uiState.kapasitasListrik,
                onValueChange = viewModel::onChangeKapasitasListrik,
                trailingIcon = {
                    Text("Kwh")
                },
                placeholder = {
                    Text("Kapasitas listrik")
                },
                modifier = textFieldModifier
                    .padding(top = 16.dp),
                keyboardOptions = numberKeyboard
            )
        }
        
        OutlinedTextField(
            value = uiState.fasilitas,
            onValueChange = viewModel::onChangeFasilitas,
            placeholder = {
                Text("Fasilitas")
            },
            modifier = textFieldModifier
                .padding(top = 16.dp)
        )
        
        OutlinedTextField(
            value = uiState.deskripsi,
            onValueChange = viewModel::onChangeDeskripsi,
            placeholder = {
                Text("Deskripsi")
            },
            modifier = textFieldModifier
                .padding(top = 16.dp)
        )

        BackAndNextButton(
            onClickNext = viewModel::validateData,
            onClickBack = navigateBack,
            modifier = Modifier
                .padding(
                    top = 36.dp
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
                viewModel = hiltViewModel(),
                navigateNext = {},
                navigateBack = {}
            )
        }
    }
}