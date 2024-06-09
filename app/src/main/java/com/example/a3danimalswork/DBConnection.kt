package com.example.a3danimalswork

import android.provider.ContactsContract.CommonDataKinds.Website.URL
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

object DBConnection {

    private const val JDBC_URL = "jdbc:postgresql://94.190.88.79/postgres"
    private const val USER = "postgres"
    private const val PASSWORD = "123"

    fun getConnection(): Connection? {
        return try {
            DriverManager.getConnection(URL, USER, PASSWORD).also {
                println("Connection established") // Для отладки
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            null
        }
    }
}