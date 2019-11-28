package com.example.budget_app.presenter

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.budget_app.model.Database
import java.util.ArrayList

class DatabaseHandler (context: Context, database: Database) :
    SQLiteOpenHelper(context, database.dbName, null, database.dbVersion) {

    private var tableName = database.tableName
    private var keyId = database.keyId
    private var itemKeys = database.itemKeys
    private var itemTypes = database.itemTypes

    override fun onCreate(db: SQLiteDatabase?) {
        var createTable = "CREATE TABLE $tableName ($keyId INTEGER PRIMARY KEY, "
        for(i in itemKeys.indices) {
            createTable += "${itemKeys[i]} ${itemTypes[i]}"
            if(i != itemKeys.size - 1) {
                createTable += ", "
            }
        }
        createTable += ")"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $tableName")
        onCreate(db)
    }

    fun create(items: List<Any>) {
        val db = writableDatabase
        val values = ContentValues()

        for(i in items.indices) {
            when (items[i]) {
                is String -> {
                    values.put(itemKeys[i], items[i] as String)
                }
                is Int -> {
                    values.put(itemKeys[i], items[i] as Int)
                }
            }
        }

        db.insert(tableName, null, values)
        db.close()
    }

    private fun readFromCursor(cursor: Cursor): List<Any> {
        val list = mutableListOf<Any>()
        for(i in itemTypes.indices) {
            if(itemKeys[i] == "id") {
                list.add(cursor.getInt(cursor.getColumnIndex(keyId)))
                break
            }
            when (itemTypes[i]) {
                "TEXT" -> {
                    list.add(cursor.getString(cursor.getColumnIndex(itemKeys[i])))
                }
                "INT" -> {
                    list.add(cursor.getInt(cursor.getColumnIndex(itemKeys[i])))
                }
            }
        }

        return list
    }

    fun read(id: Int): List<Any> {
        val db = readableDatabase
        val selectAll = "SELECT * FROM $tableName"
        val cursor = db.rawQuery(selectAll, null)
        cursor.moveToPosition(id)

        val list = readFromCursor(cursor)

        db.close()

        return list
    }

    fun readAll(): ArrayList<List<Any>> {
        val db = readableDatabase
        val list: ArrayList<List<Any>> = ArrayList()
        val selectAll = "SELECT * FROM $tableName"
        val cursor = db.rawQuery(selectAll, null)

        if(cursor.moveToFirst()) {
            do {
                list.add(readFromCursor(cursor))
            } while (cursor.moveToNext())
        }

        db.close()
        return list
    }

    fun update(id: Int, items: List<Any>): Int {
        val db = writableDatabase
        val values = ContentValues()

        for(i in items.indices) {
            when (items[i]) {
                is String -> {
                    values.put(itemKeys[i], items[i] as String)
                }
                is Int -> {
                    values.put(itemKeys[i], items[i] as Int)
                }
            }
        }
        val result = db.update(tableName, values, "$keyId=?", arrayOf(id.toString()))
        db.close()
        return result
    }

    fun delete(id: Int) {
        val db = writableDatabase
        db.delete(tableName, "$keyId=?", arrayOf(id.toString()))
        db.close()
    }

    fun getCount(): Int {
        val db = readableDatabase
        val countQuery = "SELECT * FROM $tableName"
        val cursor = db.rawQuery(countQuery, null)
        val count = cursor.count

        db.close()
        return count
    }
}
