package com.sbfirebase.kiossku.submitkios.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sbfirebase.kiossku.submitkios.enumdata.ElemenProperti
import com.sbfirebase.kiossku.submitkios.enumdata.JenisProperti
import com.sbfirebase.kiossku.submitkios.enumdata.TipePenawaran
import com.sbfirebase.kiossku.submitkios.enumdata.WaktuPembayaran
import com.sbfirebase.kiossku.submitkios.uicomponent.BackAndNextButton
import com.sbfirebase.kiossku.submitkios.uicomponent.DropDownTextField
import com.sbfirebase.kiossku.submitkios.uicomponent.NormalLongTextField
import com.sbfirebase.kiossku.submitkios.uicomponent.NormalStringTextField
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
    modifier: Modifier = Modifier
){
    Column(modifier = modifier) {
        NormalStringTextField(
            elemenProperti = judulPromosi
        )

        DropDownTextField(
            elemenProperti = jenisProperti,
            itemList = JenisProperti.values().asList()
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
                            contentDescription = null
                        )
                    }
                }
            } else null
        )


        DropDownTextField(
            elemenProperti = tipePenawaran,
            itemList = TipePenawaran.values().asList()
        )

        BackAndNextButton(
            navigateNext = navigateNext,
            navigateBack = navigateBack
        )


    }
}

@Composable
@Preview
fun LangkahSatuPreview(){
    KiosskuTheme {
        Surface(modifier = Modifier.fillMaxSize()){
            LangkahSatu(
                judulPromosi = ElemenProperti("Judul promosi"),
                jenisProperti = ElemenProperti("Pilih jenis properti"),
                harga = ElemenProperti("Harga"),
                waktuPembayaran = ElemenProperti("-" , defValue = WaktuPembayaran.TAHUNAN),
                isSewa = true,
                tipePenawaran = ElemenProperti("Pilih tipe penawaran"),
                navigateNext = {},
                navigateBack = {}
            )
        }
    }
}