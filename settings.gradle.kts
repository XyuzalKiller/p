pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        maven("https://maven.aliucord.com/releases")
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.aliucord.com/releases")
    }
}

rootProject.name = "My Plugins"
rootDir.resolve("plugins").listFiles {
    if (it.isDirectory && it.resolve("build.gradle.kts").exists()) {
        include(":plugins:${it.name}")
    }

    false
 }
