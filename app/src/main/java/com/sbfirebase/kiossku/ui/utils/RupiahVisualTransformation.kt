package com.sbfirebase.kiossku.ui.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText

fun rupiahVisualTransformation(value : String) =
    TransformedText(
        text = value.formatToRupiah(),
        offsetMapping = RupiahOffsetMapping
    )

fun String.formatToRupiah() : AnnotatedString =
    buildAnnotatedString {
        append("Rp")
        this@formatToRupiah.forEachIndexed { index, c ->
            append(c)
            if (index + 1 != this@formatToRupiah.length &&
                (this@formatToRupiah.length - 1 - index) % 3 == 0)
                append(".")
        }
    }

private object RupiahOffsetMapping : OffsetMapping {
    override fun originalToTransformed(offset: Int): Int {
        return if (offset <= 3) offset + 2
        else offset + 2 + (offset - 1) / 3
    }

    override fun transformedToOriginal(offset: Int): Int {
        return if (offset <= 2) 0
        else if (offset <= 5) offset - 2
        else 3 * (offset - 2) / 4 + 1
    }
}