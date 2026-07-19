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
class AmroFilterChipSnapshotTest(
    @TestParameter locale: PaparazziLocaleConfig,
    @TestParameter fontScale: PaparazziFontScale,
) : BasePaparazziComponentTest(locale, fontScale) {

    @Test
    fun `filter chip selected in light theme`() {
        paparazzi.snapshot {
            AmroTheme(darkTheme = false) {
                AmroFilterChip(label = "Action", selected = true, onClick = {})
            }
        }
    }

    @Test
    fun `filter chip selected in dark theme`() {
        paparazzi.snapshot {
            AmroTheme(darkTheme = true) {
                AmroFilterChip(label = "Action", selected = true, onClick = {})
            }
        }
    }

    @Test
    fun `filter chip unselected in light theme`() {
        paparazzi.snapshot {
            AmroTheme(darkTheme = false) {
                AmroFilterChip(label = "Action", selected = false, onClick = {})
            }
        }
    }

    @Test
    fun `filter chip unselected in dark theme`() {
        paparazzi.snapshot {
            AmroTheme(darkTheme = true) {
                AmroFilterChip(label = "Action", selected = false, onClick = {})
            }
        }
    }
}
