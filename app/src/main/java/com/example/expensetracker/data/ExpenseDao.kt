package com.example.expensetracker.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface ExpenseDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expense: Expense): Long
    
    @Update
    suspend fun update(expense: Expense)
    
    @Delete
    suspend fun delete(expense: Expense)
    
    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAllExpenses(): Flow<List<Expense>>
    
    @Query("SELECT * FROM expenses WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getExpensesByDateRange(startDate: Date, endDate: Date): Flow<List<Expense>>
    
    @Query("SELECT category, SUM(amount) as total FROM expenses WHERE date BETWEEN :startDate AND :endDate GROUP BY category")
    fun getCategoryTotals(startDate: Date, endDate: Date): Flow<List<CategoryTotal>>
    
    @Query("SELECT SUM(amount) FROM expenses WHERE date BETWEEN :startDate AND :endDate")
    suspend fun getTotalAmount(startDate: Date, endDate: Date): Double?
    
    @Query("SELECT * FROM expenses WHERE syncStatus = 0")
    suspend fun getUnsyncedExpenses(): List<Expense>
    
    @Query("UPDATE expenses SET syncStatus = 1 WHERE id = :expenseId")
    suspend fun markAsSynced(expenseId: Long)
}

data class CategoryTotal(
    val category: String,
    val total: Double
)
