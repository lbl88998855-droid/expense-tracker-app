package com.example.expensetracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val amount: Double,           // 金额
    val description: String,      // 描述
    val category: String,         // 分类
    val date: Date,              // 日期
    val rawInput: String,        // 原始输入文字
    val syncStatus: Boolean = false,  // 是否已同步到云端
    val createdAt: Date = Date() // 创建时间
)

// 预定义分类
object ExpenseCategories {
    val categories = listOf(
        "餐饮", "交通", "购物", "娱乐", "住房", 
        "医疗", "教育", "通讯", "其他"
    )
}
