package com.funhub.domain.model

data class AppSettings(
    val serverAddress: String = "",
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val useDynamicColor: Boolean = false
)

enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK
}
