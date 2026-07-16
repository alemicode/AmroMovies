plugins {
    id("amro.android.library")
    id("amro.koin")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.alemicode.amromovies.core.data"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:common"))
    implementation(project(":core:network"))
    implementation(project(":core:database"))
    implementation(project(":core:domain"))

    implementation(libs.kotlinx.serialization.json)

    testImplementation(project(":core:testing"))
}
