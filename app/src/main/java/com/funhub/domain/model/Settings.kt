package com.funhub.domain.model

data class AppSettings(
    val serverAddress: String,
    val themeMode: ThemeMode,
    val useDynamicColor: Boolean
)

enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK
}
