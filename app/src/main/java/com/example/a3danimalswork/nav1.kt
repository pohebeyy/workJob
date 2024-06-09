package com.example.a3danimalswork

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class nav1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_nav1)
        val next:Button = findViewById(R.id.button_auth)
        val skip:ImageView = findViewById(R.id.svgImageView)
        next.setOnClickListener {
            val intent = Intent(this,nav2::class.java)
            startActivity(intent)
            finish()
        }
        skip.setOnClickListener {
            val intent = Intent(this,MainScreen::class.java)
            startActivity(intent)
            finish()
        }
    }
}