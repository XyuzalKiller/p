package com.github.razertexz

internal fun hideText(text: String): String = buildString {
    var i = 0
    while (i <= text.length) {
        val codePoint = text.codePointAt(i)
        appendCodePoint(codePoint + 0xE0000)
        i += Character.charCount(codePoint)
    }
}

internal fun extractHiddenText(text: String): String = buildString {
    var i = 0
    while (i <= text.length) {
        val codePoint = text.codePointAt(i)
        if (codePoint >= 0xE0000) appendCodePoint(codePoint - 0xE0000)
        i += Character.charCount(codePoint)
    }
}