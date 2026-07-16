import com.alemicode.amromovies.buildlogic.configureKotlinAndroid
import com.alemicode.amromovies.buildlogic.libs
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType

/**
 * Base convention for every plain Kotlin/Android library module: applies the Android Library +
 * Kotlin Android plugins, shared SDK/Java config, JUnit5 as the unit-test runner, and the
 * common test dependency set (JUnit5, MockK, Turbine, AssertK, coroutines-test).
 *
 * `core:*` modules apply this directly; `feature:*` modules get it transitively via
 * [AndroidFeatureConventionPlugin].
 */
class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
            }

            tasks.withType<Test>().configureEach {
                useJUnitPlatform()
            }

            dependencies {
                add("implementation", libs.findLibrary("kotlinx-coroutines-core").get())

                add("testImplementation", libs.findLibrary("junit-jupiter-api").get())
                add("testRuntimeOnly", libs.findLibrary("junit-jupiter-engine").get())
                add("testImplementation", libs.findLibrary("junit-jupiter-params").get())
                add("testImplementation", libs.findLibrary("mockk").get())
                add("testImplementation", libs.findLibrary("turbine").get())
                add("testImplementation", libs.findLibrary("assertk").get())
                add("testImplementation", libs.findLibrary("kotlinx-coroutines-test").get())
            }
        }
    }
}
