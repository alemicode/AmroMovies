plugins {
    id("amro.android.library")
    id("amro.koin")
}

android {
    namespace = "com.alemicode.amromovies.core.network"
}

dependencies {
    implementation(project(":core:common"))

    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlinx.serialization.converter)
    implementation(libs.okhttp.core)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.kotlinx.serialization.json)
}
