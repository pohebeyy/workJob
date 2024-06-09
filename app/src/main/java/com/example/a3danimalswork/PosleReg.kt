package com.example.a3danimalswork

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PosleReg : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_posle_reg)
        val a:Button = findViewById(R.id.button3333)
        a.setOnClickListener {
            val intent = Intent(this,nav1::class.java)
            startActivity(intent)
            finish()
        }
    }
}