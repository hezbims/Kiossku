@file:OptIn(ExperimentalCoroutinesApi::class)

package com.itpens.kiossku.data.repository.auth_repository

import com.itpens.kiossku.data.api.AuthApi
import com.itpens.kiossku.data.mapper.RegisterErrorResponseToErrorsMapper
import com.itpens.kiossku.data.model.register.RegisterPost
import com.itpens.kiossku.data.repository.AuthRepositoryImpl
import com.itpens.kiossku.domain.ITokenManager
import com.itpens.kiossku.domain.SessionManager
import com.itpens.kiossku.domain.model.ResultWrapper
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import retrofit2.Response

@DisplayName("AuthRepositoryImpl - register - successful response")
class RegisterWhenResponseIsSuccessfulTest {

    private lateinit var authApiClient : AuthApi
    private lateinit var sut : AuthRepositoryImpl

    @BeforeEach
    fun setup() {
        authApiClient = mockk()
        sut = AuthRepositoryImpl(
            authApiClient = authApiClient,
            registerErrorMapper = RegisterErrorResponseToErrorsMapper(),
            tokenManager = mockk<ITokenManager>(relaxed = true),
            sessionManager = SessionManager()
        )
    }

    private fun registerPost() = RegisterPost(
        fullname = "Budi",
        telepon = "6281234567890",
        email = "budi@example.com",
        password = "secret123",
        confirm_password = "secret123"
    )

    @DisplayName("When registration succeeds then it returns a successful result and forwards the request body")
    @Test
    fun returnsSuccessAndForwardsRequestBodyWhenResponseIsSuccessful() = runTest {
        val post = registerPost()
        val capturedPost = slot<RegisterPost>()

        coEvery {
            authApiClient.register(capture(capturedPost))
        } returns Response.success(Unit)

        val result = sut.register(post)

        assertTrue(result is ResultWrapper.Success)
        assertEquals(Unit, (result as ResultWrapper.Success).data)
        coVerify(exactly = 1) { authApiClient.register(any()) }
        assertEquals(post, capturedPost.captured)
    }
}
