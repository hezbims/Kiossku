package com.sbfirebase.kiossku.home

import androidx.compose.material.Surface
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.sbfirebase.kiossku.ui.screen.home.FilterState
import com.sbfirebase.kiossku.ui.screen.home.HomeViewModel
import com.sbfirebase.kiossku.ui.screen.home.filter.FilterLayout
import com.sbfirebase.kiossku.ui.theme.KiosskuTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FilterTest {

    private lateinit var homeViewModel : HomeViewModel

    @get:Rule
    val composeTestRule = createComposeRule()
    // use createAndroidComposeRule<YourActivity>() if you need access to
    // an activity

    @Before
    fun prepare(){
        homeViewModel = HomeViewModel(
            daerahRepository = ,
            getAllProduct =
        )
    }

    @Test
    fun myTest() {
        // Start the app
        composeTestRule.setContent {
            KiosskuTheme {
                Surface {
                    FilterLayout(
                        showFilter = true,
                        filterState = FilterState(),
                        onEvent = {}
                    )
                }
            }
        }

        composeTestRule.onNodeWithText("lahan").performClick().assertExists()
    }
}