package com.itpens.kiossku.ui.navigation.pageobject

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithText

class HomePom(private val composeRule : ComposeTestRule) {

    private val homeNode = composeRule.onNodeWithText("Home")

    fun assertIsDisplayed() {
        composeRule.waitForIdle()
        homeNode.assertIsDisplayed()
    }

    fun assertDoesNotExist() {
        composeRule.waitForIdle()
        homeNode.assertDoesNotExist()
    }
}
