package com.example.expensetracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.expensetracker.data.Expense
import com.example.expensetracker.data.ExpenseCategories
import com.example.expensetracker.nlp.ExpenseParser
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatInputScreen(
    viewModel: ExpenseViewModel,
    onNavigateToStats: () -> Unit
) {
    var inputText by remember { mutableStateOf("") }
    var showCategoryDialog by remember { mutableStateOf(false) }
    var parsedAmount by remember { mutableStateOf(0.0) }
    var parsedDescription by remember { mutableStateOf("") }
    
    val expenses by viewModel.allExpenses.collectAsStateWithLifecycle(initialValue = emptyList())
    
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // 顶部栏
        TopAppBar(
            title = { Text("记账助手") },
            actions = {
                TextButton(onClick = onNavigateToStats) {
                    Text("统计报表")
                }
            }
        )
        
        // 聊天记录列表
        LazyColumn(
            modifier = Modifier.weight(1f),
            reverseLayout = true,
            contentPadding = PaddingValues(16.dp)
        ) {
            items(expenses) { expense ->
                ExpenseCard(expense = expense)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        
        // 输入区域
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                label = { Text("输入支出信息，如：午饭50元") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Button(
                onClick = {
                    // 解析输入
                    val result = ExpenseParser.parse(inputText)
                    if (result != null) {
                        parsedAmount = result.amount
                        parsedDescription = result.description
                        showCategoryDialog = true
                    }
                }
            ) {
                Text("发送")
            }
        }
    }
    
    // 分类选择对话框
    if (showCategoryDialog) {
        CategorySelectionDialog(
            categories = ExpenseCategories.categories,
            onCategorySelected = { category ->
                // 保存记录
                val expense = Expense(
                    amount = parsedAmount,
                    description = parsedDescription,
                    category = category,
                    date = Date(),
                    rawInput = inputText
                )
                viewModel.insert(expense)
                inputText = ""
                showCategoryDialog = false
            },
            onDismiss = { showCategoryDialog = false }
        )
    }
}

@Composable
fun ExpenseCard(expense: Expense) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = expense.description,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = expense.category,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "¥${String.format("%.2f", expense.amount)}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun CategorySelectionDialog(
    categories: List<String>,
    onCategorySelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("选择分类") },
        text = {
            Column {
                categories.forEach { category ->
                    TextButton(
                        onClick = { onCategorySelected(category) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(category, modifier = Modifier.fillMaxWidth())
                    }
                }
            }
        },
        confirmButton = {}
    )
}
