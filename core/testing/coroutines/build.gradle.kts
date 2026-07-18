plugins {
    id("amro.android.library")
}

android {
    namespace = "com.alemicode.amromovies.core.testing.coroutines"
}

dependencies {
    api(libs.junit.jupiter.api)
    api(libs.kotlinx.coroutines.test)
}
