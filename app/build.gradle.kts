plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.dehar.player"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.dehar.player"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            val keystorePath = System.getenv("RELEASE_KEYSTORE_PATH") ?: ""
            val keystorePassword = System.getenv("RELEASE_KEYSTORE_PASSWORD") ?: ""
            val keyAliasEnv = System.getenv("RELEASE_KEY_ALIAS") ?: ""
            val keyPasswordEnv = System.getenv("RELEASE_KEY_PASSWORD") ?: ""

            if (keystorePath.isNotEmpty()) {
                storeFile = file(keystorePath)
                storePassword = keystorePassword
                keyAlias = keyAliasEnv
                keyPassword = keyPasswordEnv
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
        debug {
            isMinifyEnabled = false
            isDebuggable = true
        }
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }
    
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs += listOf(
            "-opt-in=kotlin.RequiresOptIn",
            "-Xcontext-receivers",
            "-Xjvm-default=all"
        )
    }
    
    buildFeatures {
        compose = true
        buildConfig = true
        viewBinding = false
    }
    
    bundle {
        language { enableSplit = true }
        density { enableSplit = true }
        abi { enableSplit = true }
    }
    
    packaging {
        resources {
            excludes += listOf(
                "/META-INF/{AL2.0,LGPL2.1}",
                "/*.kotlin_module",
                "META-INF/versions/9/OSGI-INF/MANIFEST.MF",
                "META-INF/DEPENDENCIES",
                "META-INF/LICENSE",
                "META-INF/LICENSE.txt",
                "META-INF/NOTICE",
                "META-INF/NOTICE.txt",
                "META-INF/*.SF",
                "META-INF/*.DSA",
                "META-INF/*.RSA"
            )
        }
    }
}

dependencies {
    coreLibraryDesugaring(libs.android.desugar.jdk.libs)

    // Project Modules
    implementation(projects.core.ui)
    implementation(projects.core.data)
    implementation(projects.core.domain)
    implementation(projects.core.common)

    implementation(projects.feature.browser)
    implementation(projects.feature.home)
    implementation(projects.feature.videoPlayer)
    implementation(projects.feature.musicPlayer)
    implementation(projects.feature.musicLibrary)
    implementation(projects.feature.lyrics)
    implementation(projects.feature.ringtoneEditor)
    implementation(projects.feature.subtitle)
    implementation(projects.feature.equalizer)
    implementation(projects.feature.settings)
    implementation(projects.feature.privateFolder)
    implementation(projects.feature.mediaManager)
    
    implementation(projects.player.core)
    implementation(projects.player.service)

    // Android Core & Lifecycle
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)

    // Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    debugImplementation(libs.compose.ui.tooling)
    implementation(libs.compose.material3)
    implementation(libs.compose.material.icons.extended)
    implementation(libs.compose.animation)
    implementation(libs.compose.foundation)
    implementation(libs.navigation.compose)

    // DI
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.hilt.navigation.compose)

    // Image Loading
    implementation(libs.coil.compose)
    implementation(libs.coil.network)
    implementation(libs.coil.video)
    
    // Key-Value Storage
    implementation(libs.mmkv)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.room.runtime)
    
    // Splash
    implementation(libs.splashscreen)

    // Media3 / ExoPlayer
    implementation(libs.media3.exoplayer)
    implementation(libs.media3.exoplayer.hls)
    implementation(libs.media3.exoplayer.dash)
    implementation(libs.media3.ui)
    implementation(libs.media3.session)
    // Note: FFmpeg decoder for media3 is not available in standard repos
    // implementation(libs.media3.exoplayer.ffmpeg)


    // Home screen widget
    implementation(libs.glance.appwidget)
    implementation(libs.glance.material3)

    // Torrent streaming - Note: version 2.0.0 not available, use available versions or skip for now
    // implementation(libs.torrentstream.android)

    // SMB network shares
    implementation(libs.smbj)

    // FFmpeg for ringtone editor + video trim - Note: version 6.0 not available in standard repos
    // implementation(libs.ffmpeg.kit.full)

    // Biometric vault
    implementation(libs.biometric)

    // Charset detection for subtitles
    implementation("com.googlecode.juniversalchardet:juniversalchardet:1.0.3")
}
