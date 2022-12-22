package com.sbfirebase.kiossku.submitkios.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sbfirebase.kiossku.submitkios.enumdata.SistemPembayaran
import com.sbfirebase.kiossku.ui.theme.KiosskuTheme

@Composable
fun PilihSewaJual(
    setSistemPembayaran : (SistemPembayaran) -> Unit,
    navigate : () -> Unit,
    modifier: Modifier = Modifier
){
    Row(horizontalArrangement = Arrangement.spacedBy(15.dp)){
        Button(
            onClick = {
                setSistemPembayaran(SistemPembayaran.SEWA)
                navigate()
            }
        ){
            Text("Sewa")
        }
        Button(
            onClick = {
                setSistemPembayaran(SistemPembayaran.JUAL)
                navigate()
            }
        ){
            Text("Jual")
        }

    }
}

@Composable
@Preview
fun PilihSewaJualPreview(){
    KiosskuTheme {
        PilihSewaJual(setSistemPembayaran = {} , navigate = {})
    }
}