import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.androidxRoom)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.sqldelight)
}

kotlin {
    android {
        // AGP 9.0: New DSL for host tests (Unit Tests)
        withHostTest {
            isIncludeAndroidResources = true
        }
    }

    androidLibrary {
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
        iosX64(),
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
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting

        val iosMain by creating {
            dependsOn(commonNativeMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        val iosTest by creating {
            dependsOn(commonNativeTest)
            iosX64Test.dependsOn(this)
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
            implementation(libs.androidx.room.common)
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
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }

        commonNativeMain.dependencies {
            implementation(libs.androidx.room.runtime)
            implementation(libs.androidx.sqlite.bundled)
            implementation(libs.androidx.datastore)
            implementation(libs.androidx.datastore.preferences)
        }
        commonNativeTest.dependencies {
            implementation(libs.androidx.room.testing)
        }

        webMain.dependencies {
            implementation(libs.russhwolf.multiplatform.settings)
            implementation(libs.russhwolf.multiplatform.settings.coroutines)
            implementation(libs.russhwolf.multiplatform.settings.make.observable)
            implementation(libs.sqldelight.runtime)
            implementation(libs.sqldelight.web.worker.driver)
            implementation(npm("sql.js", "1.8.0"))
            implementation(npm("@cashapp/sqldelight-sqljs-worker", "2.2.1"))
            implementation(devNpm("copy-webpack-plugin", "9.1.0"))
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

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    androidRuntimeClasspath(compose.uiTooling)
    // Room compiler
    listOf(
        "kspAndroid",
        "kspJvm",
        "kspIosX64",
        "kspIosArm64",
        "kspIosSimulatorArm64",
    ).forEach {
        add(configurationName = it, dependencyNotation = libs.androidx.room.compiler)
    }
    listOf(
        "kspAndroid",
        "kspJvm",
        "kspIosX64",
        "kspIosArm64",
        "kspIosSimulatorArm64",
        "kspJs",
        "kspWasmJs",
    ).forEach {
        add(configurationName = it, dependencyNotation = project(":room2sqldelight-ksp"))
    }
}

sqldelight {
    databases {
        create("SQDatabase") {
            packageName.set("sokeriaaa.return0.applib.room")
            srcDirs(
                // TODO Configure paths.
                "build/generated/ksp/android/androidDebug/resources/sqldelight",
                "src/commonMain/sqldelight",
            )
            generateAsync.set(true)
        }
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
