package com.itpens.kiossku.ui.screen.authentication.register.pageobject

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import com.itpens.kiossku.R
import com.itpens.kiossku.ui.screen.authentication.register.RegisterTestTags
import org.robolectric.RuntimeEnvironment

class RegisterPom(
    private val composeRule : ComposeTestRule,
    context : Context = RuntimeEnvironment.getApplication()
) {

    private val fullNameField = composeRule.onNodeWithText(
        context.getString(R.string.register_full_name_placeholder)
    )
    private val teleponField = composeRule.onNodeWithText(
        context.getString(R.string.register_phone_label),
        substring = true
    )
    private val emailField = composeRule.onNodeWithText(
        context.getString(R.string.register_email_placeholder)
    )
    private val passwordField = composeRule.onNodeWithText(
        context.getString(R.string.register_password_placeholder)
    )
    private val confirmPasswordField = composeRule.onNodeWithText(
        context.getString(R.string.register_confirm_password_placeholder)
    )
    private val registerButton = composeRule.onNode(
        hasText(context.getString(R.string.register_submit_button)) and hasClickAction()
    )
    private val loginNode = composeRule.onNodeWithText(LOGIN_DESTINATION_TEXT)

    fun actionTypeFullName(value : String) {
        fullNameField.performScrollTo().performTextInput(value)
        composeRule.waitForIdle()
    }

    fun actionTypeTelepon(value : String) {
        teleponField.performScrollTo().performTextInput(value)
        composeRule.waitForIdle()
    }

    fun actionTypeEmail(value : String) {
        emailField.performScrollTo().performTextInput(value)
        composeRule.waitForIdle()
    }

    fun actionTypePassword(value : String) {
        passwordField.performScrollTo().performTextInput(value)
        composeRule.waitForIdle()
    }

    fun actionTypeConfirmPassword(value : String) {
        confirmPasswordField.performScrollTo().performTextInput(value)
        composeRule.waitForIdle()
    }

    fun actionClickRegister() {
        registerButton.performScrollTo().performClick()
        composeRule.waitForIdle()
    }

    fun assertFullNameErrorIsDisplayed(message : String) {
        assertFieldErrorIsDisplayed(
            message = message,
            containerTag = RegisterTestTags.FULL_NAME_FIELD_CONTAINER
        )
    }

    fun assertTeleponErrorIsDisplayed(message : String) {
        assertFieldErrorIsDisplayed(
            message = message,
            containerTag = RegisterTestTags.TELEPON_FIELD_CONTAINER
        )
    }

    fun assertEmailErrorIsDisplayed(message : String) {
        assertFieldErrorIsDisplayed(
            message = message,
            containerTag = RegisterTestTags.EMAIL_FIELD_CONTAINER
        )
    }

    fun assertPasswordErrorIsDisplayed(message : String) {
        assertFieldErrorIsDisplayed(
            message = message,
            containerTag = RegisterTestTags.PASSWORD_FIELD_CONTAINER
        )
    }

    fun assertConfirmPasswordErrorIsDisplayed(message : String) {
        assertFieldErrorIsDisplayed(
            message = message,
            containerTag = RegisterTestTags.CONFIRM_PASSWORD_FIELD_CONTAINER
        )
    }

    fun assertGeneralErrorIsDisplayed(message : String) {
        val matcher = hasText(message)

        composeRule.waitUntil(5_000) {
            composeRule.onAllNodes(matcher).fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNode(matcher).assertIsDisplayed()
        composeRule.waitForIdle()
    }

    fun assertLoginScreenIsDisplayed() {
        composeRule.waitUntil(5_000) {
            composeRule.onAllNodes(hasText(LOGIN_DESTINATION_TEXT))
                .fetchSemanticsNodes()
                .isNotEmpty()
        }
        loginNode.assertIsDisplayed()
        composeRule.waitForIdle()
    }

    fun assertLoginScreenIsNotDisplayed() {
        loginNode.assertDoesNotExist()
        composeRule.waitForIdle()
    }

    fun assertRegisterScreenIsNotDisplayed() {
        registerButton.assertDoesNotExist()
        composeRule.waitForIdle()
    }

    private fun assertFieldErrorIsDisplayed(
        message : String,
        containerTag : String
    ) {
        val matcher = hasText(message) and hasAnyAncestor(hasTestTag(containerTag))

        composeRule.waitUntil(5_000) {
            composeRule.onAllNodes(matcher).fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNode(matcher).assertIsDisplayed()
        composeRule.waitForIdle()
    }

    private companion object {
        const val LOGIN_DESTINATION_TEXT = "Login"
    }
}
