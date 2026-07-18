package com.alemicode.amromovies.core.testing.paparazzi

import app.cash.paparazzi.DeviceConfig

enum class PaparazziDeviceSize(val deviceConfig: DeviceConfig) {
    PHONE(DeviceConfig.PIXEL_9),
    TABLET(DeviceConfig.PIXEL_TABLET),
}
