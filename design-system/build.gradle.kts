plugins {
    id("amro.android.library")
    id("amro.android.compose")
}

android {
    namespace = "com.alemicode.amromovies.designsystem"
}

dependencies {
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
}
