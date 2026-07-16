plugins {
    id("amro.android.feature")
    id("amro.room")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.alemicode.amromovies.feature.movies"
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

dependencies {
    implementation(project(":core:network"))

    implementation(libs.retrofit.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.datetime)
}
