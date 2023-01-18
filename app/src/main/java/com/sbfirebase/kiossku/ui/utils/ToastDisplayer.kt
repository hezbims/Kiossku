package com.sbfirebase.kiossku.ui.utils

import android.content.Context
import android.widget.Toast
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ToastDisplayer @Inject constructor(
    @ApplicationContext private val context : Context
) {
    operator fun invoke(message : String , duration : Int = Toast.LENGTH_SHORT) {
        Toast.makeText(
            context,
            message,
            duration
        ).show()
    }
}