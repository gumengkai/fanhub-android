# FunHub Android Client

<div align="center">

📱 FunHub 官方 Android 客户端 - 让本地媒体管理更有趣

[![Platform](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0-purple.svg)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Compose-1.6-blue.svg)](https://developer.android.com/jetpack/compose)
[![Material](https://img.shields.io/badge/Material-3-pink.svg)](https://m3.material.io)

</div>

---

## 📖 项目简介

FunHub Android 客户端是一个基于 Jetpack Compose 和 Material Design 3 的现代化 Android 应用，提供与 FunHub Web 端一致的媒体管理体验。

### ✨ 核心特性

- 🎬 **视频库** - 浏览、搜索、播放本地视频
- 🖼️ **图片库** - 浏览、全屏查看图片
- 📱 **短视频模式** - 抖音风格沉浸式播放
- 🔗 **动态配置** - 支持配置后端服务器地址
- 🎨 **Material Design 3** - 现代化 UI 设计
- 🌙 **深色模式** - 自动适配系统主题
- 📡 **离线缓存** - 支持本地数据缓存

---

## 🏗️ 技术架构

### 技术栈

| 组件 | 技术 | 版本 |
|------|------|------|
| 语言 | Kotlin | 2.0+ |
| UI 框架 | Jetpack Compose | 1.6+ |
| 设计规范 | Material Design 3 | 1.2+ |
| 依赖注入 | Hilt | 2.51+ |
| 网络请求 | Retrofit + OkHttp | 2.11+ |
| 图片加载 | Coil | 2.6+ |
| 视频播放 | ExoPlayer | 2.19+ |
| 本地存储 | DataStore | 1.0+ |
| 导航 | Navigation Compose | 2.7+ |
| 异步 | Kotlin Coroutines | 1.8+ |

### 架构模式

```
┌─────────────────────────────────────────────────────────┐
│                    UI Layer (Compose)                    │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐      │
│  │   Screens   │  │  ViewModels │  │    State    │      │
│  └─────────────┘  └─────────────┘  └─────────────┘      │
├─────────────────────────────────────────────────────────┤
│                  Domain Layer (Optional)                 │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐      │
│  │  Use Cases  │  │   Models    │  │  Repository │      │
│  └─────────────┘  └─────────────┘  └─────────────┘      │
├─────────────────────────────────────────────────────────┤
│                   Data Layer                             │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐      │
│  │  API Service│  │   Repository│  │  DataStore  │      │
│  │  (Retrofit) │  │   Impl      │  │  (Settings) │      │
│  └─────────────┘  └─────────────┘  └─────────────┘      │
└─────────────────────────────────────────────────────────┘
```

---

## 📁 项目结构

```
funhub-android/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/funhub/android/
│   │   │   │   ├── FunHubApplication.kt      # Application 入口
│   │   │   │   ├── di/                       # Hilt 依赖注入
│   │   │   │   │   ├── AppModule.kt
│   │   │   │   │   └── NetworkModule.kt
│   │   │   │   ├── data/                     # 数据层
│   │   │   │   │   ├── api/                  # API 服务
│   │   │   │   │   │   ├── FunHubApi.kt
│   │   │   │   │   │   └── models/           # API 数据模型
│   │   │   │   │   ├── repository/           # 数据仓库
│   │   │   │   │   │   └── MediaRepository.kt
│   │   │   │   │   └── local/                # 本地存储
│   │   │   │   │       └── SettingsManager.kt
│   │   │   │   ├── domain/                   # 领域层
│   │   │   │   │   └── models/               # 领域模型
│   │   │   │   ├── ui/                       # UI 层
│   │   │   │   │   ├── theme/                # 主题配置
│   │   │   │   │   │   ├── Theme.kt
│   │   │   │   │   │   ├── Color.kt
│   │   │   │   │   │   └── Type.kt
│   │   │   │   │   ├── navigation/           # 导航
│   │   │   │   │   │   └── NavGraph.kt
│   │   │   │   │   ├── screens/              # 页面
│   │   │   │   │   │   ├── home/             # 首页
│   │   │   │   │   │   ├── video/            # 视频相关
│   │   │   │   │   │   │   ├── VideoLibrary.kt
│   │   │   │   │   │   │   ├── VideoPlayer.kt
│   │   │   │   │   │   │   └── ShortVideo.kt
│   │   │   │   │   │   ├── image/            # 图片相关
│   │   │   │   │   │   │   └── ImageLibrary.kt
│   │   │   │   │   │   └── settings/         # 设置
│   │   │   │   │   │       └── Settings.kt
│   │   │   │   │   └── components/           # 通用组件
│   │   │   │   │       ├── MediaCard.kt
│   │   │   │   │       ├── TopAppBar.kt
│   │   │   │   │       └── BottomNavBar.kt
│   │   │   │   └── util/                     # 工具类
│   │   │   │       ├── Constants.kt
│   │   │   │       └── Extensions.kt
│   │   │   ├── res/
│   │   │   │   ├── values/
│   │   │   │   │   ├── strings.xml
│   │   │   │   │   ├── colors.xml
│   │   │   │   │   └── themes.xml
│   │   │   │   ├── drawable/
│   │   │   │   └── mipmap-*/
│   │   │   └── AndroidManifest.xml
│   │   └── test/                              # 单元测试
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── gradle/
│   └── libs.versions.toml                     # 版本目录
├── build.gradle.kts                           # 项目级构建配置
├── settings.gradle.kts                        # 项目设置
├── gradle.properties
├── gradlew
├── gradlew.bat
├── .gitignore
└── README.md
```

---

## 🚀 快速开始

### 环境要求

- Android Studio Hedgehog (2023.1.1) 或更高版本
- JDK 17+
- Android SDK 24+ (最低支持版本)
- Target SDK 34+

### 构建步骤

1. **克隆项目**
```bash
git clone <your-repo-url> funhub-android
cd funhub-android
```

2. **打开项目**
- 使用 Android Studio 打开项目
- 等待 Gradle 同步完成

3. **配置后端地址**
- 首次启动应用后，在设置页面配置 FunHub 后端地址
- 默认地址：`http://10.0.2.2:5000` (Android 模拟器访问本地主机)

4. **运行应用**
- 选择目标设备或模拟器
- 点击 Run 按钮

---

## 📱 功能模块

### 首页 (Home)
- 仪表盘展示
- 最近播放
- 收藏内容
- 快速入口

### 视频库 (Video Library)
- 视频列表浏览
- 搜索和筛选
- 分页加载
- 收藏管理

### 视频播放器 (Video Player)
- 全屏播放
- 进度控制
- 倍速播放
- 播放进度记忆
- 手势控制

### 短视频 (Short Video)
- 沉浸式全屏
- 上下滑动切换
- 快捷收藏
- 随机/顺序播放

### 图片库 (Image Library)
- 图片网格浏览
- 全屏查看
- 幻灯片播放
- 缩放控制

### 设置 (Settings)
- 后端地址配置
- 缓存管理
- 主题切换
- 关于信息

---

## 🔌 API 接口

### 基础配置

```kotlin
// 默认 API 地址
val DEFAULT_API_URL = "http://10.0.2.2:5000/api"

// 实际设备访问
// 同一局域网：http://<服务器 IP>:5000/api
// 远程访问：http://<公网 IP>:5000/api
```

### 核心 API

| 端点 | 方法 | 描述 |
|------|------|------|
| `/videos` | GET | 获取视频列表 |
| `/videos/{id}` | GET | 获取视频详情 |
| `/videos/{id}/stream` | GET | 视频流播放 |
| `/images` | GET | 获取图片列表 |
| `/images/{id}/file` | GET | 获取原图 |
| `/favorites` | GET | 获取收藏列表 |
| `/history` | GET | 获取播放历史 |

---

## 🎨 设计规范

### 颜色主题

```kotlin
// 主色调 (Bilibili 粉色)
val FunHubPink = Color(0xFFFB7299)

// 深色模式
val DarkBackground = Color(0xFF1A1A2E)
val DarkSurface = Color(0xFF16213E)
```

### 组件规范

- 遵循 Material Design 3 设计规范
- 使用动态颜色 (Dynamic Color) 支持
- 支持深色模式自动切换
- 最小触摸目标 48dp

---

## 📦 构建发布版本

### 生成签名 APK

```bash
# 配置签名 (在 gradle.properties 或本地配置)
# storeFile=<path-to-keystore>
# storePassword=<password>
# keyAlias=<alias>
# keyPassword=<password>

# 生成 Release APK
./gradlew assembleRelease

# 生成 Android App Bundle (推荐)
./gradlew bundleRelease
```

### 输出位置

- APK: `app/build/outputs/apk/release/`
- AAB: `app/build/outputs/bundle/release/`

---

## 🧪 测试

```bash
# 运行单元测试
./gradlew test

# 运行仪器测试
./gradlew connectedAndroidTest

# 生成测试报告
./gradlew jacocoTestReport
```

---

## 📋 待开发功能

- [ ] 视频缓存
- [ ] 后台播放
- [ ] 投屏功能 (Chromecast)
- [ ] 多服务器管理
- [ ] 下载管理
- [ ] 弹幕功能
- [ ] 用户系统
- [ ] 推送通知

---

## 📄 许可证

MIT License

---

## 🔗 相关链接

- [FunHub 后端](https://github.com/your-repo/funhub)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Material Design 3](https://m3.material.io)
- [Hilt](https://dagger.dev/hilt/)

---

<div align="center">

**FunHub Android** - 让本地媒体管理更有趣 🎉

</div>
