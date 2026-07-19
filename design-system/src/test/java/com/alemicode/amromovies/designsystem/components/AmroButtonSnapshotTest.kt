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
class AmroButtonSnapshotTest(
    @TestParameter locale: PaparazziLocaleConfig,
    @TestParameter fontScale: PaparazziFontScale,
) : BasePaparazziComponentTest(locale, fontScale) {

    @Test
    fun `button default in light theme`() {
        paparazzi.snapshot {
            AmroTheme(darkTheme = false) {
                AmroButton(label = "Add To Watchlist", onClick = {})
            }
        }
    }

    @Test
    fun `button default in dark theme`() {
        paparazzi.snapshot {
            AmroTheme(darkTheme = true) {
                AmroButton(label = "Add To Watchlist", onClick = {})
            }
        }
    }

    @Test
    fun `button loading state`() {
        paparazzi.snapshot {
            AmroTheme(darkTheme = false) {
                AmroButton(label = "Add To Watchlist", onClick = {}, loading = true)
            }
        }
    }

    @Test
    fun `button disabled state`() {
        paparazzi.snapshot {
            AmroTheme(darkTheme = false) {
                AmroButton(label = "Add To Watchlist", onClick = {}, enabled = false)
            }
        }
    }
}
