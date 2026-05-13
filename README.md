# 记账助手 Android App

一个通过聊天式输入来记录支出的安卓应用，支持本地和云端同步。

## 功能特性

✅ **聊天式输入** - 自然语言输入支出，如"午饭50元"
✅ **手动分类** - 选择支出分类（餐饮、交通、购物等）
✅ **本地存储** - 使用Room数据库本地保存
✅ **云端同步** - Firebase Firestore云端备份（待完成）
✅ **统计报表** - 按日/周/月/年统计支出
✅ **分类占比** - 查看各类支出占比

## 技术栈

- **语言**: Kotlin
- **UI**: Jetpack Compose
- **数据库**: Room (SQLite)
- **云端**: Firebase Firestore
- **图表**: MPAndroidChart

## 项目结构

```
app/
├── src/main/java/com/example/expensetracker/
│   ├── data/              # 数据层
│   │   ├── Expense.kt     # 数据模型
│   │   ├── ExpenseDao.kt  # 数据访问对象
│   │   ├── ExpenseDatabase.kt  # 数据库
│   │   └── ExpenseRepository.kt # 仓库层
│   ├── ui/
│   │   ├── screens/       # 界面
│   │   │   ├── ChatInputScreen.kt  # 聊天输入界面
│   │   │   └── StatsScreen.kt      # 统计报表界面
│   │   └── theme/         # 主题
│   ├── viewmodel/         # ViewModel
│   │   ├── ExpenseViewModel.kt
│   │   └── StatsViewModel.kt
│   ├── nlp/               # 自然语言处理
│   │   └── ExpenseParser.kt
│   └── MainActivity.kt    # 主Activity
```

## 安装使用

### 1. 克隆项目
```bash
git clone <repository-url>
cd expense-tracker-app
```

### 2. 配置Firebase（可选）
如果需要云端同步功能：
1. 在Firebase Console创建项目
2. 添加Android应用
3. 下载`google-services.json`放到`app/`目录
4. 启用Firestore数据库

### 3. 构建运行
使用Android Studio打开项目，点击运行按钮。

或者命令行构建：
```bash
./gradlew assembleDebug
```

## 使用说明

1. **添加支出**: 在输入框输入如"午饭50元"，点击发送
2. **选择分类**: 在弹出的对话框中选择分类
3. **查看统计**: 点击右上角"统计报表"查看支出统计
4. **切换周期**: 在统计页面切换本周/本月/本季/本年

## 待完成功能

- [ ] Firebase云端同步完整实现
- [ ] 饼图/柱状图可视化（MPAndroidChart集成）
- [ ] 支出记录编辑和删除
- [ ] 数据导出（CSV/Excel）
- [ ] 预算设置和提醒
- [ ] 多语言支持

## 许可证

MIT License

## 贡献

欢迎提交Issue和Pull Request！
