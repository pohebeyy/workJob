package com.example.a3danimalswork

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ForgotActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_forgot)
        val litkToForgot: Button = findViewById(R.id.button_auth)
        litkToForgot.setOnClickListener {
            val intent = Intent(this,MainScreen::class.java)
            startActivity(intent)

        }
    }
}