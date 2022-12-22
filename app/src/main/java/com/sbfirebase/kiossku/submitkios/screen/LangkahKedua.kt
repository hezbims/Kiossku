package com.sbfirebase.kiossku.submitkios.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sbfirebase.kiossku.submitkios.enumdata.ElemenProperti
import com.sbfirebase.kiossku.submitkios.uicomponent.BackAndNextButton
import com.sbfirebase.kiossku.submitkios.uicomponent.NormalLongTextField
import com.sbfirebase.kiossku.submitkios.uicomponent.NormalStringTextField
import com.sbfirebase.kiossku.ui.theme.KiosskuTheme

@Composable
fun LangkahKedua(
    luasLahan : ElemenProperti<Long>,
    panjang : ElemenProperti<Long>,
    lebar : ElemenProperti<Long>,
    luasBangunan : ElemenProperti<Long>,
    jumlahLantai : ElemenProperti<Long>,
    kapasitasListrik : ElemenProperti<Long>,
    fasilitas : ElemenProperti<String>,
    deskripsi : ElemenProperti<String>,
    navigateNext : () -> Unit,
    navigateBack : () -> Unit
){
    Column {
        val meterKuadrat = buildAnnotatedString {
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
        NormalLongTextField(
            elemenProperti = luasLahan,
            trailingIcon = {
                Text(meterKuadrat)
            }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 50.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            NormalLongTextField(
                elemenProperti = panjang,
                modifier = Modifier.weight(1f),
                trailingIcon = {
                    Text(meterKuadrat)
                }
            )
            NormalLongTextField(
                elemenProperti = lebar,
                modifier = Modifier.weight(1f),
                trailingIcon = {
                    Text(meterKuadrat)
                }
            )
        }
        
        NormalLongTextField(
            elemenProperti = luasBangunan,
            trailingIcon = {
                Text(meterKuadrat)
            }
        )

        NormalLongTextField(elemenProperti = jumlahLantai)

        NormalLongTextField(
            elemenProperti = kapasitasListrik,
            trailingIcon = {
                Text("Kwh")
            }
        )
        
        NormalStringTextField(elemenProperti = fasilitas)
        
        NormalStringTextField(
            elemenProperti = deskripsi
        )

        BackAndNextButton(
            navigateNext = navigateNext,
            navigateBack = navigateBack
        )
    }
}

@Composable
@Preview
fun LangkahKeduaPreview(){
    KiosskuTheme {
        Surface(modifier = Modifier.fillMaxSize()){
            LangkahKedua(
                luasLahan = ElemenProperti("Luas lahan"),
                panjang = ElemenProperti("Panjang"),
                lebar = ElemenProperti("Lebar"),
                luasBangunan = ElemenProperti("Luas bangunan"),
                jumlahLantai = ElemenProperti("Jumlah lantai"),
                kapasitasListrik = ElemenProperti("Kapasitas listrik"),
                fasilitas = ElemenProperti("Fasilitas"),
                deskripsi = ElemenProperti("Deskripsi"),
                navigateNext = {},
                navigateBack = {}
            )
        }
    }
}