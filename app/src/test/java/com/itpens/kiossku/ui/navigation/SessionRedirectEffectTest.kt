@file:Suppress("FunctionName")

package com.itpens.kiossku.ui.navigation

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.itpens.kiossku.domain.SessionManager
import com.itpens.kiossku.route.AllRoute
import com.itpens.kiossku.ui.navigation.pageobject.HomePom
import com.itpens.kiossku.ui.navigation.pageobject.LoginPom
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

/**
 * Test redirect otomatis ketika SessionManager melaporkan sesi tidak valid.
 * Dijalankan di JVM via Robolectric (bukan emulator).
 *
 * Memakai NavHostController asli (rememberNavController) — TestNavHostController
 * tidak kompatibel dengan NavHost compose. Perilaku navigasi diverifikasi
 * lewat konten yang tampil di layar.
 */
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class SessionRedirectEffectTest {

    @get:Rule
    val composeRule = createComposeRule()

    private lateinit var sessionManager : SessionManager
    private val homePom by lazy { HomePom(composeRule) }
    private val loginPom by lazy { LoginPom(composeRule) }

    @Before
    fun setup() {
        sessionManager = SessionManager()

        composeRule.setContent {
            TestGraph()
        }

        composeRule.waitForIdle()
    }

    @Composable
    private fun TestGraph() {
        val navController : NavHostController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = AllRoute.Home.root
        ) {
            composable(AllRoute.Home.root) { Text("Home") }
            composable(AllRoute.Auth.Login.route) { Text("Login") }
        }

        // SessionRedirectEffect dipasang seperti di MainActivity
        SessionRedirectEffect(
            navController = navController,
            sessionManager = sessionManager
        )
    }

    @Test
    fun `When the session is valid then the home screen is displayed`() {
        homePom.assertIsDisplayed()
    }

    @Test
    fun `When the session becomes invalid then the login screen is displayed`() {
        sessionManager.markSessionInvalid()

        loginPom.assertIsDisplayed()
    }

    @Test
    fun `When the session is restored while on the login screen then the login screen remains displayed and the home screen is hidden`() {
        sessionManager.markSessionInvalid()

        loginPom.assertIsDisplayed()

        // User logs in again -> session becomes valid -> no additional navigation
        sessionManager.markSessionValid()

        loginPom.assertIsDisplayed()
        homePom.assertDoesNotExist()
    }
}
