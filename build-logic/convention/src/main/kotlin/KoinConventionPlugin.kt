import com.alemicode.amromovies.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

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
