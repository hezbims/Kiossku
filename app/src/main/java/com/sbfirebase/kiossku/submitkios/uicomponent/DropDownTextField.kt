package com.sbfirebase.kiossku.submitkios.uicomponent

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sbfirebase.kiossku.submitkios.enumdata.BaseEnum
import com.sbfirebase.kiossku.submitkios.enumdata.ElemenProperti
import com.sbfirebase.kiossku.submitkios.enumdata.JenisProperti
import com.sbfirebase.kiossku.ui.theme.KiosskuTheme

@Composable
fun <Item : BaseEnum> DropDownTextField(
    elemenProperti: ElemenProperti<Item>,
    itemList : List<Item>,
    modifier: Modifier = Modifier,
){
    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        var expanded by rememberSaveable{ mutableStateOf(false) }

        OutlinedTextField(
            value = elemenProperti.getValue()?.toString() ?: "",
            onValueChange = {},
            placeholder = {
                Text(elemenProperti.placeholder)
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
                            elemenProperti.setValue(item)
                            expanded = false
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
            DropDownTextField(
                ElemenProperti("Jenis properti" ,  ""),
                itemList = JenisProperti.values().asList(),
            )
        }
    }
}