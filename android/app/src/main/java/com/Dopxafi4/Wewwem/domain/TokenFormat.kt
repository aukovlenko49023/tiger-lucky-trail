package com.Dopxafi4.Wewwem.domain

fun formatTokens(value: Int): String {
    val digits = value.coerceAtLeast(0).toString()
    val builder = StringBuilder()
    digits.forEachIndexed { index, char ->
        if (index > 0 && (digits.length - index) % 3 == 0) {
            builder.append(',')
        }
        builder.append(char)
    }
    return builder.toString()
}
