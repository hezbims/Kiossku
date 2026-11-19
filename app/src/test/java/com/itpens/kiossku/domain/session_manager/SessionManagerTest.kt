@file:OptIn(ExperimentalCoroutinesApi::class)

package com.itpens.kiossku.domain.session_manager

import com.itpens.kiossku.domain.SessionManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("SessionManager")
class SessionManagerTest {

    private lateinit var sut : SessionManager

    @BeforeEach
    fun setup() {
        sut = SessionManager()
    }

    @Test
    @DisplayName("When a new SessionManager is created then the session is valid")
    fun isSessionValidWhenCreated() {
        assertTrue(sut.isSessionValid.value)
    }

    @Test
    @DisplayName("When markSessionInvalid is called then the session becomes invalid")
    fun marksSessionInvalidWhenMarkSessionInvalidIsCalled() {
        sut.markSessionInvalid()

        assertFalse(sut.isSessionValid.value)
    }

    @Test
    @DisplayName("When markSessionValid is called after the session was marked invalid then the session becomes valid again")
    fun restoresSessionWhenMarkSessionValidIsCalledAfterInvalid() {
        sut.markSessionInvalid()
        assertFalse(sut.isSessionValid.value)

        sut.markSessionValid()

        assertTrue(sut.isSessionValid.value)
    }

    @Test
    @DisplayName("When session marks are repeated with the same value then duplicate states are not re-emitted")
    fun doesNotReEmitDuplicateStatesWhenMarksAreRepeated() = runTest {
        val emissions = mutableListOf<Boolean>()

        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            sut.isSessionValid.collect { emissions.add(it) }
        }

        sut.markSessionInvalid()
        sut.markSessionInvalid()
        sut.markSessionInvalid()

        sut.markSessionValid()
        sut.markSessionValid()

        job.cancel()

        // 1 initial + 1 when it changes to false + 1 when it changes to true.
        // Repeated calls with the same value do not re-emit.
        assertEquals(listOf(true, false, true), emissions)
    }

    @Test
    @DisplayName("When a collector subscribes after the session was marked invalid then it receives the latest invalid state")
    fun receivesLatestStateWhenCollectorSubscribesAfterInvalid() = runTest {
        sut.markSessionInvalid()

        assertFalse(sut.isSessionValid.first())
    }
}
