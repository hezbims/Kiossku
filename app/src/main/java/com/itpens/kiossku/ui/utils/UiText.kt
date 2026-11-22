package com.itpens.kiossku.ui.utils

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed interface UiText {
    data class Dynamic(val value: String) : UiText
    data class Resource(@StringRes val resId: Int) : UiText
    data class Joined(
        val texts: List<UiText>,
        val separator: String = "\n"
    ) : UiText
}

fun UiText.asString(context: Context): String = when (this) {
    is UiText.Dynamic -> value
    is UiText.Resource -> context.getString(resId)
    is UiText.Joined -> texts.joinToString(separator) { it.asString(context) }
}

@Composable
fun UiText.asString(): String = when (this) {
    is UiText.Dynamic -> value
    is UiText.Resource -> stringResource(resId)
    is UiText.Joined -> {
        val parts = mutableListOf<String>()
        for (text in texts) {
            parts.add(text.asString())
        }
        parts.joinToString(separator)
    }
}
