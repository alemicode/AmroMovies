plugins {
    id("amro.android.library")
}

android {
    namespace = "com.alemicode.amromovies.core.testing"
}

dependencies {
    api(libs.junit.jupiter.api)
    api(libs.mockk)
    api(libs.turbine)
    api(libs.assertk)
    api(libs.kotlinx.coroutines.test)
}
