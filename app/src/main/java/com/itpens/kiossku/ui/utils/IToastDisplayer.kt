package com.itpens.kiossku.ui.utils

interface IToastDisplayer {
    operator fun invoke(message: String)
    operator fun invoke(message: UiText)
}
