# GitHub 仓库设置指南

## 方法一：手动创建仓库（推荐）

### 1. 在GitHub上创建仓库

1. 访问 https://github.com/new
2. 输入仓库名称: `fanhub-android`
3. 选择 "Public" 或 "Private"
4. 点击 "Create repository"

### 2. 推送代码到GitHub

```bash
# 在本地项目目录中执行
cd /home/gmk/fanhub-android

# 添加远程仓库（替换YOUR_USERNAME为你的GitHub用户名）
git remote add origin https://github.com/YOUR_USERNAME/fanhub-android.git

# 推送代码
git branch -M main
git push -u origin main
```

### 3. 查看GitHub Actions构建

1. 访问 `https://github.com/YOUR_USERNAME/fanhub-android/actions`
2. 等待构建完成
3. 下载生成的APK文件

## 方法二：使用GitHub CLI

### 安装GitHub CLI

```bash
# Ubuntu/Debian
curl -fsSL https://cli.github.com/packages/githubcli-archive-keyring.gpg | sudo dd of=/usr/share/keyrings/githubcli-archive-keyring.gpg
sudo chmod go+r /usr/share/keyrings/githubcli-archive-keyring.gpg
echo "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/githubcli-archive-keyring.gpg] https://cli.github.com/packages stable main" | sudo tee /etc/apt/sources.list.d/github-cli.list > /dev/null
sudo apt update
sudo apt install gh

# 登录
ght auth login
```

### 创建仓库并推送

```bash
cd /home/gmk/fanhub-android

# 创建仓库
gh repo create fanhub-android --public --source=. --remote=origin --push
```

## 方法三：使用API创建仓库

### 获取GitHub Token

1. 访问 https://github.com/settings/tokens
2. 点击 "Generate new token (classic)"
3. 选择权限: `repo`
4. 生成token并复制

### 使用curl创建仓库

```bash
# 设置token
export GITHUB_TOKEN=your_token_here

# 创建仓库
curl -H "Authorization: token $GITHUB_TOKEN" \
     -H "Accept: application/vnd.github.v3+json" \
     -X POST \
     -d '{"name":"fanhub-android","description":"FunHub Android Client","private":false}' \
     https://api.github.com/user/repos

# 推送代码
git remote add origin https://github.com/YOUR_USERNAME/fanhub-android.git
git push -u origin main
```

## 构建状态检查

推送代码后，GitHub Actions会自动开始构建。你可以在以下位置查看：

- **Actions页面**: `https://github.com/YOUR_USERNAME/fanhub-android/actions`
- **构建状态**: 查看工作流运行状态
- **Artifacts**: 构建完成后下载APK文件

## 常见问题

### 1. 构建失败

如果构建失败，检查：
- 代码是否有语法错误
- 依赖是否正确配置
- 查看GitHub Actions日志获取详细错误信息

### 2. APK下载

构建成功后：
1. 进入Actions页面
2. 点击最新的工作流运行
3. 滚动到页面底部
4. 在Artifacts部分下载APK

### 3. 权限问题

如果遇到权限错误，确保：
- GitHub Token有repo权限
- 仓库设置中Actions已启用