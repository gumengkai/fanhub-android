#!/bin/bash

# 项目检查脚本

echo "检查 FunHub Android 项目..."
echo ""

# 检查关键文件
echo "1. 检查关键文件..."
files=(
    "app/build.gradle.kts"
    "build.gradle.kts"
    "settings.gradle.kts"
    "gradle.properties"
    "gradlew"
    "app/src/main/AndroidManifest.xml"
    "app/src/main/java/com/funhub/MainActivity.kt"
    "app/src/main/java/com/funhub/App.kt"
    ".github/workflows/android.yml"
)

all_exist=true
for file in "${files[@]}"; do
    if [ -f "$file" ]; then
        echo "  ✓ $file"
    else
        echo "  ✗ $file (缺失)"
        all_exist=false
    fi
done

if [ "$all_exist" = false ]; then
    echo ""
    echo "错误: 部分关键文件缺失!"
    exit 1
fi

echo ""
echo "2. 统计代码..."
echo "  Kotlin文件数: $(find . -name '*.kt' | wc -l)"
echo "  Kotlin代码行数: $(find . -name '*.kt' -exec wc -l {} + 2>/dev/null | tail -1 | awk '{print $1}')"
echo "  XML文件数: $(find . -name '*.xml' | wc -l)"

echo ""
echo "3. 检查Git状态..."
if [ -d ".git" ]; then
    echo "  Git仓库: 已初始化"
    echo "  提交数: $(git rev-list --count HEAD 2>/dev/null || echo '0')"
else
    echo "  Git仓库: 未初始化"
fi

echo ""
echo "✓ 项目检查完成!"