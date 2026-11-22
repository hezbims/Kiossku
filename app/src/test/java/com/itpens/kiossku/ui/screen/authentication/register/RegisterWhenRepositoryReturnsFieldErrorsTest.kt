@file:Suppress("FunctionName")
@file:OptIn(ExperimentalCoroutinesApi::class)

package com.itpens.kiossku.ui.screen.authentication.register

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.itpens.kiossku.data.model.register.RegisterNewUserError
import com.itpens.kiossku.domain.model.ResultWrapper
import com.itpens.kiossku.domain.repo_interface.IAuthRepository
import com.itpens.kiossku.route.AllRoute
import com.itpens.kiossku.ui.screen.authentication.register.pageobject.RegisterPom
import com.itpens.kiossku.ui.utils.IToastDisplayer
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@GraphicsMode(GraphicsMode.Mode.NATIVE)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class RegisterWhenRepositoryReturnsFieldErrorsTest {

    @get:Rule
    val composeRule = createComposeRule()

    private lateinit var authRepository : IAuthRepository
    private lateinit var toastDisplayer : IToastDisplayer
    private lateinit var viewModel : RegisterViewModel
    private val registerPom by lazy { RegisterPom(composeRule) }

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())

        authRepository = mockk()
        toastDisplayer = mockk(relaxed = true)
        viewModel = RegisterViewModel(
            authRepository = authRepository,
            toastDisplayer = toastDisplayer
        )

        coEvery { authRepository.register(any()) } returns
            ResultWrapper.Failure(
                listOf(
                    RegisterNewUserError.Email.AlreadyRegistered("Email already used"),
                    RegisterNewUserError.PhoneNumber.Invalid("Phone number is invalid"),
                    RegisterNewUserError.Password.Invalid(
                        "Password must be at least 6 characters"
                    ),
                    RegisterNewUserError.FullName.AlreadyRegistered(
                        "Full name already used"
                    )
                )
            )

        composeRule.setContent { TestGraph() }
        composeRule.waitForIdle()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Composable
    private fun TestGraph() {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = AllRoute.Auth.Register.route
        ) {
            composable(AllRoute.Auth.Register.route) {
                RegisterScreen(viewModel = viewModel, navController = navController)
            }
            composable(AllRoute.Auth.Login.route) { Text("Login") }
        }
    }

    @Test
    fun `When the repository returns field errors then each error is displayed under its own field`() {
        registerPom.actionTypeFullName("Budi")
        registerPom.actionTypeTelepon("81234567890")
        registerPom.actionTypeEmail("budi@example.com")
        registerPom.actionTypePassword("secret123")
        registerPom.actionTypeConfirmPassword("secret123")

        registerPom.actionClickRegister()

        registerPom.assertFullNameErrorIsDisplayed("Full name already used")
        registerPom.assertTeleponErrorIsDisplayed("Phone number is invalid")
        registerPom.assertEmailErrorIsDisplayed("Email already used")
        registerPom.assertPasswordErrorIsDisplayed(
            "Password must be at least 6 characters"
        )
        registerPom.assertLoginScreenIsNotDisplayed()
    }
}
