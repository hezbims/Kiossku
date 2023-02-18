package com.sbfirebase.kiossku.ui.screen.submitkios.uicomponent

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
    onClickBack : () -> Unit,
    onClickNext : () -> Unit,
    modifier: Modifier = Modifier,
    nextText : String = "Lanjut",
    backText : String = "Kembali",
    isLoading : Boolean = false

){
    Row(
        horizontalArrangement = Arrangement.spacedBy(
            space = 15.dp,
            alignment = Alignment.CenterHorizontally
        ),
        modifier = modifier.fillMaxWidth()
    ){
        Button(
            onClick = onClickBack,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.White
            ),
            border = BorderStroke(
                width = 0.5.dp,
                color = Color.Black
            ),
            modifier = Modifier
                .weight(1f)
                .height(50.dp),
            shape = RoundedCornerShape(16.dp)
        ){
            Text(backText)
        }

        Button(
            onClick = onClickNext,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = GreenKiossku,
                contentColor = Color.White
            ),
            modifier = Modifier
                .weight(1f)
                .height(50.dp),
            shape = RoundedCornerShape(16.dp),
            enabled = !isLoading
        ){
            if (!isLoading) {
                Text(nextText)
                Icon(
                    imageVector = Icons.Outlined.NavigateNext,
                    contentDescription = null,
                    modifier = Modifier.padding(start = 10.dp)
                )
            }
            else
                CircularProgressIndicator()
        }
    }
}

@Composable
@Preview
fun BackAndNextButtonPreview(){
    KiosskuTheme{
        Surface {
            BackAndNextButton(
                onClickBack = {},
                onClickNext = {},
                nextText = "Lanjut",
                backText = "Kembali"
            )
        }
    }
}

fun verify(
    datas : List<Pair<String , Boolean>>,
    displayError : (String) -> Unit
) : Boolean{
    // cari elemen yang berisi kosong
    val result = datas.firstOrNull { it.second }

    // kalo ada yang berisi kosong, maka verifikasi gagal
    result?.let{
        displayError(it.first)
        return false
    }
    return true
}