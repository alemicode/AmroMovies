import com.alemicode.amromovies.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/** Adds the Koin BOM + core artifact, plus its test module, to any module that declares DI bindings. */
class KoinConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val bom = libs.findLibrary("koin-bom").get()
            dependencies {
                add("implementation", platform(bom))
                add("implementation", libs.findLibrary("koin-core").get())

                add("testImplementation", platform(bom))
                add("testImplementation", libs.findLibrary("koin-test").get())
                add("testImplementation", libs.findLibrary("koin-test-junit5").get())
            }
        }
    }
}
