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
import com.itpens.kiossku.domain.repo_interface.IAuthRepository
import com.itpens.kiossku.route.AllRoute
import com.itpens.kiossku.ui.screen.authentication.register.pageobject.RegisterPom
import com.itpens.kiossku.ui.utils.IToastDisplayer
import io.mockk.coVerify
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
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@GraphicsMode(GraphicsMode.Mode.NATIVE)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class RegisterWhenPasswordsDoNotMatchTest {

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
    fun `When the passwords do not match then the confirm password error is displayed and the repository is not called`() {
        val mismatchMessage = RuntimeEnvironment.getApplication()
            .getString(R.string.register_password_mismatch)

        registerPom.actionTypeFullName("Budi")
        registerPom.actionTypeTelepon("81234567890")
        registerPom.actionTypeEmail("budi@example.com")
        registerPom.actionTypePassword("secret123")
        registerPom.actionTypeConfirmPassword("different123")

        registerPom.actionClickRegister()

        registerPom.assertConfirmPasswordErrorIsDisplayed(mismatchMessage)
        coVerify(exactly = 0) { authRepository.register(any()) }
        registerPom.assertLoginScreenIsNotDisplayed()
    }
}
