package com.sbfirebase.kiossku.domain.use_case

import javax.inject.Inject

class EmptyValidationUseCases @Inject constructor() {
    operator fun invoke(data : String? , namaField : String) : String? {
        return if (data?.isEmpty() == true || data == null)
            "$namaField tidak boleh kosong"
        else null
    }
}