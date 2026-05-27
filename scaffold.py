import os

modules = [
    "core/domain:com.dehar.player.core.domain:CoreDomain",
    "core/testing:com.dehar.player.core.testing:CoreTesting",
    "feature/home:com.dehar.player.feature.home:Home",
    "feature/browser:com.dehar.player.feature.browser:Browser",
    "feature/video-player:com.dehar.player.feature.videoplayer:VideoPlayer",
    "feature/music-player:com.dehar.player.feature.musicplayer:MusicPlayer",
    "feature/music-library:com.dehar.player.feature.musiclibrary:MusicLibrary",
    "feature/lyrics:com.dehar.player.feature.lyrics:Lyrics",
    "feature/ringtone-editor:com.dehar.player.feature.ringtoneeditor:RingtoneEditor",
    "feature/subtitle:com.dehar.player.feature.subtitle:Subtitle",
    "feature/equalizer:com.dehar.player.feature.equalizer:Equalizer",
    "feature/settings:com.dehar.player.feature.settings:Settings",
    "feature/cloud-drive:com.dehar.player.feature.clouddrive:CloudDrive",
    "feature/smb:com.dehar.player.feature.smb:Smb",
    "feature/cast:com.dehar.player.feature.cast:Cast",
    "feature/transfer:com.dehar.player.feature.transfer:Transfer",
    "feature/torrent:com.dehar.player.feature.torrent:Torrent",
    "feature/usb:com.dehar.player.feature.usb:Usb",
    "feature/private-folder:com.dehar.player.feature.privatefolder:PrivateFolder",
    "feature/media-manager:com.dehar.player.feature.mediamanager:MediaManager",
    "feature/whatsapp-status:com.dehar.player.feature.whatsappstatus:WhatsappStatus",
    "feature/video-editor:com.dehar.player.feature.videoeditor:VideoEditor",
    "feature/tv:com.dehar.player.feature.tv:Tv",
    "player/core:com.dehar.player.player.core:PlayerCore",
    "player/service:com.dehar.player.player.service:PlayerService"
]

base_dir = r"E:\Dehar Player update\DeharPlayer"

for m in modules:
    parts = m.split(":")
    path = parts[0]
    pkg = parts[1]
    class_name = parts[2]
    
    module_dir = os.path.join(base_dir, path.replace("/", os.sep))
    src_dir = os.path.join(module_dir, "src", "main", "java", pkg.replace(".", os.sep))
    
    os.makedirs(src_dir, exist_ok=True)
    
    build_content = f"""plugins {{
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}}

android {{
    namespace = "{pkg}"
    compileSdk = 36

    defaultConfig {{
        minSdk = 24
    }}
    compileOptions {{
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }}
    kotlinOptions {{
        jvmTarget = "17"
    }}
    buildFeatures {{
        compose = true
    }}
}}

dependencies {{
    implementation(projects.core.common)
    implementation(libs.androidx.core.ktx)
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.material3)
    implementation(libs.compose.foundation)
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
}}
"""
    with open(os.path.join(module_dir, "build.gradle.kts"), "w", encoding="utf-8") as f:
        f.write(build_content)

    if path.startswith("feature"):
        ui_content = f"""package {pkg}

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun {class_name}Screen() {{
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {{
        Text(text = "{class_name} Feature")
    }}
}}
"""
        with open(os.path.join(src_dir, f"{class_name}Screen.kt"), "w", encoding="utf-8") as f:
            f.write(ui_content)

print("Scaffolding complete.")
