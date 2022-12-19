package com.sbfirebase.kiossku.submitkios

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sbfirebase.kiossku.ui.theme.KiosskuTheme

@Composable
fun PilihSewaJual(){
    Row(horizontalArrangement = Arrangement.spacedBy(15.dp)){
        Button(
            onClick = {}
        ){
            Text("Sewa")
        }
        Button(
            onClick = {}
        ){
            Text("Jual")
        }

    }
}

@Composable
@Preview
fun PilihSewaJualPreview(){
    KiosskuTheme {
        PilihSewaJual()
    }
}