package com.alemicode.amromovies.core.testing.paparazzi

import app.cash.paparazzi.Paparazzi
import org.junit.Rule

abstract class BasePaparazziMatrixTest(
    deviceSize: PaparazziDeviceSize,
    locale: PaparazziLocaleConfig,
    fontScale: PaparazziFontScale = PaparazziFontScale.DEFAULT,
) {

    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = deviceConfigOf(deviceSize, locale, fontScale))
}
