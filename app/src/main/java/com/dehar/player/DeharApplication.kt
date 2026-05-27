package com.dehar.player

import android.app.Application
import android.os.StrictMode
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.disk.DiskCache
import coil3.disk.directory
import coil3.memory.MemoryCache
import coil3.network.okhttp.OkHttpNetworkFetcher
import com.tencent.mmkv.MMKV
import dagger.hilt.android.HiltAndroidApp
import okhttp3.OkHttpClient
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Application entry point for Dehar Player
 * Initializes Hilt DI, MMKV, Coil image loader, and other global services
 */
@HiltAndroidApp
class DeharApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize MMKV for fast key-value storage
        MMKV.initialize(this)
        
        // Set up strict mode in debug builds to catch main thread violations
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .penaltyDeathOnNetwork()
                    .build()
            )
            StrictMode.setVmPolicy(
                StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build()
            )
        }
        
        // Initialize Coil image loader with custom configuration
        initializeCoil()
    }
    
    /**
     * Configure Coil image loader for optimal performance
     */
    private fun initializeCoil() {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
        
        val imageLoader = ImageLoader.Builder(this)
            .components {
                add(OkHttpNetworkFetcher(okHttpClient))
            }
            .memoryCache {
                MemoryCache.Builder()
                    .maxSizePercent(this@DeharApplication, percent = 0.20)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(File(cacheDir, "coil_cache"))
                    .maxSizeBytes(256L * 1024 * 1024)  // 256 MB
                    .build()
            }
            .crossfade(durationMillis = 200)
            .build()
        
        SingletonImageLoader.set { imageLoader }
    }
}
