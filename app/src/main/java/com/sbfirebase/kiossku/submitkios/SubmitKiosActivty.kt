package com.sbfirebase.kiossku.submitkios

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.material.Surface
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sbfirebase.kiossku.submitkios.enumdata.SistemPembayaran
import com.sbfirebase.kiossku.submitkios.navigationroute.langkahKeduaNav
import com.sbfirebase.kiossku.submitkios.navigationroute.langkahKetigaNav
import com.sbfirebase.kiossku.submitkios.navigationroute.langkahSatuNav
import com.sbfirebase.kiossku.submitkios.navigationroute.pilihSewaJualNav
import com.sbfirebase.kiossku.submitkios.screen.LangkahKedua
import com.sbfirebase.kiossku.submitkios.screen.LangkahKetiga
import com.sbfirebase.kiossku.submitkios.screen.LangkahSatu
import com.sbfirebase.kiossku.submitkios.screen.PilihSewaJual
import com.sbfirebase.kiossku.ui.theme.KiosskuTheme

class SubmitKiosActivty : ComponentActivity() {

    private lateinit var viewModel : SubmitKiosViewModel
    private val pickPhoto = registerForActivityResult(
        PickMultiplePhotoContract()
    ){multipleImageUri ->
        viewModel.addPhotoUris(multipleImageUri)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KiosskuTheme {
                Surface {
                    viewModel = viewModel(
                        factory = SubmitKiosViewModelFactory(
                            app = application
                        )
                    )

                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = pilihSewaJualNav.route

                    ) {
                        composable(
                            route = pilihSewaJualNav.route
                        ){
                            PilihSewaJual(
                                setSistemPembayaran = viewModel.sistemPembayaran::setValue,
                                navigate = {
                                    navController
                                        .navigate(langkahSatuNav.route)
                                }
                            )
                        }

                        composable(
                            route = langkahSatuNav.route
                        ){
                            LangkahSatu(
                                judulPromosi = viewModel.judulPromosi,
                                jenisProperti = viewModel.jenisProperti,
                                harga = viewModel.harga,
                                waktuPembayaran = viewModel.waktuPembayaran,
                                isSewa = viewModel.sistemPembayaran.getValue() == SistemPembayaran.SEWA,
                                tipePenawaran = viewModel.tipePenawaran,
                                navigateNext = {
                                    navController.navigate(
                                        route = langkahKeduaNav.route
                                    )
                                },
                                navigateBack = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        composable(
                            route = langkahKeduaNav.route
                        ){
                            LangkahKedua(
                                luasLahan = viewModel.luasLahan,
                                panjang = viewModel.panjang,
                                lebar = viewModel.lebar,
                                luasBangunan = viewModel.luasBangunan,
                                jumlahLantai = viewModel.jumlahLantai,
                                kapasitasListrik = viewModel.kapasitasListrik,
                                fasilitas = viewModel.fasilitas,
                                deskripsi = viewModel.deskripsi,
                                navigateNext = {
                                    navController.navigate(langkahKetigaNav.route)
                                },
                                navigateBack = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        composable(
                            route = langkahKetigaNav.route
                        ){
                            LangkahKetiga(
                                getPhotoUri = {
                                    pickPhoto.launch("image/*")
                                },
                                deletePhoto = viewModel::deletePhotoUri,
                                photoUris = viewModel.photoUris
                            )
                        }
                    }
                }
            }
        }
    }

    class PickMultiplePhotoContract : ActivityResultContract<String, List<Uri>>(){
        override fun createIntent(context: Context, input: String) : Intent {
            val openMultipleImageIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            }
            return Intent.createChooser(
                openMultipleImageIntent,
                "Select photo"
            )
        }

        override fun parseResult(resultCode: Int, intent: Intent?): List<Uri> {
            val result = mutableListOf<Uri>()
            intent?.let { _intent ->
                _intent.clipData?.let{ clipData ->
                    val itemCount = clipData.itemCount
                    for (i in 0 until itemCount)
                        result.add(clipData.getItemAt(i).uri)
                }
                _intent.data?.let {
                    result.add(it)
                }
            }
            return result
        }
    }


}

