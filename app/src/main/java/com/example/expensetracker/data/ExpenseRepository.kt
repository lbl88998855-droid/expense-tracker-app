package com.example.expensetracker.data

import kotlinx.coroutines.flow.Flow
import java.util.Date

class ExpenseRepository(private val expenseDao: ExpenseDao) {
    
    val allExpenses: Flow<List<Expense>> = expenseDao.getAllExpenses()
    
    suspend fun insert(expense: Expense): Long {
        val id = expenseDao.insert(expense)
        // 同步到云端
        syncToCloud(expense.copy(id = id))
        return id
    }
    
    suspend fun update(expense: Expense) {
        expenseDao.update(expense)
        syncToCloud(expense)
    }
    
    suspend fun delete(expense: Expense) {
        expenseDao.delete(expense)
        deleteFromCloud(expense)
    }
    
    fun getExpensesByDateRange(startDate: Date, endDate: Date): Flow<List<Expense>> {
        return expenseDao.getExpensesByDateRange(startDate, endDate)
    }
    
    suspend fun getTotalAmount(startDate: Date, endDate: Date): Double? {
        return expenseDao.getTotalAmount(startDate, endDate)
    }
    
    suspend fun getCategoryTotals(startDate: Date, endDate: Date): Flow<List<CategoryTotal>> {
        return expenseDao.getCategoryTotals(startDate, endDate)
    }
    
    // 云端同步方法
    private suspend fun syncToCloud(expense: Expense) {
        // TODO: 实现Firebase同步
        // 示例代码：
        // val db = FirebaseFirestore.getInstance()
        // db.collection("expenses")
        //     .document(expense.id.toString())
        //     .set(expense)
        //     .addOnSuccessListener {
        //         // 标记已同步
        //     }
    }
    
    private suspend fun deleteFromCloud(expense: Expense) {
        // TODO: 从Firebase删除
    }
    
    suspend fun syncFromCloud() {
        // TODO: 从Firebase拉取数据
    }
}
