@file:Suppress("FunctionName")
@file:OptIn(ExperimentalCoroutinesApi::class)

package com.itpens.kiossku.ui.screen.authentication.register

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.itpens.kiossku.R
import com.itpens.kiossku.data.model.register.RegisterNewUserError
import com.itpens.kiossku.domain.model.ResultWrapper
import com.itpens.kiossku.domain.repo_interface.IAuthRepository
import com.itpens.kiossku.route.AllRoute
import com.itpens.kiossku.ui.screen.authentication.register.pageobject.RegisterPom
import com.itpens.kiossku.ui.utils.IToastDisplayer
import com.itpens.kiossku.ui.utils.UiText
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
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
class RegisterWhenRepositoryReturnsSuccessTest {

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
            ResultWrapper.Success<Unit, List<RegisterNewUserError>>(Unit)

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
    fun `When the repository returns success then the login screen is displayed and a success toast is shown`() {
        registerPom.actionTypeFullName("Budi")
        registerPom.actionTypeTelepon("81234567890")
        registerPom.actionTypeEmail("budi@example.com")
        registerPom.actionTypePassword("secret123")
        registerPom.actionTypeConfirmPassword("secret123")

        registerPom.actionClickRegister()

        registerPom.assertLoginScreenIsDisplayed()
        registerPom.assertRegisterScreenIsNotDisplayed()
        verify { toastDisplayer(UiText.Resource(R.string.register_success)) }
    }
}
