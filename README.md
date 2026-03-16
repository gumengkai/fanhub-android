# FunHub Android 客户端

轻量级私人媒体中心的 Android 客户端应用。

## 功能特性

- **视频库**: 浏览、播放、收藏视频
- **图片库**: 浏览、收藏图片
- **视频剪辑**: 选择视频片段进行剪辑
- **设置**: 服务器地址配置、主题设置

## 技术栈

- **语言**: Kotlin
- **UI框架**: Jetpack Compose + Material Design 3
- **架构**: MVVM + Clean Architecture
- **依赖注入**: Hilt
- **网络**: Retrofit + OkHttp
- **本地存储**: Room + DataStore
- **图片加载**: Coil
- **视频播放**: ExoPlayer (Media3)

## 快速开始

### 部署到GitHub并自动构建

#### 方法1: 使用部署脚本（推荐）

```bash
# 运行部署脚本（需要GitHub用户名和Token）
/home/gmk/deploy_to_github.sh <github_username> <github_token>

# 示例
/home/gmk/deploy_to_github.sh myusername ghp_xxxxxxxxxxxx
```

#### 方法2: 手动部署

1. **在GitHub创建仓库**
   - 访问 https://github.com/new
   - 仓库名: `fanhub-android`
   - 选择 Public

2. **推送代码**
   ```bash
   cd /home/gmk/fanhub-android
   git remote add origin https://github.com/YOUR_USERNAME/fanhub-android.git
   git branch -M main
   git push -u origin main
   ```

3. **查看构建状态**
   - 访问 `https://github.com/YOUR_USERNAME/fanhub-android/actions`
   - 等待构建完成

4. **下载APK**
   - 在Actions页面找到最新的工作流运行
   - 下载 `debug-apk` 或 `release-apk`

## 项目结构

```
app/src/main/java/com/funhub/
├── data/           # 数据层
│   ├── local/      # Room数据库
│   ├── remote/     # Retrofit API
│   └── repository/ # Repository实现
├── di/             # Hilt模块
├── domain/         # 领域层
│   ├── model/      # 领域模型
│   ├── repository/ # Repository接口
│   └── usecase/    # 用例
├── ui/             # UI层
│   ├── videos/     # 视频模块
│   ├── images/     # 图片模块
│   ├── clips/      # 剪辑模块
│   ├── settings/   # 设置模块
│   ├── components/ # 通用组件
│   └── theme/      # 主题配置
├── App.kt          # Application类
├── FunHubApp.kt    # 主应用组件
├── MainActivity.kt # 主Activity
└── Screen.kt       # 导航路由
```

## 本地构建

```bash
# 构建Debug APK
./gradlew assembleDebug

# 构建Release APK
./gradlew assembleRelease

# 运行单元测试
./gradlew testDebugUnitTest
```

## 配置说明

首次运行应用时，需要在设置中配置FunHub服务器地址：

1. 打开应用，点击底部"设置"标签
2. 在"服务器设置"中输入服务器地址
3. 点击"保存"

默认服务器地址: `http://192.168.1.100:5000`

## 依赖的FunHub API

- `GET /api/health` - 健康检查
- `GET /api/videos` - 获取视频列表
- `GET /api/videos/{id}/stream` - 视频流播放
- `GET /api/clips/{id}/info` - 获取视频信息
- `POST /api/clips/{id}/clip` - 剪辑视频

## GitHub Actions CI

项目配置了GitHub Actions自动构建：
- 推送到main分支自动触发构建
- 自动构建Debug和Release APK
- APK文件作为Artifacts上传

查看构建状态: [Actions](../../actions)

## 代码统计

- **Kotlin文件**: 52个
- **总代码行数**: ~3,400行
- **配置文件**: 16个

## 许可证

MIT License