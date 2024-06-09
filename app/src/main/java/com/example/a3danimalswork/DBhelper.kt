package com.example.a3danimalswork

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBhelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, "app", factory, 2) {  // Версия базы данных изменена на 2

    override fun onCreate(db: SQLiteDatabase?) {
        // Создание таблицы пользователей
        val userTableQuery =
            "CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, surname TEXT, email TEXT, pass TEXT)"
        db?.execSQL(userTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS users")
        onCreate(db)
    }

    fun addUser(user: User) {
        val values = ContentValues()
        values.put("name", user.name)
        values.put("surname", user.surname)
        values.put("email", user.email)
        values.put("pass", user.pass)

        val db = this.writableDatabase
        db.insert("users", null, values)
        db.close()
    }

    fun getUser(email: String, pass: String): Boolean {
        val db = this.readableDatabase
        val columns = arrayOf("id")
        val selection = "email = ? AND pass = ?"
        val selectionArgs = arrayOf(email, pass)

        val cursor: Cursor = db.query("users", columns, selection, selectionArgs, null, null, null)
        val cursorCount: Int = cursor.count
        cursor.close()

        return cursorCount > 0
    }

    fun getCurrentUser(): User? {
        val db = this.readableDatabase
        val columns = arrayOf("name", "surname", "email", "pass")
        val cursor = db.query("users", columns, null, null, null, null, null)

        var user: User? = null

        if (cursor != null && cursor.moveToFirst()) {
            val nameIndex = cursor.getColumnIndex("name")
            val surnameIndex = cursor.getColumnIndex("surname")
            val emailIndex = cursor.getColumnIndex("email")
            val passIndex = cursor.getColumnIndex("pass")

            if (nameIndex != -1 && surnameIndex != -1 && emailIndex != -1 && passIndex != -1) {
                val name = cursor.getString(nameIndex)
                val surname = cursor.getString(surnameIndex)
                val email = cursor.getString(emailIndex)
                val pass = cursor.getString(passIndex)
                user = User(name, surname, email, pass)
            }
        }

        cursor?.close()
        return user
    }
}
