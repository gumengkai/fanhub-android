# FanHub Android - Developer Guide

## 快速参考

### 技术栈
- **Kotlin**: 1.9.22
- **Gradle**: 8.4
- **AGP**: 8.2.2
- **Compose BOM**: 2024.01.00
- **Hilt**: 2.50
- **Min SDK**: 26
- **Target SDK**: 34

### 构建命令

```bash
# 清理构建
./gradlew clean

# 构建 Debug APK
./gradlew assembleDebug

# 构建 Release APK
./gradlew assembleRelease

# 运行单元测试
./gradlew testDebugUnitTest

# 运行所有测试
./gradlew test

# 检查代码风格
./gradlew ktlintCheck

# 格式化代码
./gradlew ktlintFormat
```

### 项目结构

```
app/src/main/java/com/fanhub/android/
├── data/                    # 数据层
│   ├── local/              # DataStore
│   ├── model/              # 数据模型
│   ├── remote/             # Retrofit API
│   └── repository/         # 仓库实现
├── di/                      # Hilt 模块
├── ui/                      # UI 层
│   ├── components/         # 可复用组件
│   ├── navigation/         # 导航
│   ├── screens/            # 页面
│   ├── theme/              # 主题
│   └── viewmodel/          # ViewModel
└── FanHubApplication.kt
```

### 架构模式

**MVVM + Clean Architecture**

```
UI Layer (Compose)
    ↓
ViewModel Layer (Hilt)
    ↓
Domain Layer (Use Cases)
    ↓
Data Layer (Repository)
    ↓
Source Layer (Remote + Local)
```

### 依赖注入

```kotlin
@HiltViewModel
class MyViewModel @Inject constructor(
    private val repository: MyRepository
) : ViewModel()
```

### StateFlow 模式

```kotlin
data class UiState(
    val data: MyData? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class MyViewModel @Inject constructor(
    private val repository: MyRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    
    fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.getData()
                .catch { e -> _uiState.update { it.copy(isLoading = false, error = e.message) } }
                .collect { data ->
                    _uiState.update { it.copy(isLoading = false, data = data) }
                }
        }
    }
}
```

### Compose Screen 模式

```kotlin
@Composable
fun MyScreen(
    viewModel: MyViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    MyScreenContent(
        uiState = uiState,
        onRefresh = viewModel::loadData,
        onNavigateBack = onNavigateBack
    )
}

@Composable
private fun MyScreenContent(
    uiState: UiState,
    onRefresh: () -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Title") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        // Content here
    }
}
```

### Result 封装

```kotlin
sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Error(val message: String) : Result<Nothing>
    data object Loading : Result<Nothing>
}
```

### 主题色

```kotlin
// FanHub 主题色
Pink = Color(0xFFFB7299)
PinkVariant = Color(0xFFE05A7D)
DarkPink = Color(0xFFD44D6E)
```

### 常用依赖

```kotlin
// Compose
implementation("androidx.compose.material3:material3")
implementation("androidx.navigation:navigation-compose:2.7.6")

// Hilt
implementation("com.google.dagger:hilt-android:2.50")
ksp("com.google.dagger:hilt-android-compiler:2.50")

// 网络
implementation("com.squareup.retrofit2:retrofit:2.9.0")
implementation("io.coil-kt:coil-compose:2.5.0")

// 媒体
implementation("androidx.media3:media3-exoplayer:1.2.0")
```

### 测试

```kotlin
@HiltAndroidTest
class MyViewModelTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)
    
    @Test
    fun `test load data`() = runTest {
        // Test implementation
    }
}
```

### 常见问题

**Q: 构建失败，提示 KSP 错误**
A: 确保 KSP 版本与 Kotlin 版本匹配：`1.9.22-1.0.17`

**Q: Compose 预览不显示**
A: 确保使用 `@Preview` 注解，并添加 `debugImplementation("androidx.compose.ui:ui-tooling")`

**Q: Hilt 注入失败**
A: 确保 Application 类有 `@HiltAndroidApp`，Activity 有 `@AndroidEntryPoint`

**Q: APK 过大**
A: 启用 R8: `isMinifyEnabled = true`，配置 ProGuard 规则

### 代码规范

- 使用 `val` 而非 `var`（不可变优先）
- 使用 `StateFlow` 而非 `LiveData`
- ViewModel 只暴露 `StateFlow`，不暴露 `MutableStateFlow`
- Composable 函数使用 `@Composable` 注解
- 私有 Composable 添加 `private` 修饰符
- 使用 `remember` 和 `rememberSaveable` 管理状态
- 避免在 Composable 中直接使用 `LaunchedEffect`，优先使用 ViewModel

### 资源链接

- [Android Developers](https://developer.android.com)
- [Compose Documentation](https://developer.android.com/jetpack/compose)
- [Material Design 3](https://m3.material.io)
- [Hilt Documentation](https://dagger.dev/hilt)
