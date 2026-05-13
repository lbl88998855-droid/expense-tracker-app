package com.example.expensetracker.nlp

import java.util.regex.Pattern

data class ParseResult(
    val amount: Double,
    val description: String
)

object ExpenseParser {
    
    // 匹配金额的正则表达式
    private val amountPattern = Pattern.compile("""(\d+(\.\d{1,2})?)""")
    
    // 常见描述关键词
    private val descriptionKeywords = listOf(
        "午饭", "早餐", "晚餐", "午餐", "吃饭", "外卖",
        "打车", "地铁", "公交", "加油", "停车",
        "买", "购", "消费", "支出", "花费",
        "电影", "游戏", "娱乐", "咖啡", "奶茶"
    )
    
    fun parse(input: String): ParseResult? {
        // 提取金额
        val matcher = amountPattern.matcher(input)
        if (!matcher.find()) {
            return null  // 没有找到金额
        }
        
        val amount = matcher.group(1)?.toDoubleOrNull() ?: return null
        
        // 提取描述
        var description = input.substring(0, matcher.start()).trim()
        if (description.isEmpty()) {
            description = input.substring(matcher.end()).trim()
        }
        if (description.isEmpty()) {
            // 尝试从输入中提取关键词作为描述
            description = extractKeyword(input) ?: "未命名支出"
        }
        
        return ParseResult(amount, description)
    }
    
    private fun extractKeyword(input: String): String? {
        for (keyword in descriptionKeywords) {
            if (input.contains(keyword)) {
                return keyword
            }
        }
        return null
    }
    
    // 高级解析：支持更多格式
    fun advancedParse(input: String): ParseResult? {
        // 移除常见货币符号
        val cleanedInput = input.replace(Regex("[¥￥\$€]"), "")
        
        // 尝试多种金额格式
        val patterns = listOf(
            Pattern.compile("""(\d+(\.\d{1,2})?)元?"""),  // 50元 或 50
            Pattern.compile("""(\d+(\.\d{1,2})?)块?"""),  // 50块
            Pattern.compile("""(\d+(\.\d{1,2})?)刀"""),    // 50刀
        )
        
        for (pattern in patterns) {
            val matcher = pattern.matcher(cleanedInput)
            if (matcher.find()) {
                val amount = matcher.group(1)?.toDoubleOrNull() ?: continue
                val description = extractDescription(cleanedInput, matcher.start())
                return ParseResult(amount, description)
            }
        }
        
        return null
    }
    
    private fun extractDescription(input: String, amountStart: Int): String {
        val before = input.substring(0, amountStart).trim()
        return if (before.isNotEmpty()) {
            before
        } else {
            input.substring(amountStart).trim().let { after ->
                if (after.isEmpty()) "未命名支出" else after
            }
        }
    }
}
