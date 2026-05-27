pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "DeharPlayer"

// App module
include(":app")

// Core modules
include(":core:ui")
include(":core:data")
include(":core:domain")
include(":core:common")
include(":core:testing")

// Feature modules
include(":feature:home")
include(":feature:browser")
include(":feature:video-player")
include(":feature:music-player")
include(":feature:music-library")
include(":feature:lyrics")
include(":feature:ringtone-editor")
include(":feature:subtitle")
include(":feature:equalizer")
include(":feature:settings")
include(":feature:cloud-drive")
include(":feature:smb")
include(":feature:cast")
include(":feature:transfer")
include(":feature:torrent")
include(":feature:usb")
include(":feature:private-folder")
include(":feature:media-manager")
include(":feature:whatsapp-status")
include(":feature:video-editor")
include(":feature:tv")

// Player modules
include(":player:core")
include(":player:service")