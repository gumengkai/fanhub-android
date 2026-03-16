#!/bin/bash

# GitHub仓库设置脚本
# 需要设置GITHUB_TOKEN环境变量

REPO_NAME="fanhub-android"
REPO_DESC="FunHub Android Client - A lightweight private media center Android app"

if [ -z "$GITHUB_TOKEN" ]; then
    echo "错误: 请设置GITHUB_TOKEN环境变量"
    echo "export GITHUB_TOKEN=your_token_here"
    exit 1
fi

# 创建GitHub仓库
echo "创建GitHub仓库: $REPO_NAME..."
curl -H "Authorization: token $GITHUB_TOKEN" \
     -H "Accept: application/vnd.github.v3+json" \
     -X POST \
     -d "{\"name\":\"$REPO_NAME\",\"description\":\"$REPO_DESC\",\"private\":false}" \
     https://api.github.com/user/repos

echo ""
echo "仓库创建完成！"
echo "现在可以推送代码:"
echo "git remote add origin https://github.com/$(git config user.name)/$REPO_NAME.git"
echo "git push -u origin master"