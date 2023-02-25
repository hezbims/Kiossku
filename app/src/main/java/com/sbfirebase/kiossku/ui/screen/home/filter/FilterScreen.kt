package com.sbfirebase.kiossku.ui.screen.home.filter

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.sbfirebase.kiossku.ui.theme.Green200
import com.sbfirebase.kiossku.ui.theme.GreenKiossku
import com.sbfirebase.kiossku.ui.theme.KiosskuTheme
import com.sbfirebase.kiossku.ui.utils.rupiahVisualTransformation

@Composable
fun FilterLayout(
    showFilter : Boolean,
    filterState : FilterState,
    onEvent : (FilterScreenEvent) -> Unit,
    modifier : Modifier = Modifier
){
    AnimatedVisibility(
        visible = showFilter,
        enter = expandVertically(expandFrom = Alignment.Top),
        exit = shrinkVertically(shrinkTowards = Alignment.Top),
        modifier = modifier
    ) {
        Card(
            shape = RoundedCornerShape(13.dp),
            elevation = 30.dp
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(
                        vertical = 20.dp,
                        horizontal = 24.dp
                    )
            ) {
                Row {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                onEvent(FilterScreenEvent.OnChangeIsDijual("jual"))
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
                        ) {
                            val color =
                                if ("jual" in filterState.sewaJual) GreenKiossku
                                else Color(0x1A118E49)
                            drawLine(
                                color = color,
                                start = Offset(x = 0f, y = size.height / 2),
                                end = Offset(x = size.width, y = size.height / 2),
                                strokeWidth = size.height
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                onEvent(FilterScreenEvent.OnChangeIsDijual("sewa"))
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
                        ) {
                            val color =
                                if ("sewa" in filterState.sewaJual) GreenKiossku
                                else Color(0x1A118E49)
                            drawLine(
                                color = color,
                                start = Offset(x = 0f, y = size.height / 2),
                                end = Offset(x = size.width, y = size.height / 2),
                                strokeWidth = size.height
                            )
                        }
                    }
                }

                LazyVerticalGrid(
                    userScrollEnabled = false,
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    horizontalArrangement = Arrangement.spacedBy(17.dp),
                    modifier = Modifier
                        .padding(top = 14.dp)
                        .height(150.dp)
                ) {
                    items(
                        listOf("kios", "lahan", "lapak", "gudang", "ruko")
                    ) { tipeProperti ->
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
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = filterState.minHarga?.toString() ?: "",
                        onValueChange = {
                            onEvent(FilterScreenEvent.OnChangeMinHarga(it))
                        },
                        label = {
                            Text(
                                text = "Min.",
                                fontSize = 12.sp
                            )
                        },
                        textStyle = TextStyle(fontSize = 12.sp),
                        shape = RoundedCornerShape(13.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        visualTransformation = { rupiahVisualTransformation(it.toString()) },
                        modifier = Modifier
                            .weight(1f)
                    )

                    OutlinedTextField(
                        value = filterState.maxHarga?.toString() ?: "",
                        onValueChange = {
                            onEvent(FilterScreenEvent.OnChangeMaxHarga(it))
                        },
                        label = {
                            Text(
                                text = "Max.",
                                fontSize = 12.sp
                            )
                        },
                        textStyle = TextStyle(fontSize = 12.sp),
                        shape = RoundedCornerShape(13.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        visualTransformation = { rupiahVisualTransformation(it.toString()) },
                        modifier = Modifier
                            .weight(1f)
                    )
                }


                DaerahFilter(
                    label = "Provinsi",
                    requiredField = null,
                    selectedDaerah = filterState.provinsi,
                    onChangeSelectedDaerah = {
                        onEvent(FilterScreenEvent.OnChangeProvinsi(it))
                    },
                    apiResponse = filterState.provinsiResponse,
                    onRefreshData = {
                        onEvent(FilterScreenEvent.OnLoadProvinsi)
                    },
                    modifier = Modifier
                        .padding(top = 16.dp)
                )

                DaerahFilter(
                    label = "Kabupaten",
                    requiredField =
                    if (filterState.provinsi == null) "Provinsi"
                    else null,
                    selectedDaerah = filterState.kabupaten,
                    onChangeSelectedDaerah = {
                        onEvent(FilterScreenEvent.OnChangeKabupaten(it))
                    },
                    apiResponse = filterState.kabupatenResponse,
                    onRefreshData = {
                        onEvent(FilterScreenEvent.OnLoadKabupaten)
                    },
                    modifier = Modifier
                        .padding(top = 16.dp)
                )

                DaerahFilter(
                    label = "Kecamatan",
                    requiredField =
                    if (filterState.kabupaten == null) "Kabupaten"
                    else null,
                    selectedDaerah = filterState.kecamatan,
                    onChangeSelectedDaerah = {
                        onEvent(FilterScreenEvent.OnChangeKecamatan(it))
                    },
                    apiResponse = filterState.kecamatanResponse,
                    onRefreshData = {
                        onEvent(FilterScreenEvent.OnLoadKecamatan)
                    },
                    modifier = Modifier
                        .padding(top = 16.dp)
                )

                DaerahFilter(
                    label = "Kelurahan",
                    requiredField =
                    if (filterState.kecamatan == null) "Kecamatan"
                    else null,
                    selectedDaerah = filterState.kelurahan,
                    onChangeSelectedDaerah = {
                        onEvent(FilterScreenEvent.OnChangeKelurahan(it))
                    },
                    apiResponse = filterState.kelurahanResponse,
                    onRefreshData = {
                        onEvent(FilterScreenEvent.OnLoadKelurahan)
                    },
                    modifier = Modifier
                        .padding(top = 16.dp)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .padding(top = 24.dp)
                ) {
                    Button(
                        onClick = {
                            onEvent(FilterScreenEvent.OnResetFilterState)
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.White
                        ),
                        border = BorderStroke(
                            width = 0.5.dp,
                            color = Color.Black
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Reset")
                    }
                    Button(
                        onClick = {
                            onEvent(FilterScreenEvent.OnApplyFilterState)
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = GreenKiossku,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        shape = RoundedCornerShape(16.dp),
                    ) {
                        Text("Cari")
                    }
                }
            }
        }
    }
}

@Composable
private fun DaerahFilter(
    label : String,
    requiredField : String?,
    selectedDaerah : Daerah?,
    onChangeSelectedDaerah : (Daerah?) -> Unit,
    apiResponse : ApiResponse<List<Daerah?>>,
    onRefreshData: () -> Unit,
    modifier : Modifier = Modifier
){
    Column(
        modifier = modifier
    ) {
        var expanded by rememberSaveable {
            mutableStateOf(false)
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
                    .fillMaxWidth()
            ) {
                if (requiredField != null) {
                    Text(
                        text = "Tolong isi $requiredField terlebih dahulu!",
                        fontSize = 12.sp,
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .fillMaxWidth()
                    )
                } else {
                    DropDownDaerahText(
                        namaField = label,
                        apiResponse = apiResponse,
                        selectedDaerah = selectedDaerah,
                        onChangeSelectedDaerah = onChangeSelectedDaerah,
                        onRefreshData = onRefreshData
                    )
                }
            }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun DropDownDaerahText(
    namaField : String,
    selectedDaerah : Daerah?,
    onChangeSelectedDaerah : (Daerah?) -> Unit,
    apiResponse: ApiResponse<List<Daerah?>>,
    onRefreshData : () -> Unit,
){
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(horizontal = 16.dp)
    ) {
        when (apiResponse) {
            is ApiResponse.Success -> {
                var expanded by remember { mutableStateOf(false) }

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text =
                            selectedDaerah?.nama ?: "--Pilih $namaField--",
                            fontSize = 12.sp,
                            modifier = Modifier
                                .weight(1f)
                        )

                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    }

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = !expanded }
                    ) {
                        apiResponse.data?.forEach{ item ->
                            DropdownMenuItem(
                                onClick = {
                                    onChangeSelectedDaerah(item)
                                }
                            ) {
                                Text(item?.nama ?: "Semua $namaField")
                            }
                        }
                    }
                }
            }
            is ApiResponse.Loading ->
                CircularProgressIndicator()
            is ApiResponse.Failure ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { onRefreshData() }
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
                    if (isChoosen) Green200
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
                showFilter = true,
                filterState = FilterState(),
                onEvent = {}
            )
        }
    }
}

@Composable
@Preview(name = "Daerah Filter Success")
fun DaerahFilterPreviewSuccess(){
    KiosskuTheme {
        Surface {
            DaerahFilter(
                label = "Provinsi",
                apiResponse = ApiResponse.Success(
                    data = listOf(
                        Daerah(id = "" , nama = "Bali"),
                        Daerah(id = "" , nama = "Jawa Timur")
                    )
                ),
                selectedDaerah = null,
                onChangeSelectedDaerah = {},
                onRefreshData = {},
                requiredField = null
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
                selectedDaerah = null,
                onChangeSelectedDaerah = {},
                onRefreshData = {},
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
                selectedDaerah = null,
                onChangeSelectedDaerah = {},
                onRefreshData = {},
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