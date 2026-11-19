package com.itpens.kiossku.ui.navigation.pageobject

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithText

class LoginPom(private val composeRule : ComposeTestRule) {

    private val loginNode = composeRule.onNodeWithText("Login")

    fun assertIsDisplayed() {
        composeRule.waitForIdle()
        loginNode.assertIsDisplayed()
    }

    fun assertDoesNotExist() {
        composeRule.waitForIdle()
        loginNode.assertDoesNotExist()
    }
}
