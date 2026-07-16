import com.alemicode.amromovies.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

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
                add("implementation", project(":core:common"))

                add("implementation", libs.findLibrary("androidx-lifecycle-runtime-ktx").get())
                add("implementation", libs.findLibrary("androidx-navigation-compose").get())
                add("implementation", libs.findLibrary("koin-androidx-compose").get())

                add("testImplementation", project(":core:testing"))
            }
        }
    }
}
