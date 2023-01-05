package com.sbfirebase.kiossku.ui.screen.submitkios.uicomponent

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sbfirebase.kiossku.ui.theme.KiosskuTheme

@Composable
fun <Item> DropDownTextField(
    value : String,
    onChangeValue : (Item) -> Unit,
    placeholder : String,
    itemList : List<Item>,
    modifier: Modifier = Modifier,
){
    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        var expanded by rememberSaveable{ mutableStateOf(false) }

        OutlinedTextField(
            value = value,
            onValueChange = {},
            placeholder = {
                Text(placeholder)
            },
            modifier = Modifier
                .clickable {
                    expanded = !expanded
                }
                .fillMaxWidth(),
            enabled = false,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                disabledTextColor = LocalContentColor.current.copy(LocalContentAlpha.current),
                disabledLabelColor =  MaterialTheme.colors.onSurface.copy(ContentAlpha.medium),
                disabledBorderColor = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled),
                disabledPlaceholderColor = MaterialTheme.colors.onSurface.copy(ContentAlpha.medium),
                disabledLeadingIconColor = MaterialTheme.colors.onSurface.copy(alpha = TextFieldDefaults.IconOpacity),
                disabledTrailingIconColor = MaterialTheme.colors.onSurface.copy(alpha = TextFieldDefaults.IconOpacity)
            ),
            trailingIcon = {
                Icon(
                    imageVector =
                        if (expanded)
                            Icons.Outlined.ExpandLess
                        else
                            Icons.Outlined.ExpandMore ,
                    contentDescription = null
                )
            },
            shape = RoundedCornerShape(16.dp)
        )

        Box(
            modifier = Modifier.align(
                Alignment.TopEnd
            )
        ){
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .wrapContentSize()
            ) {
                for (item in itemList) {
                    DropdownMenuItem(
                        onClick = {
                            onChangeValue(item)
                        }
                    ) {
                        Text(item.toString())
                    }
                }
            }
        }

    }
}

@Composable
@Preview
fun DropDownTextFieldPreview(){
    KiosskuTheme {
        Surface{
            val value = remember {
                mutableStateOf("")
            }
            DropDownTextField(
                value = "",
                onChangeValue = {},
                itemList = listOf("Kios" , "Lahan" , "Gudang"),
                placeholder = "Jenis properti"
            )
        }
    }
}