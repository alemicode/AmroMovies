plugins {
    id("amro.android.library")
    id("amro.android.compose")
    alias(libs.plugins.paparazzi)
    alias(libs.plugins.kover)
}

android {
    namespace = "com.alemicode.amromovies.designsystem"
}

dependencies {
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    testImplementation(project(":core:testing:paparazzi"))
}
