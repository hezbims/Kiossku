package com.sbfirebase.kiossku.domain.use_case

import android.content.Context
import android.content.Intent
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class OpenWhatsappUseCase @Inject constructor(
    @ApplicationContext private val context : Context
) {
    operator fun invoke(nomorTelepon : String){
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(API_URL + nomorTelepon)
            `package` = PACKAGE_NAME
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
        }

        context.startActivity(intent)
    }

    private companion object {
        const val API_URL = "https://api.whatsapp.com/send?phone="
        const val PACKAGE_NAME = "com.whatsapp"
    }

}