package com.example.a3danimalswork

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SceletonCatActivty : AppCompatActivity() {
    private lateinit var glSurfaceView: MyGLSurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sceleton_cat_activty)

        glSurfaceView = findViewById(R.id.gl_surface_view)

        val button: Button = findViewById(R.id.button)
        button.setOnClickListener {
            glSurfaceView.moveModelToFixedPosition()
        }
    }
}
