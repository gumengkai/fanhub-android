# GitHub Actions 构建指南

## 自动构建

本项目已配置 GitHub Actions，每次推送到 `main` 或 `master` 分支时会自动构建 APK。

### 触发条件

- 推送到 `main` 或 `master` 分支
- Pull Request 到 `main` 或 `master` 分支
- 手动触发（workflow_dispatch）

### 构建产物

构建成功后，APK 文件会作为 Artifact 上传，保留 30 天。

**下载步骤**：
1. 进入项目的 Actions 标签页
2. 点击最近的构建工作流
3. 在页面底部找到 "Artifacts" 部分
4. 点击 `fanhub-android-debug` 下载 APK

---

## 手动触发构建

### 方法 1: GitHub 界面

1. 进入 **Actions** 标签页
2. 选择 **Android Build** 工作流
3. 点击 **Run workflow** 按钮
4. 选择分支（可选）
5. 点击 **Run workflow**

### 方法 2: 创建 Release Tag

```bash
# 创建版本标签
git tag v1.0.0
git push origin v1.0.0
```

---

## 本地构建

### 前置条件

- JDK 17
- Android SDK 34
- Gradle 8.4

### 构建命令

```bash
# 构建 Debug APK
./gradlew assembleDebug

# 构建 Release APK
./gradlew assembleRelease

# 清理并重新构建
./gradlew clean assembleDebug

# 运行测试
./gradlew test

# 代码格式化
./gradlew ktlintFormat
```

### 输出位置

- **Debug APK**: `app/build/outputs/apk/debug/app-debug.apk`
- **Release APK**: `app/build/outputs/apk/release/app-release.apk`

---

## 构建配置

### Gradle 版本

```properties
# gradle/wrapper/gradle-wrapper.properties
distributionUrl=https\://services.gradle.org/distributions/gradle-8.4-bin.zip
```

### Android 配置

```kotlin
android {
    compileSdk = 34
    minSdk = 26
    targetSdk = 34
    
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
}
```

### 依赖版本

| 依赖 | 版本 |
|------|------|
| Kotlin | 1.9.22 |
| AGP | 8.2.2 |
| Compose BOM | 2024.01.00 |
| Hilt | 2.50 |
| Retrofit | 2.9.0 |
| Coil | 2.5.0 |
| Media3 | 1.2.0 |

---

## 常见问题

### Q1: 构建失败，提示 "SDK not found"

**解决方案**：
```bash
# 在本地创建 local.properties 文件
echo "sdk.dir=/path/to/Android/sdk" > local.properties
```

### Q2: 构建失败，提示 "KSP 错误"

**解决方案**：
确保 KSP 版本与 Kotlin 版本匹配：
```kotlin
// build.gradle.kts
id("com.google.devtools.ksp") version "1.9.22-1.0.17"
```

### Q3: 构建时间过长

**优化建议**：
```properties
# gradle.properties
org.gradle.daemon=true
org.gradle.parallel=true
org.gradle.caching=true
org.gradle.configuration-cache=true
```

### Q4: APK 过大

**解决方案**：
1. 启用 R8 代码压缩：
```kotlin
buildTypes {
    release {
        isMinifyEnabled = true
        isShrinkResources = true
    }
}
```

2. 配置 ProGuard 规则（`proguard-rules.pro`）

### Q5: GitHub Actions 构建超时

**解决方案**：
- 检查网络问题
- 增加超时时间（当前设置为 30 分钟）
- 使用 Gradle 构建缓存

---

## 优化建议

### 1. 使用 Gradle 构建缓存

GitHub Actions 已自动缓存 Gradle 依赖，后续构建会更快。

### 2. 启用 R8 全模式

```kotlin
android {
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
        }
    }
}
```

### 3. 配置 ProGuard

确保 `proguard-rules.pro` 包含必要的保留规则：
```proguard
# 保留数据模型
-keep class com.fanhub.android.data.model.** { *; }

# 保留 Hilt
-keep class dagger.hilt.** { *; }
```

### 4. 使用 App Bundle

如果需要发布到 Google Play，建议使用 App Bundle：
```bash
./gradlew bundleRelease
```

---

## 发布流程

### 1. 更新版本号

编辑 `app/build.gradle.kts`：
```kotlin
defaultConfig {
    versionCode = 2
    versionName = "1.1.0"
}
```

### 2. 提交并推送

```bash
git add .
git commit -m "release: v1.1.0"
git push origin main
```

### 3. 创建 GitHub Release

1. 进入 **Releases** 标签页
2. 点击 **Create a new release**
3. 选择 Tag version（如 `v1.1.0`）
4. 填写发布说明
5. 上传 APK 文件（从 Actions Artifact 下载）

---

## 构建性能指标

| 指标 | 目标值 | 当前值 |
|------|--------|--------|
| 冷启动构建时间 | < 10 分钟 | ~8 分钟 |
| 增量构建时间 | < 2 分钟 | ~1 分钟 |
| APK 大小 (Debug) | < 50MB | ~45MB |
| APK 大小 (Release) | < 20MB | ~18MB |

---

## 持续集成检查清单

- [ ] 代码编译通过
- [ ] 单元测试通过
- [ ] Lint 检查通过
- [ ] APK 生成成功
- [ ] Artifact 上传成功
- [ ] 构建时间在可接受范围内

---

**最后更新**: 2026-03-05
