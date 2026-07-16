plugins {
    id("amro.android.library")
    id("amro.room")
}

android {
    namespace = "com.alemicode.amromovies.core.database"
}

dependencies {
    implementation(project(":core:model"))
}
