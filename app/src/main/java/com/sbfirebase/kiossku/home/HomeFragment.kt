package com.sbfirebase.kiossku.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.PinDrop
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sbfirebase.kiossku.R
import com.sbfirebase.kiossku.data.KiosData
import com.sbfirebase.kiossku.databinding.FragmentHomeBinding
import com.sbfirebase.kiossku.ui.theme.GreenKiossku
import com.sbfirebase.kiossku.ui.theme.KiosskuTheme
import java.text.DecimalFormat

class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(
            inflater , container , false
        )
        binding.composeView.setContent {
            KiosskuTheme {
                HomeScreen(
                    viewModel = viewModel(
                        factory = HomeViewModelFactory(
                            requireActivity().application
                        )
                    )
                )
            }
        }
        return binding.root
    }
}

@Composable
fun HomeScreen(
    viewModel: HomeViewModel?,
    modifier: Modifier = Modifier
){
    Surface {
        Column(
            modifier = modifier
                .padding(top = 24.dp)
                .padding(horizontal = 24.dp)
        ) {
            Image(
                painterResource(id = R.drawable.kiossku_header),
                contentDescription = null,
                modifier = Modifier
                    .width(94.dp)
                    .height(24.dp)
            )

            FilterBar(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .fillMaxWidth()
            )

            Banner(modifier = Modifier.padding(top = 16.dp))

            if (viewModel != null)
                KiosKios(
                    kiosList = viewModel.data.value
                )
            else
                KiosKios()

        }


    }
}

@Composable
fun FilterBar(modifier: Modifier = Modifier){
    OutlinedTextField(
        value = "",
        placeholder = {
            Text("Cari properti disini")
        },
        onValueChange = {},
        leadingIcon = {
            Icon(Icons.Outlined.Search, contentDescription = null)
        },
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(13.dp),
        trailingIcon = {
            Icon(Icons.Outlined.ExpandMore , contentDescription = null)
        },
        readOnly = true
    )
}

@Composable
fun Banner(
    modifier: Modifier = Modifier
){
    Box(
        modifier = modifier
    ) {
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

@Composable
fun KiosKios(
    kiosList : List<KiosData?>? =
        List(24){
            KiosData(
                jenis = "Kios",
                alamatLengkap = "Jl Soekarno Hatta",
                harga = 300000,
                name = "Mega Kuningan",
                tipeHarga = "bulan"
            )
        },
    modifier: Modifier = Modifier
){
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        contentPadding = PaddingValues(top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        kiosList?.let {
            items(items = it) { item ->
                KiosItem(kiosData = item!!)
            }
        }
    }
}

@Composable
fun KiosItem(
    kiosData : KiosData =
        KiosData(
            jenis = "Kios",
            alamatLengkap = "Jl Soekarno Hatta",
            harga = 300000,
            name = "Mega Kuningan",
            tipeHarga = "bulan"
        ),
    modifier: Modifier = Modifier
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Card {
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
                    stringResource(
                        id = R.string.format_harga_compose,
                        DecimalFormat("#,###").format(kiosData.harga!!),
                        kiosData.tipeHarga ?: ""
                    ),
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
fun FilterLayout(modifier : Modifier = Modifier){
    Card(
        modifier = modifier
            .padding(24.dp)
            .padding(top = 16.dp)
    ){
        Column{
            Row{
                Text(
                    text = "Dijual",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "Disewakan",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.weight(1f)
                )
            }
            Canvas(modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)){
                val height = size.height
                val width = size.width
                drawLine(
                    start = Offset(x = 0f , y = height / 2),
                    end = Offset(x = width , y = height / 2),
                    color = Color(0x1A118E49),
                    strokeWidth = height
                )
                if (false)
                    drawLine(
                        start = Offset(x = 0f , y = height / 2),
                        end = Offset(x = width / 2 , y = height / 2),
                        color = GreenKiossku,
                        strokeWidth = height
                    )
                else
                    drawLine(
                        start = Offset(x = width / 2 , y = height / 2),
                        end = Offset(x = width , y = height / 2),
                        color = GreenKiossku,
                        strokeWidth = height
                    )
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                horizontalArrangement = Arrangement.spacedBy(17.dp),
                modifier = Modifier.padding(top = 14.dp)
            ){
                items(
                    listOf("Kios/ruko" , "Lahan" , "Lapak" , "Gudang")
                ){ tipeProperti ->
                    TipePropertiCard(
                        tipeProperti = tipeProperti
                    )
                }
            }

            Text(
                text = "Rentang Harga",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                ),
                modifier = Modifier.padding(top = 4.dp)
            )
            Row{
                val minimumHarga by remember{ mutableStateOf("") }
                val maksimumHarga by remember {
                    mutableStateOf("")
                }
                OutlinedTextField(value = "AAAAA", onValueChange = {},
                modifier = Modifier.defaultMinSize(minHeight = 10.dp),

                    )
            }
        }
    }
}

@Composable
fun TipePropertiCard(
    tipeProperti : String = "Kios",
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
            .width(131.dp)
            .height(40.dp)
            .clip(RoundedCornerShape(13.dp))
            .background(color = Color(0x1A118E49)),

    ) {
        Text(
            text = tipeProperti,
            style = TextStyle(
                fontSize = 12.sp
            ),
            modifier = Modifier
                .padding(vertical = 12.dp)
                .padding(start = 16.dp)
        )
    }
}

@Composable
@Preview
fun HomeScreenPreview(){
    KiosskuTheme {
        HomeScreen(null)
    }
}

@Composable
@Preview
fun BannerPreview(){
    KiosskuTheme {
        Banner()
    }
}

@Composable
@Preview
fun KiosKiosPreview(){
    KiosskuTheme {
        Surface {
            KiosKios()
        }
    }
}

@Composable
@Preview
fun KiosItemPreview(){
    KiosskuTheme {
        Surface {
            KiosItem()
        }
    }
}

@Composable
@Preview
fun FilterLayoutPreview(){
    KiosskuTheme {
        Surface{
            FilterLayout()
        }
    }
}