package com.example.budget_app.model

interface Database {
    val dbName: String
    val dbVersion: Int
    val tableName: String
    val keyId: String
    val itemKeys: List<String>
    val itemTypes: List<String>
}

class CategoryDB: Database {
    override val dbName = "categories.db"
    override val dbVersion = 1
    override val tableName = "categories"
    override val keyId = "categories_id"
    override val itemKeys = listOf("category_name", "category_amount")
    override val itemTypes = listOf("TEXT", "INT")
}