plugins {
    id("amro.android.library")
}

android {
    namespace = "com.alemicode.amromovies.core.testing"
}

dependencies {
    // Exposed as `api`: this module's whole purpose is to hand its consumers' test source
    // sets a ready-to-use MockK/Turbine/AssertK/JUnit5 toolbox plus shared, feature-agnostic
    // rules (e.g. MainDispatcherRule). It deliberately has no project dependencies of its own -
    // feature-specific fakes (e.g. a FakeMoviesRepository) live inside that feature module's own
    // test sources, not here, since core:testing can't depend back on a feature module.
    api(libs.junit.jupiter.api)
    api(libs.mockk)
    api(libs.turbine)
    api(libs.assertk)
    api(libs.kotlinx.coroutines.test)
}
