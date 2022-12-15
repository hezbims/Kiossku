package com.sbfirebase.kiossku.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.sbfirebase.kiossku.R
import com.sbfirebase.kiossku.data.KiosData
import com.sbfirebase.kiossku.databinding.FragmentHomeBinding
import com.sbfirebase.kiossku.ui.theme.GreenKiossku
import com.sbfirebase.kiossku.ui.theme.KiosskuTheme
import java.text.DecimalFormat

class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var binding : FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentHomeBinding.inflate(inflater , container , false)
        binding.lifecycleOwner = viewLifecycleOwner


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(
            this ,
            HomeViewModelFactory(requireActivity().application)
        )[HomeViewModel::class.java]

        viewModel.navigateDataArgument.observe(viewLifecycleOwner){
            it?.let{
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToDetailFragment(it)
                )
                viewModel.doneNavigateToDetail()
            }
        }

        binding.viewModel = viewModel
    }
}

@Composable
fun HomeScreen(
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

            Filter(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .fillMaxWidth()
            )

            Banner(modifier = Modifier.padding(top = 16.dp))

            KiosKios()

        }


    }
}

@Composable
fun Filter(modifier: Modifier = Modifier){
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
        }
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
    kiosList : List<KiosData> =
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
        items(items = kiosList){ item ->
            KiosItem(kiosData = item)
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
@Preview
fun HomeScreenPreview(){
    KiosskuTheme {
        HomeScreen()
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