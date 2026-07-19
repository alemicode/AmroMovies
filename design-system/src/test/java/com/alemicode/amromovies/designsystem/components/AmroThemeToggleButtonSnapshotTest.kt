package com.alemicode.amromovies.designsystem.components

import com.alemicode.amromovies.core.testing.paparazzi.BasePaparazziComponentTest
import com.alemicode.amromovies.core.testing.paparazzi.PaparazziFontScale
import com.alemicode.amromovies.core.testing.paparazzi.PaparazziLocaleConfig
import com.alemicode.amromovies.designsystem.theme.AmroTheme
import com.google.testing.junit.testparameterinjector.TestParameter
import com.google.testing.junit.testparameterinjector.TestParameterInjector
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class AmroThemeToggleButtonSnapshotTest(
    @TestParameter locale: PaparazziLocaleConfig,
    @TestParameter fontScale: PaparazziFontScale,
) : BasePaparazziComponentTest(locale, fontScale) {

    @Test
    fun `theme toggle button in light theme`() {
        paparazzi.snapshot {
            AmroTheme(darkTheme = false) {
                AmroThemeToggleButton()
            }
        }
    }

    @Test
    fun `theme toggle button in dark theme`() {
        paparazzi.snapshot {
            AmroTheme(darkTheme = true) {
                AmroThemeToggleButton()
            }
        }
    }
}
