package com.sbfirebase.kiossku.ui.screen.submitkios.screen.langkahkedua

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.RadioButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sbfirebase.kiossku.route.AllRoute
import com.sbfirebase.kiossku.ui.screen.submitkios.uicomponent.BackAndNextButton
import com.sbfirebase.kiossku.ui.screen.submitkios.uicomponent.SubmitHeader
import com.sbfirebase.kiossku.ui.screen.submitkios.uicomponent.WithError
import com.sbfirebase.kiossku.ui.theme.KiosskuTheme


@Composable
fun LangkahKedua(
    viewModel : LangkahKeduaViewModel,
    navController : NavController
){
    val uiState = viewModel.uiState.collectAsState().value
    if (uiState.navigateNext){
        navController.navigate(AllRoute.SubmitKios.LangkahKetiga.root)
        viewModel.onEvent(LangkahKeduaScreenEvent.OnDoneNavigating)
    }
    else
        LangkahKedua(
            uiState = uiState,
            onEvent = viewModel::onEvent,
            navigateBack = { navController.popBackStack() }
        )
}

@Composable
private fun LangkahKedua(
    uiState : LangkahKeduaUiState,
    onEvent : (LangkahKeduaScreenEvent) -> Unit,
    navigateBack : () -> Unit
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
                onValueChange = {
                    onEvent(LangkahKeduaScreenEvent.OnChangeLuasLahan(it))
                },
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
                    onValueChange = {
                        onEvent(LangkahKeduaScreenEvent.OnChangePanjang(it))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        Text(meterKuadrat)
                    },
                    placeholder = {
                        Text("Panjang")
                    },
                    shape = textFieldShape,
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
                    onValueChange = {
                        onEvent(LangkahKeduaScreenEvent.OnChangeLebar(it))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        Text(meterKuadrat)
                    },
                    placeholder = {
                        Text("Lebar")
                    },
                    shape = textFieldShape,
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
                onValueChange = {
                    onEvent(LangkahKeduaScreenEvent.OnChangeLuasBangunan(it))
                },
                trailingIcon = {
                    Text(meterKuadrat)
                },
                placeholder = {
                    Text("Luas bangunan")
                },
                shape = textFieldShape,
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
                onValueChange = {
                    onEvent(LangkahKeduaScreenEvent.OnChangeJumlahLantai(it))
                },
                placeholder = {
                    Text("Jumlah lantai")
                },
                shape = textFieldShape,
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
                onValueChange = {
                    onEvent(LangkahKeduaScreenEvent.OnChangeKapasitasListrik(it))
                },
                trailingIcon = {
                    Text("Kwh")
                },
                placeholder = {
                    Text("Kapasitas listrik")
                },
                shape = textFieldShape,
                modifier = textFieldModifier
                    .padding(top = 16.dp),
                keyboardOptions = numberKeyboard
            )
        }
        
        OutlinedTextField(
            value = uiState.deskripsi,
            onValueChange = {
                onEvent(LangkahKeduaScreenEvent.OnChangeDeskripsi(it))
            },
            placeholder = {
                Text("Deskripsi")
            },
            shape = textFieldShape,
            modifier = textFieldModifier
                .padding(top = 16.dp)
        )

        Text(
            text = "Fasilitas :",
            modifier = Modifier
                .padding(
                    top = 16.dp,
                    start = 14.dp
                )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(1f)
            ) {
                RadioButton(
                    selected = uiState.fasilitas == "air",
                    onClick = {
                        onEvent(LangkahKeduaScreenEvent.OnChangeFasilitas("air"))
                    }
                )
                Text("air")
            }
            Row(
                verticalAlignment = Alignment.CenterVertically ,
                modifier = Modifier
                    .weight(1f)
            ) {
                RadioButton(
                    selected = uiState.fasilitas == "listrik",
                    onClick = {
                        onEvent(LangkahKeduaScreenEvent.OnChangeFasilitas("listrik"))
                    }
                )
                Text("listrik")
            }
        }



        BackAndNextButton(
            onClickNext = {
                onEvent(LangkahKeduaScreenEvent.OnValidateData)
            },
            onClickBack = navigateBack,
            isLoading = uiState.isValidatingData,
            modifier = Modifier
                .padding(top = 36.dp)
        )
    }
}

@Composable
@Preview
private fun LangkahKeduaPreview(){
    KiosskuTheme {
        Surface(modifier = Modifier.fillMaxSize()){
            LangkahKedua(
                uiState = LangkahKeduaUiState(),
                onEvent = {},
                navigateBack = {}
            )
        }
    }
}