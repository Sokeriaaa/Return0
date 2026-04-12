rootProject.name = "Return0"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/Sokeriaaa/SugarkaneKMP")
            credentials {
                username = settings.extra["github.packages.username"] as? String
                    ?: providers.gradleProperty("github.packages.username").getOrNull()
                password = settings.extra["github.packages.password"] as? String
                    ?: providers.gradleProperty("github.packages.password").getOrNull()
            }
        }
    }
}

plugins {
    // See https://splitties.github.io/refreshVersions
    id("de.fayard.refreshVersions") version "0.60.6"
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

include(":androidApp")
include(":composeApp")
include(":room2sqldelight-ksp")