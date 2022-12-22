package com.sbfirebase.kiossku.submitkios.uicomponent

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NavigateNext
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sbfirebase.kiossku.ui.theme.GreenKiossku
import com.sbfirebase.kiossku.ui.theme.KiosskuTheme

@Composable
fun BackAndNextButton(
    navigateNext : () -> Unit,
    navigateBack : () -> Unit,
    modifier: Modifier = Modifier,
    nextText : String = "Lanjut",
    backText : String = "Kembali"

){
    Row(
        horizontalArrangement = Arrangement.spacedBy(
            space = 15.dp,
            alignment = Alignment.CenterHorizontally
        ),
        modifier = modifier.fillMaxWidth()
    ){
        Button(
            onClick = navigateBack,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.White
            ),
            border = BorderStroke(
                width = 0.5.dp,
                color = Color.Black
            ),
            modifier = Modifier
                .weight(1f)
                .height(50.dp)
        ){
            Text(backText)
        }

        Button(
            onClick = navigateNext,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = GreenKiossku,
                contentColor = Color.White
            ),
            modifier = Modifier
                .weight(1f)
                .height(50.dp)
        ){
            Text(nextText)
            Icon(
                imageVector = Icons.Outlined.NavigateNext,
                contentDescription = null,
                modifier = Modifier.padding(start = 10.dp)
            )
        }
    }
}

@Composable
@Preview
fun BackAndNextButtonPreview(){
    KiosskuTheme{
        Surface {
            BackAndNextButton(
                navigateNext = {},
                navigateBack = {},
                nextText = "Lanjut",
                backText = "Kembali"
            )
        }
    }
}