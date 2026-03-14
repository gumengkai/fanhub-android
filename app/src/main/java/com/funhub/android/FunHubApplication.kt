package com.funhub.android

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * FunHub Android Application 入口
 * 
 * 使用 Hilt 进行依赖注入
 */
@HiltAndroidApp
class FunHubApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        
        // 初始化全局配置
        instance = this
    }

    companion object {
        lateinit var instance: FunHubApplication
            private set
    }
}
