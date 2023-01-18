package com.sbfirebase.kiossku.ui.screen.submitkios.uicomponent

import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun WithError(
    errorMessage : String?,
    modifier : Modifier,
    content : @Composable () -> Unit,

){
    Column(
        modifier = modifier
    ) {
        content()
        errorMessage?.let{
            Text(
                text = it,
                modifier = Modifier
                    .align(Alignment.End),
                color = MaterialTheme.colors.error,
                fontSize = 11.sp
            )
        }
    }
}