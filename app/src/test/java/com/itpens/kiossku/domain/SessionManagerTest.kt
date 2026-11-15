@file:OptIn(ExperimentalCoroutinesApi::class)

package com.itpens.kiossku.domain

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
    fun setup(){
        sut = SessionManager()
    }

    @Test
    @DisplayName("isSessionValid default true (app baru dibuka dianggap sesi valid)")
    fun defaultSessionIsValid(){
        assertTrue(sut.isSessionValid.value)
    }

    @Test
    @DisplayName("markSessionInvalid membuat isSessionValid menjadi false")
    fun markSessionInvalid(){
        sut.markSessionInvalid()

        assertFalse(sut.isSessionValid.value)
    }

    @Test
    @DisplayName("markSessionValid mengembalikan isSessionValid menjadi true setelah invalid")
    fun markSessionValidRestores(){
        sut.markSessionInvalid()
        assertFalse(sut.isSessionValid.value)

        sut.markSessionValid()

        assertTrue(sut.isSessionValid.value)
    }

    @Test
    @DisplayName("StateFlow skip emisi nilai sama (idempotent, tidak emisi ulang)")
    fun idempotentMarksDoNotReEmit() = runTest {
        val emissions = mutableListOf<Boolean>()

        val job = launch(UnconfinedTestDispatcher(testScheduler)){
            sut.isSessionValid.collect{ emissions.add(it) }
        }

        sut.markSessionInvalid()
        sut.markSessionInvalid()
        sut.markSessionInvalid()

        sut.markSessionValid()
        sut.markSessionValid()

        job.cancel()

        // 1 initial + 1 saat berubah ke false + 1 saat berubah ke true.
        // Pemanggilan berulang dengan nilai sama tidak mengemisi ulang.
        assertEquals(listOf(true, false, true), emissions)
    }

    @Test
    @DisplayName("Collector menerima state terakhir walau baru subscribe kemudian")
    fun lateCollectorGetsLatestState() = runTest {
        sut.markSessionInvalid()

        assertEquals(false, sut.isSessionValid.first())
    }
}
