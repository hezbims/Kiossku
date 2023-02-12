package com.sbfirebase.kiossku.domain.use_case.validation

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ValidationUseCase @Inject constructor(
    private val emailValidationUseCase : EmailValidationUseCase,
    private val passwordValidationUseCase : PasswordValidationUseCase,
    private val teleponValidationUseCase : TeleponValidationUseCase
) {
    fun validateEmail(email : String) = emailValidationUseCase(email)

    fun validatePassword(password : String , confirmPassword : String) =
        passwordValidationUseCase(
            password = password,
            confirmPassword = confirmPassword
        )

    fun validateTelepon(nomorTelepon : String) = teleponValidationUseCase(nomorTelepon)
}