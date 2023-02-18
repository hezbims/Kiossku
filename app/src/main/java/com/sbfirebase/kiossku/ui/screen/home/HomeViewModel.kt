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

    private val rawKiosData = MutableStateFlow<ApiResponse<List<KiosDataDto?>>>(ApiResponse.Loading())

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

    private fun loadData(){
        viewModelScope.launch(Dispatchers.IO) {
            rawKiosData.update { ApiResponse.Loading() }
            rawKiosData.update { getAllProduct() }
        }
    }

    init {
        loadData()
        viewModelScope.launch(Dispatchers.IO){
            rawKiosData.collectLatest { response ->
                if (response is ApiResponse.Success)
                    onFilterScreenEvent(FilterScreenEvent.OnApplyFilterState)
                else
                    _uiHomeState.update { it.copy(filteredData = response) }
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
                        val initialList = it.sewaJual
                        if (event.item in initialList){
                            val removedItem =
                                if (initialList.size == 1) emptySet()
                                else setOf(event.item)
                            it.copy(sewaJual = initialList - removedItem)
                        }
                        else
                            it.copy(sewaJual = initialList + event.item)
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
                    onFilterScreenEvent(FilterScreenEvent.OnLoadKabupaten)
                }
                is FilterScreenEvent.OnChangeKabupaten -> {
                    _filterState.update {
                        it.copy(
                            kabupaten = event.newValue,
                            kecamatan = null,
                            kelurahan = null
                        )
                    }
                    onFilterScreenEvent(FilterScreenEvent.OnLoadKecamatan)
                }
                is FilterScreenEvent.OnChangeKecamatan -> {
                    _filterState.update {
                        it.copy(
                            kecamatan = event.newValue,
                            kelurahan = null
                        )
                    }
                    onFilterScreenEvent(FilterScreenEvent.OnLoadKelurahan)
                }
                is FilterScreenEvent.OnChangeKelurahan ->
                    _filterState.update { it.copy(kelurahan = event.newValue) }
                FilterScreenEvent.OnLoadProvinsi ->
                    daerahRepository.getProvinsi().collectLatest { response ->
                        _filterState.update {
                            it.copy(provinsiResponse = addNullIfSuccess(response))
                        }
                    }
                FilterScreenEvent.OnLoadKabupaten ->
                    _filterState.value.provinsi?.id?.let { idProvinsi ->
                        daerahRepository.getKabupaten(
                            idProvinsi = idProvinsi
                        ).collectLatest { response ->
                            _filterState.update {
                                it.copy(kabupatenResponse = addNullIfSuccess(response))
                            }
                        }
                    }
                FilterScreenEvent.OnLoadKecamatan ->
                    _filterState.value.kabupaten?.id?.let { idKabupaten ->
                        daerahRepository.getKecamatan(
                            idKabupaten = idKabupaten
                        ).collectLatest { response ->
                            _filterState.update {
                                it.copy(kecamatanResponse = addNullIfSuccess(response))
                            }
                        }
                    }
                FilterScreenEvent.OnLoadKelurahan ->
                    _filterState.value.kecamatan?.id?.let { idKecamatan ->
                        daerahRepository.getKelurahan(
                            idKecamatan = idKecamatan
                        ).collectLatest { response ->
                            _filterState.update {
                                it.copy(kelurahanResponse = addNullIfSuccess(response))
                            }
                        }
                    }
                FilterScreenEvent.OnResetFilterState -> {
                    _filterState.update {
                        FilterState()
                    }
                    onFilterScreenEvent(FilterScreenEvent.OnLoadProvinsi)
                    _uiHomeState.update {
                        it.copy(filteredData = rawKiosData.value)
                    }
                }
                FilterScreenEvent.OnApplyFilterState -> {
                    if (rawKiosData.value !is ApiResponse.Success) return@launch

                    _filterState.value.let { filterState ->
                        val filteredData = rawKiosData.value.data!!.filter {
                            it?.sistem in filterState.sewaJual
                        }.filter {
                            it?.jenis in filterState.tipeProperti

                        }.filter {
                            val minHarga = filterState.minHarga ?: 0
                            val maxHarga = filterState.maxHarga ?: Int.MAX_VALUE

                            it!!.harga!! >= minHarga &&
                                    it.harga!! <= maxHarga
                        }.filter {
                            val provinsi = filterState.provinsi?.nama ?: ""
                            val kabupaten = filterState.kabupaten?.nama ?: ""
                            val kecamatan = filterState.kecamatan?.nama ?: ""
                            val kelurahan = filterState.kelurahan?.nama ?: ""

                            it!!.lokasi!!.contains(provinsi) &&
                                    it.lokasi!!.contains(kabupaten) &&
                                    it.lokasi.contains(kecamatan) &&
                                    it.lokasi.contains(kelurahan)
                        }

                        _uiHomeState.update {
                            it.copy(filteredData = ApiResponse.Success(filteredData))
                        }
                    }
                }
            }
        }
    }

    private fun addNullIfSuccess(response : ApiResponse<List<Daerah?>>) =
        if (response is ApiResponse.Success)
            ApiResponse.Success(listOf(null) + response.data!!)
        else response

    init {
        onFilterScreenEvent(
            event = FilterScreenEvent.OnLoadProvinsi
        )
    }
}

data class UiHomeState(
    val filteredData : ApiResponse<List<KiosDataDto?>> = ApiResponse.Loading(),
    val showFilter : Boolean = false,
    val isRefreshing : Boolean = false
)

sealed class HomeScreenEvent {
    object LoadKiosData : HomeScreenEvent()
    object OnChangeShowFilter : HomeScreenEvent()
}

data class FilterState(
    val sewaJual : List<String> = listOf("sewa" , "jual"),
    val tipeProperti : List<String> = listOf("kios" , "lapak" , "lahan" , "gudang" , "ruko"),
    val minHarga : Int? = null,
    val maxHarga : Int? = null,
    val provinsi : Daerah? = null,
    val kabupaten : Daerah? = null,
    val kecamatan : Daerah? = null,
    val kelurahan : Daerah? = null,
    val provinsiResponse: ApiResponse<List<Daerah?>> = ApiResponse.Loading(),
    val kabupatenResponse: ApiResponse<List<Daerah?>> = ApiResponse.Loading(),
    val kecamatanResponse: ApiResponse<List<Daerah?>> = ApiResponse.Loading(),
    val kelurahanResponse: ApiResponse<List<Daerah?>> = ApiResponse.Loading(),
)

sealed class FilterScreenEvent{
    class OnChangeIsDijual(val item : String) : FilterScreenEvent()
    class OnAddTipeProperti(val newItem : String) : FilterScreenEvent()
    class OnRemoveTipeProperti(val removedItem : String) : FilterScreenEvent()
    class OnChangeMinHarga(val newValue : String) : FilterScreenEvent()
    class OnChangeMaxHarga(val newValue : String) : FilterScreenEvent()
    class OnChangeProvinsi(val newValue : Daerah?) : FilterScreenEvent()
    class OnChangeKabupaten(val newValue : Daerah?) : FilterScreenEvent()
    class OnChangeKecamatan(val newValue : Daerah?) : FilterScreenEvent()
    class OnChangeKelurahan(val newValue : Daerah?) : FilterScreenEvent()
    object OnLoadProvinsi : FilterScreenEvent()
    object OnLoadKabupaten : FilterScreenEvent()
    object OnLoadKecamatan : FilterScreenEvent()
    object OnLoadKelurahan : FilterScreenEvent()
    object OnResetFilterState : FilterScreenEvent()
    object OnApplyFilterState : FilterScreenEvent()
}