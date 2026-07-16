import com.alemicode.amromovies.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

/**
 * Convention for `feature:*` modules. Bundles the Android Library + Compose + Koin conventions
 * and wires the dependencies every feature module needs by definition: [design-system], the
 * shared domain contracts, navigation, and Koin's Compose integration.
 *
 * This is the direct answer to AMRO's "future feature teams" requirement: a new feature module
 * applies a single plugin ID and is immediately buildable, themed, navigable, and DI-wired -
 * without ever depending on `core:data` or another feature's internals.
 */
class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("amro.android.library")
                apply("amro.android.compose")
                apply("amro.koin")
            }

            dependencies {
                add("implementation", project(":design-system"))
                add("implementation", project(":core:model"))
                add("implementation", project(":core:domain"))

                add("implementation", libs.findLibrary("androidx-lifecycle-runtime-ktx").get())
                add("implementation", libs.findLibrary("androidx-navigation-compose").get())
                add("implementation", libs.findLibrary("koin-androidx-compose").get())

                add("testImplementation", project(":core:testing"))
            }
        }
    }
}
