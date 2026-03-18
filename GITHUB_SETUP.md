# GitHub 仓库设置指南

## 方法1: 手动创建仓库（推荐）

### 步骤1: 在 GitHub 创建仓库

1. 访问 https://github.com/new
2. 填写信息：
   - **Repository name**: `fanhub-android`
   - **Description**: `FunHub 私人媒体中心 Android 客户端`
   - **Visibility**: Public（或 Private）
   - ✅ 勾选 "Add a README file"（可选）
3. 点击 **Create repository**

### 步骤2: 推送本地代码

```bash
cd /home/gmk/.openclaw/workspace/fanhub-android

# 设置远程仓库地址
git remote set-url origin https://github.com/gumengkai/fanhub-android.git

# 推送代码
git push -u origin main
```

### 步骤3: 验证推送

访问 https://github.com/gumengkai/fanhub-android 查看代码是否已上传

---

## 方法2: 使用 GitHub CLI

如果安装了 `gh` 命令行工具：

```bash
# 登录 GitHub
gh auth login

# 创建仓库
gh repo create fanhub-android \
  --description "FunHub 私人媒体中心 Android 客户端" \
  --public \
  --source=. \
  --remote=origin \
  --push
```

---

## 方法3: 使用 Git Bundle 文件

如果无法直接推送，可以使用生成的 bundle 文件：

1. 下载文件：`fanhub-android.bundle` (位于项目根目录)
2. 在本地克隆：
   ```bash
   git clone fanhub-android.bundle fanhub-android
   ```
3. 然后按照方法1推送到 GitHub

---

## 上传 APK 文件

构建好的 APK 文件可以上传到 GitHub Releases：

1. 访问仓库页面
2. 点击右侧 **Releases**
3. 点击 **Create a new release**
4. 填写版本信息：
   - **Tag**: `v1.0.0`
   - **Title**: `FunHub Android v1.0.0`
   - **Description**: 复制下方内容
5. 上传 APK 文件
6. 点击 **Publish release**

### Release 描述模板

```markdown
## FunHub Android v1.0.0

### 新增功能
- 🔍 搜索功能 - 支持视频搜索、历史记录、热门推荐
- ⚙️ 增强设置 - 现代化UI、服务器连接测试
- 🎵 抖音风格播放器 - 垂直滑动、双击点赞

### 技术栈
- Kotlin + Jetpack Compose
- MVVM + Clean Architecture
- Hilt 依赖注入
- ExoPlayer 视频播放

### 下载
- [FunHub-v1.0.0-debug.apk](链接)

### 安装要求
- Android 8.0+ (API 26+)
- 需要配置 FunHub 服务器地址
```

---

## GitHub Actions 自动构建

推送代码后，GitHub Actions 会自动构建 APK：

1. 访问 https://github.com/gumengkai/fanhub-android/actions
2. 查看构建状态
3. 构建完成后，在 Artifacts 中下载 APK

---

## 常见问题

### Q: 推送时提示权限错误？
A: 需要使用 Personal Access Token 或配置 SSH key

### Q: 仓库已存在？
A: 先删除旧仓库，或使用 `git push --force` 强制推送

### Q: 如何生成 Personal Access Token？
A: 
1. 访问 https://github.com/settings/tokens
2. 点击 **Generate new token (classic)**
3. 勾选 `repo` 权限
4. 生成后复制 token
5. 推送时使用：`git push https://TOKEN@github.com/gumengkai/fanhub-android.git`
