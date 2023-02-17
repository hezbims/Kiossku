package com.sbfirebase.kiossku.ui.screen.authentication

import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sbfirebase.kiossku.ui.theme.KiosskuTheme

@Composable
fun PasswordTextField(
    value : String,
    onValueChange : (String) -> Unit,
    placeholder : @Composable (() -> Unit),
    showPassword : Boolean,
    onChangeVisibility : () -> Unit,
    modifier : Modifier = Modifier
){
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = placeholder,
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Lock,
                contentDescription = null
            )
        },
        trailingIcon = {
            Icon(
                imageVector =
                    if (showPassword)
                        Icons.Outlined.Visibility
                    else
                        Icons.Outlined.VisibilityOff,
                contentDescription = null,
                modifier = Modifier
                    .clickable { onChangeVisibility() }
            )
        },
        visualTransformation =
            if (showPassword) VisualTransformation.None
            else PasswordVisualTransformation(),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
    )
}

@Composable
@Preview(name = "Visible")
private fun PasswordTextFieldPreviewVisible(){
    KiosskuTheme{
        Surface{
            PasswordTextField(
                value = "123abc",
                onValueChange = {},
                placeholder = { },
                showPassword = true,
                onChangeVisibility = {}
            )
        }
    }
}

@Composable
@Preview(name = "Not Visible")
private fun PasswordTextFieldPreviewNotVisible(){
    KiosskuTheme{
        Surface{
            PasswordTextField(
                value = "123abc",
                onValueChange = {},
                placeholder = { },
                showPassword = false,
                onChangeVisibility = {}
            )
        }
    }

}