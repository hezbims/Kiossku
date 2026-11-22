package com.itpens.kiossku.ui.screen.submitkios.uicomponent

import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.itpens.kiossku.R

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
                text = if (it.length == 0) stringResource(R.string.unknown_error_occur) else it,
                modifier = Modifier
                    .align(Alignment.End),
                color = MaterialTheme.colors.error,
                fontSize = 11.sp
            )
        }
    }
}