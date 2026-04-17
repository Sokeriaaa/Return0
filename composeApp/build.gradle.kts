import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.androidxRoom3)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
}

kotlin {
    android {
        // AGP 9.0: New DSL for host tests (Unit Tests)
        withHostTest {
            isIncludeAndroidResources = true
        }
        namespace = "sokeriaaa.return0.composeapp"
        compileSdk = libs.versions.android.compileSdk.get().toInt()

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
        androidResources {
            enable = true
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    jvm()

    js {
        browser()
        binaries.executable()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }

    sourceSets {
        val commonMain by getting
        val commonTest by getting
        // Shared source set for Android, iOS and JVM.
        val commonNativeMain by creating { dependsOn(commonMain) }
        val commonNativeTest by creating { dependsOn(commonTest) }

        // Platforms
        val androidMain by getting { dependsOn(commonNativeMain) }
        // AGP 9.0: Access the renamed source set
        val androidHostTest by getting { dependsOn(commonNativeTest) }
        val jvmMain by getting { dependsOn(commonNativeMain) }
        val jvmTest by getting { dependsOn(commonNativeTest) }

        // iOS consolidation
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting

        val iosMain by creating {
            dependsOn(commonNativeMain)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        val iosTest by creating {
            dependsOn(commonNativeTest)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }

        // Web
        val webMain by creating { dependsOn(commonMain) }
        val webTest by creating { dependsOn(commonTest) }
        val jsMain by getting { dependsOn(webMain) }
        val jsTest by getting { dependsOn(webTest) }
        val wasmJsMain by getting { dependsOn(webMain) }
        val wasmJsTest by getting { dependsOn(webTest) }

        commonMain.dependencies {
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.androidx.navigation.compose)
            implementation(libs.androidx.room3.common)
            implementation(libs.androidx.room3.runtime)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.material3.adaptive)
            implementation(libs.compose.runtime)
            implementation(libs.compose.ui)
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.koin.compose)
            implementation(libs.koin.core)
            implementation(libs.kotlinx.atomicfu)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.materialKolor)
            implementation(libs.sugarkane.compose)
            implementation(libs.sugarkane.kelp)
        }
        commonTest.dependencies {
            implementation(libs.androidx.room3.testing)
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.sugarkane.wrench)
        }

        commonNativeMain.dependencies {
            implementation(libs.androidx.sqlite.bundled)
            implementation(libs.androidx.datastore)
            implementation(libs.androidx.datastore.preferences)
        }

        webMain.dependencies {
            implementation(libs.russhwolf.multiplatform.settings)
            implementation(libs.russhwolf.multiplatform.settings.coroutines)
            implementation(libs.russhwolf.multiplatform.settings.make.observable)
            implementation(libs.androidx.sqlite.web)
        }
        androidHostTest.dependencies {
            // For testing in Android Studio directly.
            // Solve the java.lang.UnsatisfiedLinkError: no sqliteJni in java.library.path
            implementation(libs.androidx.sqlite.bundled.jvm)

            implementation(libs.androidx.test.core)
            implementation(libs.androidx.test.ext.junit)
            implementation(libs.androidx.test.monitor)
            implementation(libs.androidx.test.runner)
            implementation(libs.robolectric)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
        }
    }
}

room3 {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    androidRuntimeClasspath(libs.compose.ui.tooling)
    // Room compiler
    listOf(
        "kspAndroid",
        "kspJvm",
        "kspIosArm64",
        "kspIosSimulatorArm64",
        "kspJs",
        "kspWasmJs",
    ).forEach {
        add(configurationName = it, dependencyNotation = libs.androidx.room3.compiler)
    }
}

compose.desktop {
    application {
        mainClass = "sokeriaaa.return0.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "sokeriaaa.return0"
            packageVersion = "1.0.0"
        }
    }
}
