package com.idimi.garage.view.compose.util

import android.content.Context
import android.os.Build
import coil.ImageLoader
import coil.decode.ImageDecoderDecoder
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.util.DebugLogger
import com.idimi.garage.GarageApp
import com.idimi.garage.util.isNetworkAvailable
import kotlinx.coroutines.Dispatchers

object BuildImageLoader {

    init {

    }

    val imageLoader: ImageLoader = ImageLoader.Builder(GarageApp.getAppContext())
        .components {
            add(ImageDecoderDecoder.Factory())
        }.respectCacheHeaders(isNetworkAvailable(GarageApp.getAppContext())).build()

    fun buildImageCache() {
        imageLoader.newBuilder()
            .memoryCachePolicy(CachePolicy.ENABLED)
            .memoryCache {
                MemoryCache.Builder(GarageApp.getAppContext())
                    .maxSizeBytes(500 * 1024 * 1024)//250MB
                    .maxSizePercent(0.5)
                    .weakReferencesEnabled(true)
                    .strongReferencesEnabled(true)
                    .build()
            }
            .diskCachePolicy(CachePolicy.ENABLED)
            .diskCache {
                DiskCache.Builder()
                    .maxSizeBytes(1500L * 1024 * 1024)//1.5GB
                    .maxSizePercent(0.05)
                    .directory(GarageApp.getAppContext().cacheDir.resolve("image_cache"))//(cacheDir)
                    .build()
            }
            // Will not respect Image Network URLs when internet connection if off - it will rely only on the Disc Cache
            .respectCacheHeaders(isNetworkAvailable(GarageApp.getAppContext()))
            .logger(DebugLogger())
            .build()
    }

    suspend fun fillImageCache(imageURL: String?) {
        imageURL?.let {
            val request = ImageRequest.Builder(GarageApp.getAppContext())
                .data(imageURL)
                .dispatcher(Dispatchers.IO)
                .memoryCacheKey(imageURL)
                .diskCacheKey(imageURL)
                .diskCachePolicy(CachePolicy.ENABLED)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .build()
            val req = imageLoader.execute(request)//.enqueue(request)
        }
    }
}