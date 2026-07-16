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
 * Base convention for every plain Kotlin/Android library module: applies the Android Library
 * plugin, shared SDK/Java config, JUnit5 as the unit-test runner, and the common test dependency
 * set (JUnit5, MockK, Turbine, AssertK, coroutines-test).
 *
 * `core:*` modules apply this directly; `feature:*` modules get it transitively via
 * [AndroidFeatureConventionPlugin].
 */
class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // AGP 9's "Built-in Kotlin" (https://developer.android.com/r/tools/built-in-kotlin)
            // compiles Kotlin sources itself and registers its own `kotlin` extension - applying
            // org.jetbrains.kotlin.android on top of that throws a duplicate-extension error.
            pluginManager.apply("com.android.library")

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
