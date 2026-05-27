# ProGuard Rules for DeharPlayer
# Optimization, obfuscation, and security configuration for release builds

# ============================================================================
# GENERAL OPTIONS
# ============================================================================

# Enable shrinking for better APK size
# -dontshrink (commented out - we want shrinking enabled)

# Enable optimization with 5 passes
# -dontoptimize (commented out - we want optimization enabled)

# Specifies to output some useful information during processing
-verbose

# Specifies the version number of the class files
-target 8

# ============================================================================
# KEEP ANNOTATIONS
# ============================================================================

# Keep all annotations
-keepattributes *Annotation*

# Keep runtime visible annotations
-keepattributes RuntimeVisibleAnnotations
-keepattributes RuntimeInvisibleAnnotations

# Keep runtime visible parameter annotations
-keepattributes RuntimeVisibleParameterAnnotations
-keepattributes RuntimeInvisibleParameterAnnotations

# Keep line numbers for stack traces
-keepattributes SourceFile,LineNumberTable

# Keep all classes and methods that are annotated with @Keep
-keep @androidx.annotation.Keep class * { *; }
-keepclassmembers class * {
    @androidx.annotation.Keep *;
}

# ============================================================================
# ANDROID FRAMEWORK & SUPPORT LIBRARIES
# ============================================================================

# Android support library
-keep class androidx.** { *; }
-keep interface androidx.** { *; }
-keepclassmembers class * extends androidx.** {
    public <init>(...);
}

# Android Material Design
-keep class com.google.android.material.** { *; }
-keep interface com.google.android.material.** { *; }

# Android native methods
-keepclasseswithmembernames class * {
    native <methods>;
}

# Keep Activity, Service, BroadcastReceiver, ContentProvider, Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.Fragment
-keep public class * extends androidx.fragment.app.Fragment

# Keep View constructors for inflation from XML
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

# Android Manifest
-keepclasseswithmembers class * {
    public <init>(android.content.Context);
}

# Keep R classes for resources
-keepclassmembers class **.R$* {
    public static <fields>;
}

# ============================================================================
# HILT DEPENDENCY INJECTION
# ============================================================================

# Hilt generated classes
-keep class hilt_aggregated_deps.** { *; }
-keep class dagger.hilt.** { *; }
-keep class dagger.hilt.internal.** { *; }
-keep interface dagger.hilt.** { *; }

# Keep all classes with Hilt annotations
-keep @dagger.hilt.android.HiltAndroidApp class *
-keep @dagger.hilt.android.lifecycle.HiltViewModel class *
-keep @dagger.hilt.android.qualifiers.** class *

# Keep Hilt generated Factory classes
-keepclasseswithmembers class * {
    @dagger.hilt.** <fields>;
}
-keepclasseswithmembers class * {
    @dagger.hilt.** <methods>;
}

# ============================================================================
# JETPACK COMPOSE
# ============================================================================

# Compose runtime
-keep class androidx.compose.** { *; }
-keep interface androidx.compose.** { *; }
-keepclassmembers class androidx.compose.** { *; }

# Composable functions
-keep @androidx.compose.runtime.Composable class * { *; }
-keepclassmembers @androidx.compose.runtime.Composable class * { *; }

# Compose compiler generated classes
-keep class androidx.compose.compiler.** { *; }

# Keep generated Compose stability files
-keepclassmembers class ** {
    @androidx.compose.compiler.plugins.kotlin.ComposeStableMarker <fields>;
}

# ============================================================================
# KOTLIN
# ============================================================================

# Kotlin metadata
-keepattributes RuntimeVisibleAnnotations
-keep class kotlin.Metadata { *; }

# Keep Kotlin data classes
-keep class kotlin.jvm.internal.** { *; }
-keepclassmembers class * {
    *** Companion;
}

# Keep coroutines
-keep class kotlinx.coroutines.** { *; }
-keep interface kotlinx.coroutines.** { *; }
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}

# ============================================================================
# DEHAR PLAYER CORE
# ============================================================================

# Keep all DeharPlayer classes and their constructors
-keep class com.dehar.player.** { *; }
-keep interface com.dehar.player.** { *; }

# Keep all ViewModel classes
-keep class com.dehar.player.**.viewmodel.** extends androidx.lifecycle.ViewModel { *; }

# Keep all Repository classes
-keep class com.dehar.player.**.repository.** { *; }

# Keep all Model/Entity data classes
-keep class com.dehar.player.**.model.** { *; }
-keep class com.dehar.player.**.entity.** { *; }

# Keep all UI Screen components
-keep class com.dehar.player.**.ui.screen.** { *; }

