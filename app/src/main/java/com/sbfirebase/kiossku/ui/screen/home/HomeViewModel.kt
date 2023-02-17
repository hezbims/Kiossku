package com.sbfirebase.kiossku.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sbfirebase.kiossku.data.api.Daerah
import com.sbfirebase.kiossku.data.model.getproduct.KiosDataDto
import com.sbfirebase.kiossku.domain.apiresponse.ApiResponse
import com.sbfirebase.kiossku.domain.repo_interface.IDaerahRepository
import com.sbfirebase.kiossku.domain.use_case.GetAllProductUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllProduct: GetAllProductUseCases,
    private val daerahRepository : IDaerahRepository
) : ViewModel() {
    private val _uiHomeState = MutableStateFlow(UiHomeState())
    val uiHomeState = _uiHomeState.asStateFlow()

    fun onHomeScreenEvent(event : HomeScreenEvent){
        when (event){
            HomeScreenEvent.LoadKiosData ->
                loadData()
            HomeScreenEvent.OnChangeShowFilter ->
                _uiHomeState.update {
                    it.copy(showFilter = !it.showFilter)
                }
        }
    }
    init {
        loadData()
    }

    private fun loadData(){
        viewModelScope.launch(Dispatchers.IO) {
            _uiHomeState.update {
                _uiHomeState.value.copy(getProductApiResponse = ApiResponse.Loading())
            }
            _uiHomeState.update {
                it.copy(getProductApiResponse = getAllProduct())
            }
        }
    }

    private val _filterState = MutableStateFlow(FilterState())
    val filterState = _filterState.asStateFlow()
    fun onFilterScreenEvent(event: FilterScreenEvent) {
        viewModelScope.launch(Dispatchers.IO) {
            when (event) {
                is FilterScreenEvent.OnChangeIsDijual ->
                    _filterState.update {
                        it.copy(isDijual = event.newValue)
                    }
                is FilterScreenEvent.OnAddTipeProperti ->
                    _filterState.update {
                        it.copy(tipeProperti = it.tipeProperti + event.newItem)
                    }
                is FilterScreenEvent.OnRemoveTipeProperti ->
                    _filterState.update {
                        it.copy(tipeProperti = it.tipeProperti - event.removedItem)
                    }
                is FilterScreenEvent.OnChangeMinHarga ->
                    if (event.newValue.isEmpty())
                        _filterState.update { it.copy(minHarga = null) }
                    else if (event.newValue.last().isDigit())
                        try {
                            _filterState.update {
                                it.copy(minHarga = event.newValue.toInt())
                            }
                        } catch (e: NumberFormatException) {
                            e.printStackTrace()
                        }
                is FilterScreenEvent.OnChangeMaxHarga ->
                    if (event.newValue.isEmpty())
                        _filterState.update { it.copy(maxHarga = null) }
                    else if (event.newValue.last().isDigit())
                        try {
                            _filterState.update {
                                it.copy(maxHarga = event.newValue.toInt())
                            }
                        } catch (e: NumberFormatException) {
                            e.printStackTrace()
                        }
                is FilterScreenEvent.OnChangeProvinsi -> {
                    _filterState.update {
                        it.copy(
                            provinsi = event.newValue,
                            kabupaten = null,
                            kecamatan = null,
                            kelurahan = null,
                        )
                    }
                }
                is FilterScreenEvent.OnChangeKabupaten -> {
                    _filterState.update {
                        it.copy(
                            kabupaten = event.newValue,
                            kecamatan = null,
                            kelurahan = null
                        )
                    }
                }
                is FilterScreenEvent.OnChangeKecamatan -> {
                    _filterState.update {
                        it.copy(
                            kecamatan = event.newValue,
                            kelurahan = null
                        )
                    }
                }
                is FilterScreenEvent.OnChangeKelurahan ->
                    _filterState.update { it.copy(kelurahan = event.newValue) }
                FilterScreenEvent.OnLoadProvinsi ->
                    daerahRepository.getProvinsi().collectLatest { response ->
                        _filterState.update { it.copy(provinsiResponse = response) }
                    }
                FilterScreenEvent.OnLoadKabupaten ->
                    daerahRepository.getKabupaten(
                        idProvinsi = _filterState.value.provinsi!!.id
                    ).collectLatest { response ->
                        _filterState.update {
                            it.copy(kabupatenResponse = response)
                        }
                    }
                FilterScreenEvent.OnLoadKecamatan ->
                    daerahRepository.getKecamatan(
                        idKabupaten = _filterState.value.kabupaten!!.id
                    ).collectLatest { response ->
                        _filterState.update {
                            it.copy(kecamatanResponse = response)
                        }
                    }
                FilterScreenEvent.OnLoadKelurahan ->
                    daerahRepository.getKelurahan(
                        idKecamatan = _filterState.value.kecamatan!!.id
                    ).collectLatest { response ->
                        _filterState.update {
                            it.copy(kelurahanResponse = response)
                        }
                    }
            }
        }
    }
}

data class UiHomeState(
    val getProductApiResponse : ApiResponse<List<KiosDataDto?>> = ApiResponse.Loading(),
    val showFilter : Boolean = false
)

sealed class HomeScreenEvent {
    object LoadKiosData : HomeScreenEvent()
    object OnChangeShowFilter : HomeScreenEvent()
}

data class FilterState(
    val isDijual : Boolean = true,
    val tipeProperti : List<String> = listOf("kios" , "lapak" , "lahan" , "gudang" , "ruko"),
    val minHarga : Int? = null,
    val maxHarga : Int? = null,
    val provinsi : Daerah? = null,
    val kabupaten : Daerah? = null,
    val kecamatan : Daerah? = null,
    val kelurahan : Daerah? = null,
    val provinsiResponse: ApiResponse<List<Daerah>> = ApiResponse.Loading(),
    val kabupatenResponse: ApiResponse<List<Daerah>> = ApiResponse.Loading(),
    val kecamatanResponse: ApiResponse<List<Daerah>> = ApiResponse.Loading(),
    val kelurahanResponse: ApiResponse<List<Daerah>> = ApiResponse.Loading(),
)

sealed class FilterScreenEvent{
    class OnChangeIsDijual(val newValue : Boolean) : FilterScreenEvent()
    class OnAddTipeProperti(val newItem : String) : FilterScreenEvent()
    class OnRemoveTipeProperti(val removedItem : String) : FilterScreenEvent()
    class OnChangeMinHarga(val newValue : String) : FilterScreenEvent()
    class OnChangeMaxHarga(val newValue : String) : FilterScreenEvent()
    class OnChangeProvinsi(val newValue : Daerah) : FilterScreenEvent()
    class OnChangeKabupaten(val newValue : Daerah) : FilterScreenEvent()
    class OnChangeKecamatan(val newValue : Daerah) : FilterScreenEvent()
    class OnChangeKelurahan(val newValue : Daerah) : FilterScreenEvent()
    object OnLoadProvinsi : FilterScreenEvent()
    object OnLoadKabupaten : FilterScreenEvent()
    object OnLoadKecamatan : FilterScreenEvent()
    object OnLoadKelurahan : FilterScreenEvent()
}