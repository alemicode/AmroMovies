plugins {
    id("amro.android.library")
}

android {
    namespace = "com.alemicode.amromovies.core.domain"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:common"))
}
