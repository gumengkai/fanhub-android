#!/bin/bash
# GitHub 仓库创建脚本

set -e

REPO_NAME="fanhub-android"
DESCRIPTION="FunHub 私人媒体中心 Android 客户端"
USERNAME="gumengkai"

echo "========================================"
echo "FunHub Android GitHub 仓库创建工具"
echo "========================================"
echo ""

# 检查是否有 GitHub Token
if [ -z "$GITHUB_TOKEN" ]; then
    echo "⚠️  未设置 GITHUB_TOKEN 环境变量"
    echo ""
    echo "请访问 https://github.com/settings/tokens 生成 Personal Access Token"
    echo "然后设置环境变量: export GITHUB_TOKEN=your_token_here"
    echo ""
    echo "或者手动创建仓库:"
    echo "  1. 访问 https://github.com/new"
    echo "  2. 仓库名: $REPO_NAME"
    echo "  3. 点击 Create repository"
    echo "  4. 运行: git push -u origin main"
    echo ""
    exit 1
fi

echo "📦 创建仓库: $REPO_NAME"
echo ""

# 创建仓库
curl -s -X POST \
  -H "Authorization: token $GITHUB_TOKEN" \
  -H "Accept: application/vnd.github.v3+json" \
  https://api.github.com/user/repos \
  -d "{
    \"name\": \"$REPO_NAME\",
    \"description\": \"$DESCRIPTION\",
    \"private\": false,
    \"auto_init\": false
  }" | grep -E '"message"|"html_url"' || true

echo ""
echo "🔗 设置远程仓库..."
cd "$(dirname "$0")"
git remote remove origin 2>/dev/null || true
git remote add origin "https://github.com/$USERNAME/$REPO_NAME.git"

echo ""
echo "📤 推送代码到 GitHub..."
git push -u origin main || {
    echo ""
    echo "❌ 推送失败，尝试使用 token 推送..."
    git push "https://$GITHUB_TOKEN@github.com/$USERNAME/$REPO_NAME.git" main
}

echo ""
echo "✅ 完成！"
echo ""
echo "仓库地址: https://github.com/$USERNAME/$REPO_NAME"
echo ""
echo "下一步:"
echo "  1. 访问仓库查看代码"
echo "  2. 在 Actions 中查看自动构建"
echo "  3. 创建 Release 上传 APK"
