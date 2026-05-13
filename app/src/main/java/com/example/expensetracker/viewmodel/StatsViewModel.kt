package com.example.expensetracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.data.CategoryTotal
import com.example.expensetracker.data.ExpenseDatabase
import com.example.expensetracker.data.ExpenseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*

abstract class StatsViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository: ExpenseRepository
    
    init {
        val expenseDao = ExpenseDatabase.getDatabase(application).expenseDao()
        repository = ExpenseRepository(expenseDao)
    }
    
    fun getCategoryTotals(period: StatsPeriod): Flow<List<CategoryTotal>> {
        val calendar = Calendar.getInstance()
        val endDate = calendar.time
        
        calendar.add(Calendar.DAY_OF_YEAR, -period.days)
        val startDate = calendar.time
        
        return repository.getExpensesByDateRange(startDate, endDate)
            .map { expenses ->
                expenses.groupBy { it.category }
                    .map { (category, expenses) ->
                        CategoryTotal(category, expenses.sumOf { it.amount })
                    }
            }
    }
    
    fun getTotalAmount(period: StatsPeriod): Flow<Double> {
        val calendar = Calendar.getInstance()
        val endDate = calendar.time
        
        calendar.add(Calendar.DAY_OF_YEAR, -period.days)
        val startDate = calendar.time
        
        return repository.getTotalAmount(startDate, endDate)
            .map { it ?: 0.0 }
    }
}
