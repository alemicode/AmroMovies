package com.alemicode.amromovies.designsystem.components

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alemicode.amromovies.core.testing.paparazzi.BasePaparazziComponentTest
import com.alemicode.amromovies.core.testing.paparazzi.PaparazziFontScale
import com.alemicode.amromovies.core.testing.paparazzi.PaparazziLocaleConfig
import com.alemicode.amromovies.designsystem.theme.AmroTheme
import com.google.testing.junit.testparameterinjector.TestParameter
import com.google.testing.junit.testparameterinjector.TestParameterInjector
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class AmroPosterImageSnapshotTest(
    @TestParameter locale: PaparazziLocaleConfig,
    @TestParameter fontScale: PaparazziFontScale,
) : BasePaparazziComponentTest(locale, fontScale) {

    @Test
    fun `poster image placeholder in light theme`() {
        paparazzi.snapshot {
            AmroTheme(darkTheme = false) {
                AmroPosterImage(
                    posterUrl = null,
                    contentDescription = "Preview poster",
                    modifier = Modifier.width(120.dp).aspectRatio(2f / 3f),
                )
            }
        }
    }

    @Test
    fun `poster image placeholder in dark theme`() {
        paparazzi.snapshot {
            AmroTheme(darkTheme = true) {
                AmroPosterImage(
                    posterUrl = null,
                    contentDescription = "Preview poster",
                    modifier = Modifier.width(120.dp).aspectRatio(2f / 3f),
                )
            }
        }
    }
}
