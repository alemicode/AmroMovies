plugins {
    id("amro.android.library")
}

android {
    namespace = "com.alemicode.amromovies.core.testing.coroutines"
}

dependencies {
    api(libs.junit)
    api(libs.kotlinx.coroutines.test)
}
