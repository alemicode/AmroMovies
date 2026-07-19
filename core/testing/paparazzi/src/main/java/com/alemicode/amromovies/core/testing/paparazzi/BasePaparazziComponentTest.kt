package com.alemicode.amromovies.core.testing.paparazzi

abstract class BasePaparazziComponentTest(
    locale: PaparazziLocaleConfig,
    fontScale: PaparazziFontScale = PaparazziFontScale.DEFAULT,
) : BasePaparazziMatrixTest(PaparazziDeviceSize.PHONE, locale, fontScale)
