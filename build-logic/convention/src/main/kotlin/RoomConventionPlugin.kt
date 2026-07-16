import com.alemicode.amromovies.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Applies KSP and wires Room's runtime/ktx/compiler artifacts. Schema export location is
 * left to the consuming module's own `build.gradle.kts` (e.g. `core:database`) since it's
 * genuinely per-module, not something a shared convention should hardcode.
 */
class RoomConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.google.devtools.ksp")

            dependencies {
                add("implementation", libs.findLibrary("androidx-room-runtime").get())
                add("implementation", libs.findLibrary("androidx-room-ktx").get())
                add("ksp", libs.findLibrary("androidx-room-compiler").get())

                add("testImplementation", libs.findLibrary("androidx-room-testing").get())
            }
        }
    }
}
