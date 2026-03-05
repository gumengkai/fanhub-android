# FanHub Android

FanHub 的 Android 客户端，采用最新的 Android 开发规范，基于 Jetpack Compose 和 Material Design 3 构建。

[![Android Build](https://github.com/your-username/fanhub-android/actions/workflows/build.yml/badge.svg)](https://github.com/your-username/fanhub-android/actions/workflows/build.yml)
[![API](https://img.shields.io/badge/API-26%2B-brightgreen.svg)](https://android-arsenal.com/api?level=26)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.22-purple.svg)](https://kotlinlang.org)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

---

## 📱 功能特性

- 🎬 **视频库** - 浏览、搜索、播放视频
- 🖼️ **图片库** - 浏览、幻灯片播放图片
- 📱 **短视频** - 抖音风格沉浸式播放
- 💗 **收藏** - 统一管理视频和图片收藏
- ⚙️ **设置** - 灵活配置后端地址
- 🎨 **Material 3** - 遵循最新 Material Design 3 设计规范
- 🌙 **深色模式** - 自动适配系统深色模式

---

## 🏗️ 技术栈

### 核心架构
- **语言**: Kotlin 1.9.22
- **最低 API**: 26 (Android 8.0)
- **目标 API**: 34 (Android 14)
- **架构模式**: MVVM + Clean Architecture

### UI 框架
- **UI**: Jetpack Compose (Material 3)
- **导航**: Navigation Compose
- **主题**: 自定义 Bilibili 粉色主题

### 依赖注入
- **框架**: Hilt 2.50
- **KSP**: Kotlin Symbol Processing

### 网络与数据
- **网络**: Retrofit 2 + OkHttp 4
- **JSON**: Gson
- **图片加载**: Coil 2.5
- **本地存储**: DataStore Preferences

### 媒体播放
- **视频播放**: ExoPlayer (Media3) 1.2.0

### 异步处理
- **协程**: Kotlinx Coroutines 1.7.3
- **Flow**: StateFlow + SharedFlow

---

## 🚀 快速开始

### 环境要求
- Android Studio Hedgehog (2023.1.1) 或更高版本
- JDK 17
- Android SDK 34

### 编译步骤

1. **克隆项目**
```bash
git clone https://github.com/your-username/fanhub-android.git
cd fanhub-android
```

2. **配置后端地址**

编辑 `app/build.gradle.kts`，修改 `DEFAULT_API_URL`：
```kotlin
buildConfigField("String", "DEFAULT_API_URL", "\"http://你的服务器地址:8080/api\"")
```

3. **同步 Gradle**
在 Android Studio 中点击 "Sync Project with Gradle Files"

4. **运行应用**
选择设备或模拟器，点击 Run 按钮

---

## 📦 项目结构

```
app/
├── src/main/
│   ├── java/com/fanhub/android/
│   │   ├── data/
│   │   │   ├── local/          # 本地数据源 (DataStore)
│   │   │   ├── model/          # 数据模型
│   │   │   ├── remote/         # 远程数据源 (Retrofit)
│   │   │   └── repository/     # 数据仓库实现
│   │   ├── di/                 # Hilt 依赖注入模块
│   │   ├── ui/
│   │   │   ├── components/     # 可复用 Compose 组件
│   │   │   ├── navigation/     # 导航配置
│   │   │   ├── screens/        # 页面屏幕
│   │   │   │   ├── home/
│   │   │   │   ├── videos/
│   │   │   │   ├── images/
│   │   │   │   ├── favorites/
│   │   │   │   └── settings/
│   │   │   ├── theme/          # Material 主题配置
│   │   │   └── viewmodel/      # ViewModel
│   │   ├── FanHubApplication.kt
│   │   └── MainActivity.kt
│   ├── res/
│   │   ├── values/             # 资源文件
│   │   ├── drawable/           # 图片资源
│   │   └── mipmap/             # 应用图标
│   └── AndroidManifest.xml
├── build.gradle.kts
└── proguard-rules.pro
```

---

## 🔧 构建配置

### Gradle 版本
```properties
# gradle/wrapper/gradle-wrapper.properties
distributionUrl=https\://services.gradle.org/distributions/gradle-8.4-bin.zip
```

### Android 配置
```kotlin
android {
    namespace = "com.fanhub.android"
    compileSdk = 34
    
    defaultConfig {
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"
    }
    
    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    
    kotlinOptions {
        jvmTarget = "17"
    }
}
```

---

## 🎨 主题定制

FanHub 使用自定义的 Bilibili 粉色主题：

```kotlin
// 主题色
private val Pink = Color(0xFFFB7299)
private val PinkVariant = Color(0xFFE05A7D)
private val DarkPink = Color(0xFFD44D6E)

// 支持动态颜色 (Android 12+)
// 支持深色模式自动切换
```

---

## 🔌 API 接口

完整 API 请参考后端服务：

| 功能 | 端点 | 方法 |
|------|------|------|
| 视频列表 | `/api/videos` | GET |
| 视频详情 | `/api/videos/{id}` | GET |
| 视频播放 | `/api/videos/{id}/stream` | GET |
| 切换收藏 | `/api/videos/{id}/favorite` | POST |
| 图片列表 | `/api/images` | GET |
| 图片文件 | `/api/images/{id}/file` | GET |
| 标签管理 | `/api/tags` | GET |

---

## 📝 开发进度

- [x] 项目基础架构
- [x] Gradle 配置优化
- [x] 数据层（Model + API + Repository）
- [x] Hilt 依赖注入
- [x] 主题配置（Material 3）
- [x] 主屏幕
- [x] 导航系统
- [x] GitHub Actions CI/CD
- [ ] 视频播放器完整实现
- [ ] 图片库页面完整实现
- [ ] 短视频页面完整实现
- [ ] 收藏页面完整实现
- [ ] 设置页面（后端地址配置）
- [ ] 单元测试
- [ ] UI 测试

---

## 🤝 贡献指南

欢迎贡献代码、报告问题或提出建议！

1. Fork 本项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

---

## 📄 许可证

MIT License - 详见 [LICENSE](LICENSE) 文件

---

## 🙏 致谢

- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Material Design 3](https://m3.material.io/)
- [Hilt](https://dagger.dev/hilt/)
- [Retrofit](https://square.github.io/retrofit/)
- [Coil](https://coil-kt.github.io/coil/)
- [ExoPlayer](https://exoplayer.dev/)

---

<div align="center">

**FanHub Android** - 让媒体管理更简单

⭐ 如果这个项目对你有帮助，请给个 Star！

</div>
