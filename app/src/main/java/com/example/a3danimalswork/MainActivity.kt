package com.example.a3danimalswork

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userName: EditText = findViewById(R.id.user_user_auth)
        val userSurname: EditText = findViewById(R.id.user_fam_auth)
        val userEmail: EditText = findViewById(R.id.user_adress_auth)
        val userPass: EditText = findViewById(R.id.user_pass_auth)
        val button: Button = findViewById(R.id.button_auth)
        val g:ImageView = findViewById(R.id.svgImageView)


        val preferenceManager = PreferenceManager(this)
        val currentUser = preferenceManager.getUser()
        if (currentUser != null) {
            val intent = Intent(this, MainScreen::class.java)
            startActivity(intent)
            finish()
        }

      g.setOnClickListener{
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
            finish()
        }

        button.setOnClickListener{
            val name = userName.text.toString().trim()
            val surname = userSurname.text.toString().trim()
            val email = userEmail.text.toString().trim()
            val pass = userPass.text.toString().trim()

            if(name == "" || surname == "" || email == "" || pass ==""){
                Toast.makeText(this,"Не все поля заполнены", Toast.LENGTH_LONG).show()
            } else {
                val user = User(name, surname, email, pass)
                val db = DBhelper(this, null)
                db.addUser(user)

                preferenceManager.saveUser(user)

                Toast.makeText(this,"Пользователь $name добавлен",Toast.LENGTH_LONG).show()
                val intent = Intent(this,PosleReg::class.java)
                intent.putExtra("user", user) // Передача данных о пользователе на главный экран
                startActivity(intent)
                finish()
                userName.text.clear()
                userSurname.text.clear()
                userEmail.text.clear()
                userPass.text.clear()
            }
        }

        println(currentUser)
    }
}
