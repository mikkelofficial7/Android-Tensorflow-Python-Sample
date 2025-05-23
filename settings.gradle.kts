pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://chaquo.com/maven") }
    }
    plugins {
        id("com.chaquo.python") version "16.1.0"  // Updated to the latest version
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://chaquo.com/maven/") }
    }
}

rootProject.name = "AndroidWithPython"
include(":app")
