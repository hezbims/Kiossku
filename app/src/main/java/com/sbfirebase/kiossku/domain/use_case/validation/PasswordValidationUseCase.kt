package com.sbfirebase.kiossku.domain.use_case.validation

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PasswordValidationUseCase @Inject constructor() {
    operator fun invoke(password : String , confirmPassword : String) =
        if (password != confirmPassword)
            "Password tidak sama"
        else if (password.length < 6)
            "Panjang password tidak boleh kurang dari 6"
        else null
}