package com.sbfirebase.kiossku.ui.screen.submitkios.uicomponent

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.sbfirebase.kiossku.data.api.Daerah
import com.sbfirebase.kiossku.domain.apiresponse.ApiResponse

@Composable
fun DropDownDaerah(
    value : Daerah?,
    onValueChange : (Daerah?) -> Unit,
    onLoadData : () -> Unit,
    response : ApiResponse<List<Daerah?>>?,
    placeholder : String,
    errorMessage : String?,

    modifier : Modifier = Modifier
){
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (response) {
            is ApiResponse.Failure -> {
                Text(
                    text = "Gagal memuat data $placeholder,\n" +
                            "Tekan untuk memuat ulang",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .clickable { onLoadData() }
                )
                errorMessage?.let{
                    Text(
                        text = it,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colors.error,
                        fontSize = 11.sp
                    )
                }
            }
            is ApiResponse.Loading-> {
                CircularProgressIndicator()
            }
            is ApiResponse.Success -> {
                DropDownTextField(
                    value = value?.nama ?: "",
                    onChangeValue = onValueChange,
                    placeholder = placeholder,
                    itemList = response.data!!,
                    errorMessage = errorMessage
                )
            }
            null -> {}
        }
    }
}