package com.sbfirebase.kiossku

import com.google.gson.JsonParser
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test fun jsonObject_test(){
        val jsonString = "{\"message\":\"Password or email not match\"}"
        val jsonObject = JsonParser().parse(jsonString).asJsonObject

        val message = jsonObject.get("message").asString
        assertEquals("Password or email not match" , message)
    }

    @Test
    fun tesListConcatenation(){
        val x = mutableListOf(1 , 2 , 3 , 4 , 5)
        val y = listOf(6 , 7 , 8 , 9)
        val v = x + y
    }
}