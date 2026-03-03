# FanHub Android

FanHub 的 Android 客户端，采用最新的 Android 开发规范。

## 📱 功能特性

- 🎬 **视频库** - 浏览、搜索、播放视频
- 🖼️ **图片库** - 浏览、幻灯片播放图片
- 📱 **短视频** - 抖音风格沉浸式播放
- 💗 **收藏** - 统一管理视频和图片收藏
- ⚙️ **设置** - 灵活配置后端地址

## 🏗️ 技术栈

- **语言**: Kotlin
- **UI**: Jetpack Compose (Material 3)
- **架构**: MVVM + Clean Architecture
- **依赖注入**: Hilt
- **网络**: Retrofit + OkHttp
- **图片加载**: Coil
- **视频播放**: ExoPlayer (Media3)
- **导航**: Navigation Compose

## 🚀 在线编译

### 方式一：GitHub Actions（推荐）

1. 将本项目推送到 GitHub
2. 进入 **Actions** 标签页
3. 点击左侧 **Android Build**
4. 点击 **Run workflow** 按钮
5. 等待编译完成（约 5-10 分钟）
6. 在 **Artifacts** 部分下载 APK 文件

### 方式二：手动触发

```bash
# 推送到 GitHub
git add .
git commit -m "更新代码"
git push origin main

# 或者创建标签触发 Release 构建
git tag v1.0.0
git push origin v1.0.0
```

## ⚙️ 配置后端地址

### 方法 1：修改默认地址

编辑 `app/build.gradle.kts`：

```kotlin
buildConfigField("String", "DEFAULT_API_URL", "\"http://你的服务器地址:8080/api\"")
```

### 方法 2：运行时配置

在 App 的设置页面中配置后端地址（开发中）。

## 📦 项目结构

```
app/
├── src/main/
│   ├── java/com/fanhub/android/
│   │   ├── data/
│   │   │   ├── model/          # 数据模型
│   │   │   ├── remote/         # API 接口
│   │   │   ├── local/          # 本地存储
│   │   │   └── repository/     # 数据仓库
│   │   ├── domain/
│   │   │   ├── model/          # 领域模型
│   │   │   ├── repository/     # 仓库接口
│   │   │   └── usecase/        # 用例
│   │   ├── ui/
│   │   │   ├── screens/        # 页面
│   │   │   ├── components/     # 组件
│   │   │   ├── theme/          # 主题
│   │   │   ├── viewmodel/      # ViewModel
│   │   │   └── navigation/     # 导航
│   │   ├── di/                 # 依赖注入
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

## 🔑 API 接口

完整 API 请参考后端服务：

| 功能 | 端点 |
|------|------|
| 视频列表 | `GET /api/videos` |
| 视频详情 | `GET /api/videos/{id}` |
| 视频播放 | `GET /api/videos/{id}/stream` |
| 图片列表 | `GET /api/images` |
| 图片文件 | `GET /api/images/{id}/file` |
| 收藏管理 | `POST /api/videos|images/{id}/favorite` |
| 标签管理 | `GET /api/tags` |

## 📝 开发进度

- [x] 项目基础架构
- [x] 数据层（Model + API + Repository）
- [x] 主题配置
- [x] 主屏幕
- [x] 视频库页面
- [x] GitHub Actions 配置
- [ ] 视频播放器
- [ ] 图片库页面
- [ ] 短视频页面
- [ ] 收藏页面
- [ ] 设置页面（后端地址配置）

## 📄 许可证

MIT License
