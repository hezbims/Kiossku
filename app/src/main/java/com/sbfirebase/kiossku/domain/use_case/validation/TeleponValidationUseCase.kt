package com.sbfirebase.kiossku.domain.use_case.validation

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TeleponValidationUseCase @Inject constructor() {
    operator fun invoke(nomorTelepon : String) : String? =
        if (nomorTelepon.length != 11)
            "Nomor telepon tidak valid"
        else null

}