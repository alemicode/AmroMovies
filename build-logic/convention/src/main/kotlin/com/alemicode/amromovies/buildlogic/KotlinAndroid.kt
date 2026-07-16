package com.alemicode.amromovies.buildlogic

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

private const val COMPILE_SDK = 36
private const val MIN_SDK = 24

/**
 * Baseline SDK/Java/Kotlin config shared by every Android library module.
 * Single source of truth so bumping compile/min SDK is a one-line change here, not N edits.
 */
internal fun Project.configureKotlinAndroid(
    libraryExtension: LibraryExtension,
) {
    libraryExtension.apply {
        compileSdk = COMPILE_SDK

        defaultConfig {
            minSdk = MIN_SDK
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }
    }

    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
}
