import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.androidxRoom)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.sqldelight)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
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
        val androidUnitTest by getting { dependsOn(commonNativeTest) }
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
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.androidx.navigation.compose)
            implementation(libs.androidx.room.common)
            implementation(libs.compose.material3.adaptive)
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
        }
        commonNativeTest.dependencies {
            implementation(libs.androidx.room.testing)
        }

        webMain.dependencies {
            implementation(libs.sqldelight.runtime)
            implementation(libs.sqldelight.web.worker.driver)
            implementation(npm("sql.js", "1.8.0"))
            implementation(npm("@cashapp/sqldelight-sqljs-worker", "2.2.1"))
            implementation(devNpm("copy-webpack-plugin", "9.1.0"))
        }

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.koin.android)
        }
        androidUnitTest.dependencies {
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

android {
    namespace = "sokeriaaa.return0"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "sokeriaaa.return0"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "org.robolectric.RobolectricTestRunner"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

sqldelight {
    databases {
        create("SQDatabase") {
            packageName.set("sokeriaaa.return0.applib.room")
            generateAsync.set(true)
        }
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
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
