package com.sbfirebase.kiossku.ui.screen.home.filter

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.WifiOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sbfirebase.kiossku.data.api.Daerah
import com.sbfirebase.kiossku.domain.apiresponse.ApiResponse
import com.sbfirebase.kiossku.ui.screen.home.FilterScreenEvent
import com.sbfirebase.kiossku.ui.screen.home.FilterState
import com.sbfirebase.kiossku.ui.theme.GreenKiossku
import com.sbfirebase.kiossku.ui.theme.KiosskuTheme

@Composable
fun FilterLayout(
    filterState : FilterState,
    onEvent : (FilterScreenEvent) -> Unit,
    modifier : Modifier = Modifier
){
    Card(
        elevation = 16.dp,
        shape = RoundedCornerShape(13.dp),
        modifier = modifier
    ){
        Column(
            modifier = Modifier
                .padding(
                    vertical = 20.dp,
                    horizontal = 24.dp
                )
                .verticalScroll(rememberScrollState())
        ){
            Row{
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            onEvent(FilterScreenEvent.OnChangeIsDijual(true))
                        }
                ) {
                    Text(
                        text = "Dijual",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Canvas(
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .fillMaxWidth()
                            .height(4.dp)
                    ){
                        val color =
                            if (filterState.isDijual) GreenKiossku
                            else Color(0x1A118E49)
                        drawLine(
                            color = color,
                            start = Offset(x = 0f , y = size.height / 2),
                            end = Offset(x = size.width , y = size.height / 2),
                            strokeWidth = size.height
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            onEvent(FilterScreenEvent.OnChangeIsDijual(false))
                        }
                ) {
                    Text(
                        text = "Disewakan",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Canvas(
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .fillMaxWidth()
                            .height(4.dp)
                    ){
                        val color =
                            if (!filterState.isDijual) GreenKiossku
                            else Color(0x1A118E49)
                        drawLine(
                            color = color,
                            start = Offset(x = 0f , y = size.height / 2),
                            end = Offset(x = size.width , y = size.height / 2),
                            strokeWidth = size.height
                        )
                    }
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                horizontalArrangement = Arrangement.spacedBy(17.dp),
                modifier = Modifier
                    .padding(top = 14.dp)
                    .height(150.dp)
            ){
                items(
                    listOf("kios" , "lahan" , "lapak" , "gudang" , "ruko")
                ){ tipeProperti ->
                    val isChoosen = tipeProperti in filterState.tipeProperti
                    TipePropertiCard(
                        tipeProperti = tipeProperti,
                        isChoosen = isChoosen,
                        onClick = {
                            if (isChoosen)
                                onEvent(FilterScreenEvent.OnRemoveTipeProperti(tipeProperti))
                            else
                                onEvent(FilterScreenEvent.OnAddTipeProperti(tipeProperti))
                        }

                    )
                }
            }

            Text(
                text = "Rentang Harga",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                ),
                modifier = Modifier.padding(top = 16.dp)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(17.dp)
            ) {
                OutlinedTextField(
                    value = "",
                    onValueChange = {
                        onEvent(FilterScreenEvent.OnChangeMinHarga(it))
                    },
                    label = {
                        Text("Min.")
                    },
                    shape = RoundedCornerShape(13.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .weight(1f)
                )

                OutlinedTextField(
                    value = "",
                    onValueChange = {
                        onEvent(FilterScreenEvent.OnChangeMaxHarga(it))
                    },
                    label = {
                        Text("Max.")
                    },
                    shape = RoundedCornerShape(13.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .weight(1f)
                )
            }
        }
    }
}

@Composable
private fun DaerahFilter(
    label : String,
    requiredField : String?,
    apiResponse : ApiResponse<List<Daerah>>,
    modifier : Modifier = Modifier
){
    Column(
        modifier = modifier
    ) {
        var expanded by rememberSaveable {
            mutableStateOf(true)
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable { expanded = !expanded }
        ) {
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )

            Icon(
                imageVector =
                if (!expanded) Icons.Outlined.ExpandMore
                else Icons.Outlined.ExpandLess,
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 12.dp)
                    .size(18.dp)
            )
        }

        if (expanded)
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
            ) {
                if (requiredField != null) {
                    Text(
                        text = "Tolong isi $requiredField terlebih dahulu",
                        textAlign = TextAlign.Center
                    )
                } else {
                    DropDownDaerahText(
                        namaField = label,
                        apiResponse = apiResponse
                    )
                }
            }
    }
}

@Composable
private fun DropDownDaerahText(
    namaField : String,
    apiResponse: ApiResponse<List<Daerah>>
){
    Box(
        contentAlignment = Alignment.Center
    ) {
        when (apiResponse) {
            is ApiResponse.Success ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(
                            vertical = 11.dp,
                            horizontal = 16.dp
                        )
                ) {
                    Text(
                        text = "--Pilih $namaField--",
                        fontSize = 12.sp,
                        modifier = Modifier
                            .weight(1f)
                    )
                    Icon(
                        imageVector = Icons.Outlined.ExpandMore,
                        contentDescription = null,
                        modifier = Modifier
                            .size(18.dp)
                    )
                }
            is ApiResponse.Loading ->
                CircularProgressIndicator()
            is ApiResponse.Failure ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Icon(
                        imageVector = Icons.Outlined.WifiOff,
                        contentDescription = null
                    )
                    Text(
                        text = "Gagal memuat data, tekan untuk memuat ulang",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .width(200.dp)
                    )
                }
        }
    }
}

@Composable
fun TipePropertiCard(
    tipeProperti : String,
    isChoosen : Boolean,
    onClick : () -> Unit,
    modifier: Modifier = Modifier
){
    Card(
        shape = RoundedCornerShape(13.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color =
                        if (isChoosen) Color(0x1A118E49)
                        else MaterialTheme.colors.surface
                )
        ) {
            Text(
                text = tipeProperti,
                fontSize = 12.sp,
                modifier = Modifier
                    .padding(
                        vertical = 12.dp,
                        horizontal = 16.dp
                    )
            )
        }
    }
}

@Composable
@Preview
fun FilterLayoutPreview(){
    KiosskuTheme {
        Surface{
            FilterLayout(
                filterState = FilterState(),
                onEvent = {}
            )
        }
    }
}

@Composable
@Preview(name = "Daerah Filter Loading")
fun DaerahFilterPreviewLoading(){
    KiosskuTheme {
        Surface {
            DaerahFilter(
                label = "Provinsi",
                apiResponse = ApiResponse.Loading(),
                requiredField = null
            )
        }
    }
}

@Composable
@Preview(name = "Daerah Filter Internet Failure")
fun DaerahFilterPreviewInternetFailed(){
    KiosskuTheme {
        Surface {
            DaerahFilter(
                label = "Provinsi",
                apiResponse = ApiResponse.Failure(),
                requiredField = null
            )
        }
    }
}

@Composable
@Preview
fun TipePropertiCardPreview(){
    KiosskuTheme {
        Surface {
            TipePropertiCard(
                tipeProperti = "Kios",
                isChoosen = false,
                onClick = {}
            )
        }
    }
}