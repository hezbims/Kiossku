package com.sbfirebase.kiossku.ui.screen.home.filter

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sbfirebase.kiossku.ui.theme.KiosskuTheme

@Composable
fun Experiment(){
    Column {
        var text by remember {
            mutableStateOf("")
        }

        BasicTextField(
            value = text,
            onValueChange = {
                text = it
            },
            textStyle = TextStyle(
                fontSize = 12.sp
            ),
            decorationBox = {innerTextField ->
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                ){
                    Box(
                        modifier = Modifier.weight(1f)
                    ) {
                        if (text.isEmpty())
                            Text(
                                text = "Placeholder",
                                fontSize = 12.sp
                            )
                        innerTextField()
                    }
                }
            }
        )
    }
}

@Composable
@Preview
fun ExperimentPreview(){
    KiosskuTheme {
        Surface {
            Experiment()
        }
    }
}