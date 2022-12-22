package com.sbfirebase.kiossku

import com.google.gson.Gson
import com.sbfirebase.kiossku.authentication.SavedAuthToken
import org.json.JSONArray
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

    @Test fun savedAuthTokenJson_isCorrect(){
        val x = SavedAuthToken("777").toString()
        val savedAuthToken = Gson().fromJson(x , SavedAuthToken::class.java)
        savedAuthToken.toString()
    }

    @Test fun jsonObject_test(){
        val x = """[
                        {
                            \"message\" : \"Halo dunia\"
                        },
                        {
                            \"message\" : \"Halo2\"
                        }
                   ]
            """
        x
        val jsonArray = JSONArray(x)
        val y = jsonArray.getJSONObject(0).toString()
        y
        //val jsonArray = jsonObject.getAsJsonArray("message")
    }

    @Test
    fun tesListConcatenation(){
        val x = mutableListOf(1 , 2 , 3 , 4 , 5)
        val y = listOf(6 , 7 , 8 , 9)
        val v = x + y
    }
}