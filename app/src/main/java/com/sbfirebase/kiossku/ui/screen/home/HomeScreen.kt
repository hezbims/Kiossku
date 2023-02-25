package com.sbfirebase.kiossku.ui.screen.home

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.sbfirebase.kiossku.R
import com.sbfirebase.kiossku.data.model.getproduct.KiosDataDto
import com.sbfirebase.kiossku.domain.apiresponse.ApiResponse
import com.sbfirebase.kiossku.domain.model.KiosData
import com.sbfirebase.kiossku.route.AllRoute
import com.sbfirebase.kiossku.ui.screen.home.filter.FilterLayout
import com.sbfirebase.kiossku.ui.theme.GreenKiossku
import com.sbfirebase.kiossku.ui.theme.KiosskuTheme
import kotlinx.coroutines.launch
import java.text.DecimalFormat

@Composable
fun HomeScreen(
    navController : NavHostController,
    viewModel : HomeViewModel = hiltViewModel()
){
    val uiHomeState = viewModel.uiHomeState.collectAsState().value
    val filterState = viewModel.filterState.collectAsState().value

    HomeScreen(
        uiHomeState = uiHomeState,
        filterState = filterState,
        onHomeScreenEvent = viewModel::onHomeScreenEvent,
        onFilterScreenEvent = viewModel::onFilterScreenEvent,
        onItemClick = { kiosData ->
            navController.navigate(
                AllRoute.Detail.formatRouteWithArg(kiosData)
            ){
                launchSingleTop = true
            }
        }
    )
}
@Composable
fun HomeScreen(
    uiHomeState: UiHomeState,
    filterState : FilterState,
    onHomeScreenEvent : (HomeScreenEvent) -> Unit,
    onFilterScreenEvent : (FilterScreenEvent) -> Unit,
    onItemClick : (KiosData) -> Unit,
    modifier: Modifier = Modifier
){
    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 24.dp)
            .padding(horizontal = 24.dp)
    ) {
        val (imageHeader , filterBar , kiosKios , filterLayout) = createRefs()

        Image(
            painterResource(id = R.drawable.kiossku_header),
            contentDescription = null,
            modifier = Modifier
                .constrainAs(imageHeader) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
                .width(94.dp)
                .height(24.dp)
        )

        FilterBar(
            expanded = uiHomeState.showFilter,
            modifier = Modifier
                .constrainAs(filterBar) {
                    top.linkTo(imageHeader.bottom, margin = 16.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
                .clickable { onHomeScreenEvent(HomeScreenEvent.OnChangeShowFilter) }
        )

        KiosKios(
            response = uiHomeState.filteredData,
            isRefreshing = uiHomeState.isRefreshing,
            onLoadData = { onHomeScreenEvent(HomeScreenEvent.LoadKiosData(it)) },
            onItemClick = onItemClick,
            modifier = Modifier
                .animateContentSize(
                    animationSpec = tween()
                )
                .constrainAs(kiosKios) {
                    top.linkTo(filterBar.bottom, margin = 16.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
        )

        FilterLayout(
            showFilter = uiHomeState.showFilter,
            filterState = filterState,
            onEvent = onFilterScreenEvent,
            modifier = Modifier
                .constrainAs(filterLayout) {
                    linkTo(
                        top = filterBar.bottom,
                        bottom = parent.bottom,
                        bias = 0f,
                        topMargin = 12.dp
                    )
                    width = Dimension.fillToConstraints
                }
                .height(300.dp)
        )
    }


}

@Composable
fun FilterBar(
    expanded : Boolean,
    modifier : Modifier = Modifier
){
    OutlinedTextField(
        value = "",
        placeholder = {
            Text(
                text =
                    if (!expanded) "Cari properti disini"
                    else "Tutup filter"
            )
        },
        onValueChange = {},
        leadingIcon = {
            Icon(Icons.Outlined.Search, contentDescription = null)
        },

        shape = RoundedCornerShape(13.dp),
        trailingIcon = {
            Icon(
                imageVector =
                    if (!expanded) Icons.Outlined.ExpandMore
                    else Icons.Outlined.ExpandLess,
                contentDescription = null
            )
        },
        readOnly = true,
        enabled = false,
        modifier = modifier
    )
}

@Composable
fun Banner(
    modifier: Modifier = Modifier
){
    Box(
        modifier = modifier
    ){
        Image(
            painterResource(id = R.drawable.banner_kiossku),
            contentDescription = "Banner",
            colorFilter = ColorFilter.tint(
                Color(0x99231E1E),
                blendMode = BlendMode.Darken
            ),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
        )

        Column(Modifier.padding(11.dp)) {
            Text(
                "Temukan kios terbaikmu",
                modifier = Modifier.width(132.dp),
                color = Color.White,
                fontSize = 18.sp
            )
            Text(
                "#RealProperty",
                color = Color.White,
                fontSize = 10.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun KiosKios(
    response : ApiResponse<List<KiosDataDto?>>,
    isRefreshing : Boolean,
    onLoadData : (Boolean) -> Unit,
    onItemClick: (KiosData) -> Unit,
    modifier: Modifier = Modifier
){
    if (response is ApiResponse.Success) {
        Box(
            modifier = modifier
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item(
                    span = {
                        GridItemSpan(maxCurrentLineSpan)
                    }
                ) {
                    Banner()
                }

                response.data?.let {
                    items(items = it) { item ->
                        KiosItem(kiosData = item!!, onItemClick = onItemClick)
                    }
                }
            }

            val refreshState = rememberPullRefreshState(
                refreshing = isRefreshing,
                onRefresh = { onLoadData(true) }
            )

            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = refreshState,
                modifier = Modifier
                    .align(Alignment.TopCenter)
            )
        }
    }
    else
        Column(
            modifier = modifier
                .fillMaxSize()
        ){
            Banner()

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxSize()
            ) {
                if (response is ApiResponse.Loading)
                    CircularProgressIndicator()
                else
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable { onLoadData(false) }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.WifiOff,
                            contentDescription = null
                        )

                        Text(
                            text =
                            if (response.errorCode == null)
                                "Gagal tersambung ke server,\n" +
                                        "tekan untuk mencoba kembali"
                            else
                                "Token expired",
                            textAlign = TextAlign.Center
                        )
                    }
            }
        }

}

@Composable
fun KiosItem(
    kiosData : KiosDataDto,
    onItemClick: (KiosData) -> Unit,
    modifier: Modifier = Modifier
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        val coroutineScope = rememberCoroutineScope()
        Card(
            modifier = modifier
                .clickable {
                    coroutineScope.launch {
                        onItemClick(kiosData.mapToKiosData())
                    }
                }
        ) {
            Column(modifier = Modifier.padding(8.dp)) {

                Image(
                    painter = painterResource(id = R.drawable.kioss_home_placeholder),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .width(139.dp)
                        .height(119.dp)
                )

                // jenis, apakah kios atau lahan
                Text(
                    kiosData.jenis ?: "",
                    modifier = Modifier.padding(top = 8.dp),
                    style = TextStyle(color = GreenKiossku),
                    fontSize = 10.sp
                )

                Text(
                    kiosData.name ?: "",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Row(modifier = Modifier.padding(top = 2.dp)) {
                    Icon(
                        Icons.Outlined.PinDrop,
                        contentDescription = null,
                        tint = GreenKiossku,
                        modifier = Modifier
                            .size(13.dp)
                    )

                    Text(
                        kiosData.alamatLengkap ?: "",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(
                            color = Color(0xFF808080),
                            fontSize = 10.sp
                        ),
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                Text(
                    text =
                        "Rp" +
                        DecimalFormat("#,###").format(kiosData.harga!!) +
                        if (kiosData.sistem == "sewa") "/${kiosData.tipeHarga}"
                        else ""
                    ,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        color = GreenKiossku,
                        fontSize = 9.sp
                    ),
                    modifier = Modifier.padding(top = 7.dp)
                )
            }
        }
    }
}

@Composable
@Preview
fun HomeScreenPreview(){
    KiosskuTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {
            HomeScreen(
                uiHomeState = UiHomeState(),
                filterState = FilterState(),
                onHomeScreenEvent = {},
                onFilterScreenEvent = {},
                onItemClick = {}
            )
        }
    }
}

@Composable
@Preview
fun BannerPreview(){
    KiosskuTheme {
        Surface {
            Banner()
        }
    }
}

@Composable
@Preview
fun KiosKiosPreview(){
    KiosskuTheme {
        Surface {
            KiosKios(
                response = ApiResponse.Success(
                    data = List(24){
                        KiosDataDto(
                            jenis = "Kios",
                            alamatLengkap = "Jl Soekarno Hatta",
                            harga = 300000,
                            name = "Mega Kuningan",
                            tipeHarga = "bulan"
                        )
                    }
                ),
                isRefreshing = false,
                onLoadData = {},
                onItemClick = {}
            )
        }
    }
}

@Composable
@Preview
fun KiosItemPreview(){
    KiosskuTheme {
        Surface {
            KiosItem(kiosData = KiosDataDto(
                jenis = "Kios",
                alamatLengkap = "Jl Soekarno Hatta",
                harga = 300000,
                name = "Mega Kuningan",
                tipeHarga = "bulan"
            ) , onItemClick = {})
        }
    }
}