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
    override val itemKeys = listOf("category_name", "category_amount", "id")
    override val itemTypes = listOf("TEXT", "LONG", "INT")
}

class AccountDB: Database {
    override val dbName = "accounts.db"
    override val dbVersion = 1
    override val tableName = "accounts"
    override val keyId = "accounts_id"
    override val itemKeys = listOf("account_name", "account_balance", "id")
    override val itemTypes = listOf("TEXT", "LONG", "INT")
}

class TransactionDB: Database {
    override val dbName = "transactions.db"
    override val dbVersion = 1
    override val tableName = "transactions"
    override val keyId = "transactions_id"
    override val itemKeys = listOf("transaction_amount", "transaction_vendor", "category_index", "account_index", "id")
    override val itemTypes = listOf("LONG", "TEXT", "INT", "INT", "INT")
}