plugins {
    id("amro.android.library")
}

android {
    namespace = "com.alemicode.amromovies.core.testing.paparazzi"
}

dependencies {
    api(libs.junit)
    api(libs.junit.vintage.engine)
    api(libs.paparazzi)
    api(libs.test.parameter.injector)
}
