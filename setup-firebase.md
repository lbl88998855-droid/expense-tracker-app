# Firebase 配置指南

要启用云端同步功能，需要配置Firebase项目。

## 步骤

### 1. 创建Firebase项目
1. 访问 [Firebase Console](https://console.firebase.google.com/)
2. 点击"添加项目"
3. 输入项目名称，如"Expense Tracker"
4. 禁用Google Analytics（可选）
5. 点击"创建项目"

### 2. 添加Android应用
1. 在项目概览页面，点击"添加应用"，选择Android图标
2. 输入包名：`com.example.expensetracker`
3. 下载`google-services.json`文件
4. 将文件复制到项目的 `app/` 目录下

### 3. 启用Firestore数据库
1. 在Firebase Console左侧菜单，点击"Firestore Database"
2. 点击"创建数据库"
3. 选择"测试模式"（开发阶段）
4. 选择数据库位置（如`asia-northeast1`）

### 4. 配置安全规则
在Firestore Database的"规则"标签页，设置：
```
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /expenses/{document} {
      allow read, write: if true;  // 测试阶段，生产环境需要添加身份验证
    }
  }
}
```

### 5. 同步逻辑实现
在 `ExpenseRepository.kt` 中完成以下方法：

```kotlin
// 同步到云端
private suspend fun syncToCloud(expense: Expense) {
    val db = FirebaseFirestore.getInstance()
    val data = hashMapOf(
        "amount" to expense.amount,
        "description" to expense.description,
        "category" to expense.category,
        "date" to expense.date,
        "rawInput" to expense.rawInput,
        "createdAt" to expense.createdAt
    )
    
    db.collection("expenses")
        .document(expense.id.toString())
        .set(data)
        .addOnSuccessListener {
            // 标记已同步
            CoroutineScope(Dispatchers.IO).launch {
                expenseDao.markAsSynced(expense.id)
            }
        }
        .addOnFailureListener { e ->
            // 同步失败，稍后重试
            Log.e("FirebaseSync", "Sync failed", e)
        }
}

// 从云端删除
private suspend fun deleteFromCloud(expense: Expense) {
    val db = FirebaseFirestore.getInstance()
    db.collection("expenses")
        .document(expense.id.toString())
        .delete()
}

// 从云端同步
suspend fun syncFromCloud() {
    val db = FirebaseFirestore.getInstance()
    db.collection("expenses")
        .get()
        .addOnSuccessListener { documents ->
            for (document in documents) {
                val expense = document.toObject(Expense::class.java)
                // 插入本地数据库
                CoroutineScope(Dispatchers.IO).launch {
                    expenseDao.insert(expense)
                }
            }
        }
}
```

### 6. 构建运行
同步完成后，重新构建项目：
```bash
./gradlew clean
./gradlew assembleDebug
```

## 注意事项

- Firebase免费配额：Spark计划
  - Firestore: 1GB存储，50K读取/天，20K写入/天
  - 足够个人使用
  
- 生产环境需要添加用户身份验证（Firebase Auth）

- 建议添加网络状态检测，离线时只保存到本地

## 故障排除

如果遇到`google-services.json`相关错误：
1. 确认文件已放在正确位置（`app/google-services.json`）
2. 确认包名匹配
3. 在Firebase Console重新下载配置文件
