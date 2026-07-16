plugins {
    id("amro.android.library")
    id("amro.koin")
    alias(libs.plugins.kover)
}

android {
    namespace = "com.alemicode.amromovies.core.common"
}
