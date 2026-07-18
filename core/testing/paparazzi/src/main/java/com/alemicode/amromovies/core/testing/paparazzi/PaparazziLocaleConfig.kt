package com.alemicode.amromovies.core.testing.paparazzi

import com.android.resources.LayoutDirection

enum class PaparazziLocaleConfig(val locale: String, val layoutDirection: LayoutDirection) {
    ENGLISH("en", LayoutDirection.LTR),
    GERMAN("de", LayoutDirection.LTR),
    FARSI("fa", LayoutDirection.RTL),
}
