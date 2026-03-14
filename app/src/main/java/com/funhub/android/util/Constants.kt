package com.funhub.android.util

/**
 * 应用常量定义
 */
object Constants {

    // API 配置
    const val DEFAULT_API_URL = "http://10.0.2.2:5000/api"
    const val API_TIMEOUT_SECONDS = 30L

    // 分页配置
    const val DEFAULT_PAGE_SIZE = 20
    const val MAX_PAGE_SIZE = 100

    // 缓存配置
    const val CACHE_EXPIRY_HOURS = 24

    // 播放器配置
    const val VIDEO_PROGRESS_SYNC_INTERVAL_MS = 10000L // 10 秒
    const val SHORT_VIDEO_PRELOAD_COUNT = 2

    // 图片配置
    const val SLIDESHOW_DEFAULT_INTERVAL_SECONDS = 5
    const val SLIDESHOW_MIN_INTERVAL_SECONDS = 1
    const val SLIDESHOW_MAX_INTERVAL_SECONDS = 30

    // UI 配置
    const val ANIMATION_DURATION_MS = 300
    const val SCROLL_TO_TOP_THRESHOLD = 500

    // DataStore 键
    object DataStoreKeys {
        const val API_URL = "api_url"
        const val DARK_MODE = "dark_mode"
        const val AUTO_PLAY = "auto_play"
        const val SLIDESHOW_INTERVAL = "slideshow_interval"
        const val SHORT_VIDEO_MODE = "short_video_mode" // "sequential" or "random"
    }
}
