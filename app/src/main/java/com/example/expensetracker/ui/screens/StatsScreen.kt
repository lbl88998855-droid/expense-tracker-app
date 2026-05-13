package com.example.expensetracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.expensetracker.data.CategoryTotal
import com.example.expensetracker.viewmodel.StatsViewModel
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(
    viewModel: StatsViewModel,
    onNavigateBack: () -> Unit
) {
    var selectedPeriod by remember { mutableStateOf(StatsPeriod.WEEK) }
    val categoryTotals by viewModel.getCategoryTotals(selectedPeriod).collectAsStateWithLifecycle(initialValue = emptyList())
    val totalAmount by viewModel.getTotalAmount(selectedPeriod).collectAsStateWithLifecycle(initialValue = 0.0)
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("统计报表") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Text("< 返回")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 时间周期选择
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatsPeriod.values().forEach { period ->
                        FilterChip(
                            selected = selectedPeriod == period,
                            onClick = { selectedPeriod = period },
                            label = { Text(period.displayName) }
                        )
                    }
                }
            }
            
            // 总金额显示
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "总支出",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "¥${String.format("%.2f", totalAmount)}",
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            
            // 饼图（需要集成MPAndroidChart）
            item {
                Text(
                    text = "分类占比",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                // 这里需要集成MPAndroidChart来显示饼图
                // 由于Compose集成复杂，可以先用列表代替
                CategoryStatsList(categoryTotals = categoryTotals)
            }
        }
    }
}

@Composable
fun CategoryStatsList(categoryTotals: List<CategoryTotal>) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categoryTotals.forEach { item ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = item.category,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "¥${String.format("%.2f", item.total)}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

enum class StatsPeriod(
    val displayName: String,
    val days: Int
) {
    WEEK("本周", 7),
    MONTH("本月", 30),
    QUARTER("本季", 90),
    YEAR("本年", 365)
}
