package com.sbfirebase.kiossku.submitkios.uicomponent

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Money
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sbfirebase.kiossku.ui.theme.KiosskuTheme

@Composable
fun BorderedTrailingIcon(
    imageVector: ImageVector,
    modifier : Modifier = Modifier
){
    Box(
        modifier = modifier
            .height(IntrinsicSize.Min)
    ){
        Icon(
            imageVector = imageVector,
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(14.dp)
        )
        Divider(
            modifier = Modifier
                .width(1.dp)
                .fillMaxHeight()
                .align(Alignment.TopEnd)
        )
    }
}

@Composable
@Preview
fun BorderedTrailingIconPreview(){
    KiosskuTheme {
        Surface{
            BorderedTrailingIcon(
                imageVector = Icons.Outlined.Money
            )
        }
    }
}
