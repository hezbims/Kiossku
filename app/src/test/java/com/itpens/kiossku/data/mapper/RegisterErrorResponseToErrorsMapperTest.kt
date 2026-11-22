package com.itpens.kiossku.data.mapper

import com.itpens.kiossku.data.model.ApiError
import com.itpens.kiossku.data.model.register.RegisterNewUserError
import com.itpens.kiossku.domain.model.ExternalSystemError
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@DisplayName("RegisterErrorResponseToErrorsMapper")
class RegisterErrorResponseToErrorsMapperTest {

    private val sut = RegisterErrorResponseToErrorsMapper()

    private fun httpError(body : String, statusCode : Int = 422) = ApiError.Http(
        body = body.toResponseBody("application/json".toMediaType()),
        statusCode = statusCode
    )

    companion object {
        @JvmStatic
        fun knownTypeMappings() : Stream<Arguments> = Stream.of(
            Arguments.of("EMAIL_INVALID", RegisterNewUserError.Email.Invalid("msg")),
            Arguments.of(
                "EMAIL_ALREADY_USED",
                RegisterNewUserError.Email.AlreadyRegistered("msg")
            ),
            Arguments.of(
                "PHONE_NUMBER_INVALID",
                RegisterNewUserError.PhoneNumber.Invalid("msg")
            ),
            Arguments.of(
                "PHONE_NUMBER_ALREADY_USED",
                RegisterNewUserError.PhoneNumber.AlreadyRegistered("msg")
            ),
            Arguments.of("PASSWORD_INVALID", RegisterNewUserError.Password.Invalid("msg")),
            Arguments.of("FULL_NAME_EMPTY", RegisterNewUserError.FullName.Empty("msg")),
            Arguments.of(
                "FULL_NAME_ALREADY_USED",
                RegisterNewUserError.FullName.AlreadyRegistered("msg")
            )
        )
    }

    @DisplayName("When the error type is known then it maps to the matching field error")
    @ParameterizedTest(name = "When the error type is {0}")
    @MethodSource("knownTypeMappings")
    fun mapsKnownTypeToFieldError(
        type : String,
        expected : RegisterNewUserError
    ) {
        val body = """{"errors": [{"type": "$type", "message": "msg"}]}"""

        val result = sut.from(httpError(body))

        assertEquals(listOf(expected), result)
    }

    @DisplayName("When the error type is unknown then it maps to a general error")
    @Test
    fun mapsUnknownTypeToGeneralError() {
        val body = """{"errors": [{"type": "SOMETHING_ELSE", "message": "boom"}]}"""

        val result = sut.from(httpError(body))

        assertEquals(
            listOf(
                RegisterNewUserError.Other(ExternalSystemError.Unexpected, "boom")
            ),
            result
        )
    }

    @DisplayName("When the error type is null then it maps to a general error")
    @Test
    fun mapsNullTypeToGeneralError() {
        val body = """{"errors": [{"message": "boom"}]}"""

        val result = sut.from(httpError(body))

        assertEquals(
            listOf(
                RegisterNewUserError.Other(ExternalSystemError.Unexpected, "boom")
            ),
            result
        )
    }

    @DisplayName("When there are no errors but a top-level message then it maps the message to a general error")
    @Test
    fun mapsTopLevelMessageToGeneralErrorWhenErrorsAreAbsent() {
        val body = """{"message": "boom"}"""

        val result = sut.from(httpError(body))

        assertEquals(
            listOf(
                RegisterNewUserError.Other(ExternalSystemError.Unexpected, "boom")
            ),
            result
        )
    }

    @DisplayName("When the body is empty then it maps to a general error without a message")
    @Test
    fun mapsEmptyBodyToGeneralErrorWithoutMessage() {
        val result = sut.from(httpError(""))

        assertEquals(
            listOf(RegisterNewUserError.Other(ExternalSystemError.Unexpected, "")),
            result
        )
    }

    @DisplayName("When the body is malformed then it maps to a general error without a message")
    @Test
    fun mapsMalformedBodyToGeneralErrorWithoutMessage() {
        val result = sut.from(httpError("""{"errors": ["""))

        assertEquals(
            listOf(RegisterNewUserError.Other(ExternalSystemError.Unexpected, "")),
            result
        )
    }

    @DisplayName("When the status is service unavailable then it maps to a service unavailable general error")
    @Test
    fun mapsServiceUnavailableStatusToGeneralError() {
        val result = sut.from(httpError("""{"message": "down"}""", statusCode = 503))

        assertEquals(
            listOf(
                RegisterNewUserError.Other(
                    ExternalSystemError.ServiceUnavailable,
                    "down"
                )
            ),
            result
        )
    }

    @DisplayName("When the status is unauthorized then it maps to an unauthorized access general error")
    @Test
    fun mapsUnauthorizedStatusToGeneralError() {
        val result = sut.from(httpError("""{"message": "unauthorized"}""", statusCode = 401))

        assertEquals(
            listOf(
                RegisterNewUserError.Other(
                    ExternalSystemError.UnauthorizedAccess,
                    "unauthorized"
                )
            ),
            result
        )
    }

    @DisplayName("When known and unknown error types are mixed then it maps both entries")
    @Test
    fun mapsBothKnownAndUnknownErrorsWhenMixed() {
        val body = """
            {
              "errors": [
                {"type": "EMAIL_INVALID", "message": "bad email"},
                {"type": "SOMETHING_ELSE", "message": "weird"}
              ]
            }
        """.trimIndent()

        val result = sut.from(httpError(body))

        assertEquals(
            listOf(
                RegisterNewUserError.Email.Invalid("bad email"),
                RegisterNewUserError.Other(ExternalSystemError.Unexpected, "weird")
            ),
            result
        )
    }
}
