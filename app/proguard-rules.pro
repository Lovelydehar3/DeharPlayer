# Proguard rules for Dehar Player

# Keep Compose/Material3 rules
-keepclassmembers class * extends androidx.compose.runtime.snapshots.SnapshotState { *; }

# Keep Media3/ExoPlayer rules
-keep class androidx.media3.common.** { *; }
-keep class androidx.media3.exoplayer.** { *; }
-keep class androidx.media3.ui.** { *; }
-dontwarn androidx.media3.common.**
-dontwarn androidx.media3.exoplayer.**
-dontwarn androidx.media3.ui.**

# Keep our data classes
-keep class com.dehar.player.data.** { *; }
-keepclassmembers class com.dehar.player.data.** { *; }
