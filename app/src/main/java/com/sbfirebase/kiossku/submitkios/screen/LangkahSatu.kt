package com.sbfirebase.kiossku.submitkios.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sbfirebase.kiossku.submitkios.enumdata.ElemenProperti
import com.sbfirebase.kiossku.submitkios.enumdata.JenisProperti
import com.sbfirebase.kiossku.submitkios.enumdata.TipePenawaran
import com.sbfirebase.kiossku.submitkios.enumdata.WaktuPembayaran
import com.sbfirebase.kiossku.submitkios.toPair
import com.sbfirebase.kiossku.submitkios.uicomponent.*
import com.sbfirebase.kiossku.ui.theme.KiosskuTheme

@Composable
fun LangkahSatu(
    judulPromosi : ElemenProperti<String>,
    jenisProperti : ElemenProperti<JenisProperti>,
    harga : ElemenProperti<Long>,
    tipePenawaran : ElemenProperti<TipePenawaran>,
    waktuPembayaran : ElemenProperti<WaktuPembayaran>,
    isSewa : Boolean,

    navigateNext : () -> Unit,
    navigateBack : () -> Unit,
    displayError : (String) -> Unit,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
            .padding(
                top = 16.dp,
                start = 24.dp,
                end = 24.dp,
                bottom = 48.dp
            )
    ) {
        SubmitHeader(langkah = 1)

        NormalStringTextField(
            elemenProperti = judulPromosi,
            modifier = Modifier
                .padding(top = 16.dp)
        )

        DropDownTextField(
            elemenProperti = jenisProperti,
            itemList = JenisProperti.values().asList(),
            modifier = Modifier
                .padding(top = 16.dp)
        )

        var expandWaktuPembayaran by rememberSaveable{
            mutableStateOf(false)
        }
        NormalLongTextField(
            elemenProperti = harga,
            trailingIcon = if (isSewa){
                {
                    DropdownMenu(
                        expanded = expandWaktuPembayaran,
                        onDismissRequest = {
                            expandWaktuPembayaran = false
                        }
                    ) {
                        for (item in WaktuPembayaran.values())
                            DropdownMenuItem(onClick = {
                                waktuPembayaran.setValue(item)
                                expandWaktuPembayaran = false
                            }) {
                                Text(item.toString())
                            }
                    }

                    Row (
                        modifier = Modifier.clickable {
                            expandWaktuPembayaran = !expandWaktuPembayaran
                        }
                    ) {
                        Text(
                            waktuPembayaran.getValue()!!.toString(),
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
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.AttachMoney,
                    contentDescription = null
                )
            },
            modifier = Modifier
                .padding(top = 16.dp)
        )


        DropDownTextField(
            elemenProperti = tipePenawaran,
            itemList = TipePenawaran.values().asList(),
            modifier = Modifier
                .padding(top = 16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxHeight()
        ) {
            BackAndNextButton(
                navigateNext = navigateNext,
                navigateBack = navigateBack,
                verificationData = listOf(
                    toPair(judulPromosi),
                    toPair(jenisProperti),
                    toPair(harga),
                    toPair(tipePenawaran)
                ),
                displayError = displayError,
                modifier = Modifier
                    .align(Alignment.Bottom)
            )
        }


    }
}

@Composable
@Preview
fun LangkahSatuPreview(){
    KiosskuTheme {
        Surface(modifier = Modifier.fillMaxSize()){
            LangkahSatu(
                judulPromosi = ElemenProperti("Judul promosi" , ""),
                jenisProperti = ElemenProperti("Pilih jenis properti" , ""),
                harga = ElemenProperti("Harga" , ""),
                waktuPembayaran = ElemenProperti("-", "", defValue = WaktuPembayaran.TAHUNAN),
                isSewa = true,
                tipePenawaran = ElemenProperti("Pilih tipe penawaran" , ""),
                navigateNext = {},
                navigateBack = {},
                displayError = {}
            )
        }
    }
}