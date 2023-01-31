package com.sbfirebase.kiossku.ui.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText

fun phoneVisualTransformation(text : AnnotatedString) =
    TransformedText(
        text = buildAnnotatedString {
            append("+62 ")
            append(text)
        },
        offsetMapping = PhoneOffsetMapping
    )

private object PhoneOffsetMapping : OffsetMapping{
    override fun originalToTransformed(offset: Int): Int {
        return offset + 4
    }

    override fun transformedToOriginal(offset: Int): Int {
        return offset - 4
    }
}