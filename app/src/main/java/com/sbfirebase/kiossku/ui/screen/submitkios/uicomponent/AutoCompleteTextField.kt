package com.sbfirebase.kiossku.ui.screen.submitkios.uicomponent

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties

@Composable
fun AutoCompleteTextField(
    value : String,
    onValueChange : (String) -> Unit,
    placeholder : String,
    data : List<String>,
    modifier : Modifier = Modifier
){
    Column(
        modifier = modifier
            .fillMaxWidth()
    ){
        var expanded by rememberSaveable {
            mutableStateOf(false)
        }

        OutlinedTextField(
            value = value ,
            onValueChange = {
                expanded = it.length > value.length
                onValueChange(it)
            },
            placeholder = {
                Text(placeholder)
            },
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        )

        val filteredData = data.filter {
            it.contains(value , ignoreCase = true)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            properties = PopupProperties(),
            modifier = Modifier
        ) {
            for (item in filteredData)
                DropdownMenuItem(onClick = {
                    expanded = false
                    onValueChange(item)
                }) {
                    Text(item)
                }
        }
    }
}