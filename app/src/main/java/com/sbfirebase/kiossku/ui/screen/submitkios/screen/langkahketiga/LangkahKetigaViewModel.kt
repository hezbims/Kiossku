package com.sbfirebase.kiossku.ui.screen.submitkios.screen.langkahketiga

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LangkahKetigaViewModel @Inject constructor(): ViewModel() {
    private var photoId = 0
    private val _photoUris = mutableStateListOf<UriWithId>()
    val photoUris = _photoUris
    fun addPhotoUris(newPhotoUris : Collection<Uri>){
        _photoUris.addAll(
            newPhotoUris.map {
                    uri ->
                UriWithId(
                    uri = uri,
                    id = photoId++
                )
            }
        )
    }
    fun deletePhotoUri(deleteId : Int){
        _photoUris.removeIf { it.id == deleteId }
    }


}

data class UriWithId(
    val uri : Uri,
    val id : Int
)

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