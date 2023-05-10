package com.sbfirebase.kiossku.ui.screen.submitkios.screen.langkahketiga

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LangkahKetigaViewModel @Inject constructor(): ViewModel() {

    private val _photosUriWithId = mutableStateListOf<UriWithId>()
    val photoUriWithId : List<UriWithId>
        get() = _photosUriWithId


    private var photoId = 0
    fun onEvent(event : LangkahKetigaScreenEvent){
        when (event){
            is LangkahKetigaScreenEvent.AddPhotoUris ->
                addPhotoUris(event.photoUris)
            is LangkahKetigaScreenEvent.DeletePhotoUri ->
                deletePhotoUri(event.id)
        }
    }
    private fun addPhotoUris(newPhotoUris : Collection<Uri>){
        viewModelScope.launch(Dispatchers.IO) {
            val newPhotosUriWithId = newPhotoUris.map { uri ->
                UriWithId(
                    uri = uri,
                    id = photoId++
                )
            }

            _photosUriWithId.addAll(newPhotosUriWithId)

            Log.e("qqq" , "${_photosUriWithId.size}")
        }
    }
    private fun deletePhotoUri(deleteId : Int){
        viewModelScope.launch(Dispatchers.IO) {
            _photosUriWithId.removeIf { it.id == deleteId }
        }
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

sealed class LangkahKetigaScreenEvent{
    class AddPhotoUris(val photoUris : Collection<Uri>) : LangkahKetigaScreenEvent()
    class DeletePhotoUri(val id : Int) : LangkahKetigaScreenEvent()
}