# Keep all UI Components
-keep class com.dehar.player.**.ui.components.** { *; }

# Keep all Navigation related classes
-keep class com.dehar.player.navigation.** { *; }

# ============================================================================
# FEATURE MODULES - SPECIFIC KEEP RULES
# ============================================================================

# Music Player & Library
-keep class com.dehar.player.feature.musicplayer.** { *; }
-keep class com.dehar.player.feature.musiclibrary.** { *; }

# Video Player
-keep class com.dehar.player.feature.videoplayer.** { *; }

# Lyrics
-keep class com.dehar.player.feature.lyrics.** { *; }

# Subtitle
-keep class com.dehar.player.feature.subtitle.** { *; }

# Settings
-keep class com.dehar.player.feature.settings.** { *; }

# Ringtone Editor
-keep class com.dehar.player.feature.rintoneeditor.** { *; }

# SMB Browser
-keep class com.dehar.player.feature.smb.** { *; }

# USB OTG Browser
-keep class com.dehar.player.feature.usb.** { *; }

# Torrent
-keep class com.dehar.player.feature.torrent.** { *; }

# Cast
-keep class com.dehar.player.feature.cast.** { *; }

# Private Vault
-keep class com.dehar.player.feature.vault.** { *; }

# WhatsApp Status Downloader
-keep class com.dehar.player.feature.whatsappstatus.** { *; }

# Video Editor
-keep class com.dehar.player.feature.videoeditor.** { *; }

# ============================================================================
# THIRD-PARTY LIBRARIES
# ============================================================================

# Retrofit
-keep class retrofit2.** { *; }
-keep interface retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

# Gson
-keep class com.google.gson.** { *; }
-keep interface com.google.gson.** { *; }
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# OkHttp
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }

# Room Database
-keep class androidx.room.** { *; }
-keepattributes Entity
-keepattributes Dao

# WorkManager
-keep class androidx.work.** { *; }
-keepattributes Work

# Datastore
-keep class androidx.datastore.** { *; }

# Media3/ExoPlayer
-keep class androidx.media3.common.** { *; }
-keep class androidx.media3.exoplayer.** { *; }
-keep class androidx.media3.ui.** { *; }

# Cast Framework
-keep class androidx.mediarouter.** { *; }
-keep class com.google.android.gms.cast.** { *; }

# Biometric
-keep class androidx.biometric.** { *; }

# Android Security
-keep class androidx.security.** { *; }

# JCIFS-ng (SMB)
-keep class jcifs.** { *; }
-keep interface jcifs.** { *; }

# ============================================================================
# REFLECTION AND SERIALIZATION
# ============================================================================

# Keep classes that are dynamically loaded via reflection
-keepclasseswithmembers class * {
    *** *(...) throws <Throwable>;
}

# Keep enum values
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Keep parcelable classes
-keepclassmembers class * implements android.os.Parcelable {
    static android.os.Parcelable$Creator CREATOR;
}

# ============================================================================
# LOGGING AND DEBUGGING
# ============================================================================

# Keep stack traces readable
-keepattributes SourceFile
-keepattributes LineNumberTable

# ============================================================================
# OPTIMIZATION
# ============================================================================

# Optimization passes
-optimizationpasses 5

# Class merging
-mergeinterfacesaggressively

# Field optimization
-optimizeaggressively

# Keep method parameter names for better debuggability
-keepparameternames

# ============================================================================
# CRITICAL CLASSES
# ============================================================================

# Never obfuscate critical classes
-keep public class com.dehar.player.MainActivity
-keep public class com.dehar.player.App

# Keep exception classes readable
-keep class * extends java.lang.Exception
-keep class * extends java.lang.RuntimeException

# Preserve custom exceptions
-keepclasseswithmembers class * {
    public <init>(java.lang.String);
    public <init>(java.lang.String, java.lang.Throwable);
}

# ============================================================================
# KOTLIN-SPECIFIC RULES
# ============================================================================

# Kotlin extensions
-dontwarn kotlin.extensions.**
-dontwarn kotlin.internal.ir.** 

# Keep inline functions
-keepclassmembers class ** {
    @kotlin.jvm.JvmInline *;
}

# ============================================================================
# WARNINGS AND SUPPRESSIONS
# ============================================================================

-dontwarn sun.misc.Unsafe
-dontwarn com.google.common.**
-dontwarn java.lang.invoke.**
-dontwarn **$Lambda$*

# Suppress warnings for third-party libraries
-dontwarn javax.**
-dontwarn com.sun.**
-dontwarn org.apache.**
