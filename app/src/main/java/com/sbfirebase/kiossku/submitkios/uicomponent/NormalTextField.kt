package com.sbfirebase.kiossku.submitkios.uicomponent

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.sbfirebase.kiossku.submitkios.enumdata.ElemenProperti
import com.sbfirebase.kiossku.ui.theme.KiosskuTheme

@Composable
fun NormalLongTextField(
    elemenProperti: ElemenProperti<Long>,
    modifier : Modifier = Modifier,
    leadingIcon : @Composable (() -> Unit)? = null,
    trailingIcon : @Composable (() -> Unit)? = null
){
    OutlinedTextField(
        value = elemenProperti.getValue()?.toString() ?: "",
        onValueChange = {
            if (it.isEmpty())
                elemenProperti.setValue(null)
            else if (it.length < 18 && it.last().isDigit())
                elemenProperti.setValue(it.toLong())

        },
        modifier = modifier.fillMaxWidth(),
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        placeholder = {
            Text(elemenProperti.placeholder)
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

@Composable
fun NormalStringTextField(
    elemenProperti: ElemenProperti<String>,
    modifier : Modifier = Modifier,
    leadingIcon : @Composable (() -> Unit)? = null,
    trailingIcon : @Composable (() -> Unit)? = null
){
    OutlinedTextField(
        value = elemenProperti.getValue() ?: "",
        onValueChange = {
            elemenProperti.setValue(it)
        },
        modifier = modifier.fillMaxWidth(),
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        placeholder = {
            Text(elemenProperti.placeholder)
        }
    )
}

@Composable
@Preview
fun NormalIntTextFieldPreview(){
    KiosskuTheme{
        Surface{
            NormalLongTextField(
                elemenProperti = ElemenProperti("Harga")
            )
        }
    }
}