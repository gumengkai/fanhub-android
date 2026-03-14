# FunHub Android 构建指南

## 📋 目录

- [本地构建](#本地构建)
- [GitHub Actions CI/CD](#github-actions-cicd)
- [发布流程](#发布流程)
- [故障排除](#故障排除)

---

## 本地构建

### 环境要求

1. **Android Studio** (推荐最新稳定版)
   - 下载：https://developer.android.com/studio

2. **JDK 17+**
   ```bash
   # 检查 Java 版本
   java -version
   
   # 如果版本过低，需要升级
   ```

3. **Android SDK**
   - SDK Platform: Android 14 (API 34)
   - Build Tools: 34.0.0+

### 构建步骤

1. **克隆项目**
   ```bash
   git clone <your-repo-url> funhub-android
   cd funhub-android
   ```

2. **使用 Android Studio**
   - 打开 Android Studio
   - File → Open → 选择 `funhub-android` 目录
   - 等待 Gradle 同步完成
   - Build → Build Bundle(s) / APK(s) → Build APK(s)

3. **使用命令行**
   ```bash
   # 授予执行权限 (Linux/macOS)
   chmod +x gradlew
   
   # 构建 Debug APK
   ./gradlew assembleDebug
   
   # 构建 Release APK (需要签名配置)
   ./gradlew assembleRelease
   
   # 构建 Android App Bundle (推荐用于 Play Store)
   ./gradlew bundleRelease
   
   # 运行测试
   ./gradlew test
   
   # 清理构建
   ./gradlew clean
   ```

### 输出位置

```
app/build/outputs/apk/
├── debug/app-debug.apk
└── release/app-release.apk

app/build/outputs/bundle/
└── release/app-release.aab
```

---

## GitHub Actions CI/CD

### 自动构建

项目已配置 GitHub Actions 工作流，在以下情况自动触发：

- **Push** 到 `main` 或 `develop` 分支
- **Pull Request** 到 `main` 或 `develop` 分支
- **Tag** 推送 (自动创建 Release)

### 工作流说明

#### 1. CI 构建 (`.github/workflows/android.yml`)

```yaml
on:
  push:
    branches: ["main", "develop"]
  pull_request:
    branches: ["main", "develop"]
```

**执行步骤：**
1.  checkout 代码
2.  设置 JDK 17
3.  运行 `./gradlew build`
4.  运行 `./gradlew test`
5.  构建 Debug APK
6.  上传 Debug APK 为 Artifact

#### 2. Release 发布

当推送版本标签时自动创建 Release：

```bash
# 创建并推送标签
git tag v1.0.0
git push origin v1.0.0
```

**执行步骤：**
1.  构建 Release APK
2.  构建 Release AAB
3.  创建 GitHub Release
4.  上传 APK 和 AAB 文件

### 查看构建状态

1.  进入 GitHub 仓库页面
2.  点击 **Actions** 标签
3.  选择对应的工作流查看详细信息

### 下载构建产物

**Debug APK：**
1.  进入 Actions → 对应的工作流运行
2.  在页面底部找到 **Artifacts**
3.  点击 `app-debug` 下载

**Release APK/AAB：**
1.  进入仓库 **Releases** 页面
2.  选择对应版本
3.  下载 Assets 中的文件

---

## 发布流程

### 1. 版本准备

```bash
# 更新版本号 (app/build.gradle.kts)
# versionCode = 2
# versionName = "1.1.0"

# 提交更改
git add .
git commit -m "release: v1.1.0"

# 创建标签
git tag v1.1.0
git push origin main --tags
```

### 2. 配置签名 (Release 版本)

在 `gradle.properties` 中配置签名信息：

```properties
# 签名配置 (不要提交到版本控制!)
RELEASE_STORE_FILE=/path/to/keystore.jks
RELEASE_STORE_PASSWORD=your_password
RELEASE_KEY_ALIAS=your_alias
RELEASE_KEY_PASSWORD=your_password
```

在 `app/build.gradle.kts` 中添加：

```kotlin
android {
    signingConfigs {
        create("release") {
            storeFile = file(System.getenv("RELEASE_STORE_FILE") ?: "debug.keystore")
            storePassword = System.getenv("RELEASE_STORE_PASSWORD") ?: ""
            keyAlias = System.getenv("RELEASE_KEY_ALIAS") ?: "androiddebugkey"
            keyPassword = System.getenv("RELEASE_KEY_PASSWORD") ?: ""
        }
    }
    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
        }
    }
}
```

### 3. GitHub Secrets 配置

在 GitHub 仓库设置中添加 Secrets：

1.  Settings → Secrets and variables → Actions
2.  添加以下 Secrets：
    - `RELEASE_STORE_FILE` (Base64 编码的 keystore 文件)
    - `RELEASE_STORE_PASSWORD`
    - `RELEASE_KEY_ALIAS`
    - `RELEASE_KEY_PASSWORD`

---

## 故障排除

### 常见问题

#### 1. Gradle 同步失败

```bash
# 清理 Gradle 缓存
./gradlew clean --refresh-dependencies

# 删除 .gradle 目录
rm -rf .gradle/

# 重新同步
```

#### 2. SDK 未找到

在 Android Studio 中：
- File → Project Structure → SDK Location
- 确保 Android SDK 路径正确

#### 3. 内存不足

在 `gradle.properties` 中增加内存：

```properties
org.gradle.jvmargs=-Xmx4096m -Dfile.encoding=UTF-8
```

#### 4. 构建速度慢

```bash
# 启用 Gradle 守护进程
# 启用配置缓存
./gradlew build --configuration-cache

# 使用离线模式 (依赖已缓存时)
./gradlew build --offline
```

### 日志查看

```bash
# 查看详细日志
./gradlew build --info

# 查看调试日志
./gradlew build --debug

# 查看特定任务日志
./gradlew assembleDebug --stacktrace
```

---

## 相关链接

- [Android 开发者文档](https://developer.android.com)
- [GitHub Actions 文档](https://docs.github.com/actions)
- [Gradle 文档](https://docs.gradle.org)
- [Jetpack Compose 文档](https://developer.android.com/jetpack/compose)

---

<div align="center">

**FunHub Android** - 让本地媒体管理更有趣 🎉

</div>
