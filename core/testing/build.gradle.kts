plugins {
    id("amro.android.library")
}

android {
    namespace = "com.alemicode.amromovies.core.testing"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:domain"))

    // Exposed as `api`: this module's whole purpose is to hand its consumers' test source
    // sets a ready-to-use MockK/Turbine/AssertK/JUnit5 toolbox plus shared fakes/rules.
    api(libs.junit.jupiter.api)
    api(libs.mockk)
    api(libs.turbine)
    api(libs.assertk)
    api(libs.kotlinx.coroutines.test)
}
