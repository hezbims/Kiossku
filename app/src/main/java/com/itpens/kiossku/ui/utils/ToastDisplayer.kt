package com.itpens.kiossku.ui.utils

import android.content.Context
import android.widget.Toast
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ToastDisplayer @Inject constructor(
    @ApplicationContext private val context: Context
) : IToastDisplayer {
    override fun invoke(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun invoke(message: UiText) {
        invoke(message.asString(context))
    }
}
