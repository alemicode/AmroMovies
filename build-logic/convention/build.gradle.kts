plugins {
    `kotlin-dsl`
}

group = "com.alemicode.amromovies.buildlogic"

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidLibrary") {
            id = "amro.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidCompose") {
            id = "amro.android.compose"
            implementationClass = "AndroidComposeConventionPlugin"
        }
        register("androidFeature") {
            id = "amro.android.feature"
            implementationClass = "AndroidFeatureConventionPlugin"
        }
        register("koin") {
            id = "amro.koin"
            implementationClass = "KoinConventionPlugin"
        }
        register("room") {
            id = "amro.room"
            implementationClass = "RoomConventionPlugin"
        }
    }
}
