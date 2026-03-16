# FunHub Android Client

FunHub Android客户端是一个使用Kotlin + Jetpack Compose开发的私人媒体中心应用。

## 功能特性

- **视频库管理**: 浏览、播放、收藏视频
- **图片库管理**: 浏览图片，支持收藏
- **视频剪辑**: 使用FFmpeg进行无损视频剪辑
- **动态主题**: 支持Material Design 3动态主题
- **深色/浅色模式**: 支持系统、浅色、深色三种主题模式

## 技术栈

- **语言**: Kotlin
- **UI框架**: Jetpack Compose
- **架构**: MVVM + Clean Architecture
- **依赖注入**: Hilt
- **网络请求**: Retrofit + OkHttp
- **图片加载**: Coil
- **视频播放**: ExoPlayer (Media3)
- **本地缓存**: Room
- **状态管理**: Kotlin Coroutines + Flow

## 项目结构

```
app/src/main/java/com/funhub/
├── data/
│   ├── local/          # Room数据库
│   ├── remote/         # Retrofit API
│   └── repository/     # Repository实现
├── di/                 # Hilt模块
├── domain/
│   ├── model/          # 领域模型
│   ├── repository/     # Repository接口
│   └── usecase/        # UseCase
├── ui/
│   ├── videos/         # 视频模块
│   ├── images/         # 图片模块
│   ├── clips/          # 剪辑模块
│   ├── settings/       # 设置模块
│   ├── components/     # 通用组件
│   ├── navigation/     # 导航配置
│   └── theme/          # 主题配置
├── App.kt              # 应用入口
└── MainActivity.kt     # 主Activity
```

## 后端API

- `GET /api/health` - 健康检查
- `GET /api/videos` - 获取视频列表
- `GET /api/videos/{id}/stream` - 视频流播放
- `GET /api/clips/{id}/info` - 获取视频信息（用于剪辑）
- `POST /api/clips/{id}/clip` - 剪辑视频
- `GET /api/clips/check` - 检查FFmpeg可用性
- `GET /api/images` - 获取图片列表

## 构建

```bash
# 构建Debug版本
./gradlew assembleDebug

# 构建Release版本
./gradlew assembleRelease

# 运行测试
./gradlew test
```

## CI/CD

项目配置了GitHub Actions自动构建流水线，包括：
- 代码检查 (lint)
- 单元测试
- Debug APK构建
- Release APK构建

## 许可证

MIT License
