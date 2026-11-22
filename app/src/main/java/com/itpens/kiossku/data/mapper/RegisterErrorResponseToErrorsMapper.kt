package com.itpens.kiossku.data.mapper

import com.google.gson.Gson
import com.itpens.kiossku.data.model.ApiError
import com.itpens.kiossku.data.model.register.RegisterErrorItem
import com.itpens.kiossku.data.model.register.RegisterErrorResponse
import com.itpens.kiossku.data.model.register.RegisterNewUserError
import com.itpens.kiossku.domain.mapper.Mapper
import javax.inject.Inject

class RegisterErrorResponseToErrorsMapper @Inject constructor() :
    Mapper<ApiError.Http, @JvmSuppressWildcards List<RegisterNewUserError>> {

    private val gson = Gson()

    override fun from(data: ApiError.Http): List<RegisterNewUserError> {
        val body = data.body?.string().orEmpty()

        val response = try {
            gson.fromJson(body, RegisterErrorResponse::class.java)
        } catch (_: Exception) {
            null
        }

        val items = response?.errors.orEmpty()

        if (items.isNotEmpty()) {
            return items.map { it.toRegisterNewUserError(data) }
        }

        val message = response?.message
        return listOf(
            RegisterNewUserError.Other(
                error = data.toDomain(),
                message = message.orEmpty()
            )
        )
    }

    private fun RegisterErrorItem.toRegisterNewUserError(
        httpError: ApiError.Http
    ): RegisterNewUserError = when (type) {
        "EMAIL_INVALID" -> RegisterNewUserError.Email.Invalid(message.orEmpty())
        "EMAIL_ALREADY_USED" ->
            RegisterNewUserError.Email.AlreadyRegistered(message.orEmpty())
        "PHONE_NUMBER_INVALID" ->
            RegisterNewUserError.PhoneNumber.Invalid(message.orEmpty())
        "PHONE_NUMBER_ALREADY_USED" ->
            RegisterNewUserError.PhoneNumber.AlreadyRegistered(message.orEmpty())
        "PASSWORD_INVALID" ->
            RegisterNewUserError.Password.Invalid(message.orEmpty())
        "FULL_NAME_EMPTY" ->
            RegisterNewUserError.FullName.Empty(message.orEmpty())
        "FULL_NAME_ALREADY_USED" ->
            RegisterNewUserError.FullName.AlreadyRegistered(message.orEmpty())
        else -> RegisterNewUserError.Other(
            error = httpError.toDomain(),
            message = message.orEmpty()
        )
    }
}
