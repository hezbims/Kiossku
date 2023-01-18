package com.sbfirebase.kiossku.ui.screen.submitkios.screen.langkahketiga

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sbfirebase.kiossku.data.model.postproduct.PostKiosData
import com.sbfirebase.kiossku.data.model.postproduct.SuccessfulPostProductResponse
import com.sbfirebase.kiossku.domain.AuthManager
import com.sbfirebase.kiossku.domain.apiresponse.AuthorizedApiResponse
import com.sbfirebase.kiossku.domain.apiresponse.RefreshTokenApiResponse
import com.sbfirebase.kiossku.domain.repo_interface.IPostProductRepository
import com.sbfirebase.kiossku.ui.utils.ToastDisplayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LangkahKetigaViewModel @Inject constructor(
    private val postProductRepo : IPostProductRepository,
    private val authManager : AuthManager,
    private val toastDisplayer: ToastDisplayer
): ViewModel() {
    private var photoId = 0
    private val _photoUris = mutableStateListOf<UriWithId>()
    val photoUris : List<UriWithId>
        get() = _photoUris


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

    private val _submitState = MutableStateFlow<AuthorizedApiResponse<SuccessfulPostProductResponse>>(
        AuthorizedApiResponse.Failure()
    )
    val submitState = _submitState.asStateFlow()
    fun doneNavigating(){ _submitState.update { AuthorizedApiResponse.Failure() } }

    private var submitJob : Job? = null

    fun submitData(
        postData : PostKiosData
    ){
        if (submitJob == null || submitJob?.isCompleted == true)
            submitJob = viewModelScope.launch(Dispatchers.IO){
                val tokenResponse = authManager.getToken()

                when(tokenResponse) {
                    is RefreshTokenApiResponse.Success ->
                        postProductRepo.submitProduct(
                            postData = postData,
                            token = tokenResponse.token
                        )
                            .collect { apiResponse ->
                                if (apiResponse is AuthorizedApiResponse.Failure)
                                    withContext(Dispatchers.Main) {
                                        toastDisplayer("Gagal mensubmit data, periksa internet anda")
                                    }
                                _submitState.update { apiResponse }
                            }
                    else ->
                        withContext(Dispatchers.Main) {
                            toastDisplayer("Data gagal dikirim, periksa internet anda")
                        }
                }
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