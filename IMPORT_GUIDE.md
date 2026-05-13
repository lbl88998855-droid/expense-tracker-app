# 如何导入和运行项目

## 方法一：使用 Android Studio（推荐）

### 1. 安装 Android Studio
- 下载地址：https://developer.android.com/studio
- 安装时选择标准安装

### 2. 导入项目
1. 打开 Android Studio
2. 点击 "Open" 或 "Open an Existing Project"
3. 选择 `expense-tracker-app` 文件夹
4. 等待 Gradle 同步完成（首次可能需要10-20分钟）

### 3. 配置 SDK
如果提示 SDK 缺失：
1. 点击 "File" → "Settings" (Windows) 或 "Preferences" (Mac)
2. 找到 "Android SDK"
3. 安装 SDK Platform (API 34) 和 Build Tools

### 4. 运行应用
1. 连接 Android 手机（开启USB调试）或启动模拟器
2. 点击运行按钮 ▶️
3. 选择设备，等待安装

## 方法二：命令行构建

### 1. 安装 Android SDK
- 下载 Command-line Tools：https://developer.android.com/studio#command-tools
- 解压到 `C:\Android\cmdline-tools`

### 2. 设置环境变量
```bash
# Windows
set ANDROID_HOME=C:\Android
set PATH=%PATH%;%ANDROID_HOME%\cmdline-tools\latest\bin

# Mac/Linux
export ANDROID_HOME=~/Android
export PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin
```

### 3. 安装 SDK
```bash
sdkmanager "platforms;android-34" "build-tools;34.0.0"
```

### 4. 构建项目
```bash
cd expense-tracker-app
./gradlew assembleDebug
```

生成的 APK 位置：
```
app/build/outputs/apk/debug/app-debug.apk
```

### 5. 安装到手机
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

## 常见问题

### 1. Gradle 同步失败
**解决方案**：
- 检查网络连接（需要访问 dl.google.com）
- 配置代理或使用国内镜像
- 在 `gradle.properties` 添加：
```properties
systemProp.http.proxyHost=proxy.example.com
systemProp.http.proxyPort=8080
```

### 2. 找不到设备
**解决方案**：
- 手机开启"开发者选项"和"USB调试"
- 允许电脑调试授权
- 重新插拔USB线

### 3. 编译错误
**解决方案**：
- 确保 JDK 版本 11 或更高
- 执行 `./gradlew clean` 清理后重新构建
- 检查 `build.gradle.kts` 中的版本号

## 下一步

构建成功后，你可以：
1. 测试应用功能
2. 根据需求修改UI和功能
3. 配置 Firebase 实现云端同步（参考 `setup-firebase.md`）
4. 打包发布版本

如有问题，欢迎随时询问！
