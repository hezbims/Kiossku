package com.sbfirebase.kiossku.domain.use_case.validation

import android.util.Patterns
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EmailValidationUseCase @Inject constructor() {
    operator fun invoke(email : String) : String? =
        email.let{
            if (it.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(it).matches())
                null
            else "Email tidak valid"
        }
}