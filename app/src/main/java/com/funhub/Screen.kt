package com.funhub

sealed class Screen(val route: String) {
    data object Videos : Screen("videos")
    data object VideoPlayer : Screen("video/{videoId}") {
        fun createRoute(videoId: String) = "video/$videoId"
    }
    data object Images : Screen("images")
    data object ImageDetail : Screen("image/{imageId}") {
        fun createRoute(imageId: String) = "image/$imageId"
    }
    data object Clip : Screen("clip?videoId={videoId}") {
        fun createRoute(videoId: String?) = if (videoId != null) "clip?videoId=$videoId" else "clip"
    }
    data object Settings : Screen("settings")
}