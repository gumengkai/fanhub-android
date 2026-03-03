# 🚀 在线编译部署指南

## 方式一：GitHub Actions（推荐）

### 步骤 1：创建 GitHub 仓库

```bash
cd /home/gmk/.openclaw/workspace/fanhub-android

# 初始化 Git（如果还没有）
git init

# 添加所有文件
git add .

# 提交
git commit -m "Initial commit: FanHub Android"
```

### 步骤 2：推送到 GitHub

```bash
# 添加远程仓库（替换为你的仓库地址）
git remote add origin https://github.com/你的用户名/fanhub-android.git

# 推送
git push -u origin main
```

### 步骤 3：触发编译

1. 打开 GitHub 仓库页面
2. 点击 **Actions** 标签
3. 点击左侧 **Android Build**
4. 点击 **Run workflow** 按钮
5. 等待 5-10 分钟

### 步骤 4：下载 APK

编译完成后：

1. 在 Actions 页面点击最近的构建记录
2. 滚动到页面底部 **Artifacts** 部分
3. 点击 **fanhub-android-debug** 下载 APK

---

## 方式二：创建 Release 版本

### 打标签推送

```bash
# 创建版本标签
git tag v1.0.0

# 推送标签
git push origin v1.0.0
```

GitHub Actions 会自动构建 Release 版本。

---

## ⚙️ 配置后端地址

### 方案 A：修改默认地址

编辑 `app/build.gradle.kts` 第 22 行：

```kotlin
buildConfigField("String", "DEFAULT_API_URL", "\"http://你的服务器 IP:8080/api\"")
```

### 方案 B：使用 GitHub Secrets

1. 在 GitHub 仓库 → Settings → Secrets and variables → Actions
2. 添加 Secret：`BACKEND_API_URL`
3. 修改 workflow 文件使用变量

---

## 📱 安装 APK

### 通过 USB 安装

```bash
adb install app-debug.apk
```

### 通过文件传输

1. 下载 APK 文件
2. 传输到 Android 设备
3. 在设备上打开 APK 安装
4. 如果提示"未知来源"，允许安装

---

## 🔧 故障排查

### 编译失败

检查 GitHub Actions 日志，常见问题：

- **JDK 版本**：确保使用 JDK 17
- **内存不足**：Actions 默认 7GB RAM，应该够用
- **依赖下载失败**：检查网络连接

### APK 无法安装

- 确保允许"未知来源"安装
- 检查 Android 版本（最低 Android 7.0）
- 尝试 Debug 版本而非 Release

---

## 📊 构建时间

- 首次构建：8-12 分钟（下载依赖）
- 后续构建：3-5 分钟（缓存命中）

---

## 🎯 下一步

1. 推送代码到 GitHub
2. 运行 Actions 工作流
3. 下载并测试 APK
4. 反馈问题，继续完善功能
