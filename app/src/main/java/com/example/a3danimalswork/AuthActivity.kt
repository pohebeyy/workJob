package com.example.a3danimalswork


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast


class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        val userLogin: EditText = findViewById(R.id.user_login_auth)
        val userPass: EditText = findViewById(R.id.user_pass_auth)
        val button: Button = findViewById(R.id.button_auth)
        val linkToReg: TextView = findViewById(R.id.textView7)
        val litkToForgot:TextView = findViewById(R.id.textView3)

        val preferenceManager = PreferenceManager(this)
        val currentUser = preferenceManager.getUser()
        if (currentUser != null) {
            val intent = Intent(this, MainScreen::class.java)
            startActivity(intent)
            finish()
        }
        litkToForgot.setOnClickListener {
            val intent = Intent(this,EmailActivity::class.java)
            startActivity(intent)

        }

        linkToReg.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        button.setOnClickListener{
            val email = userLogin.text.toString().trim()
            val pass = userPass.text.toString().trim()

            if(email == ""  || pass ==""){
                Toast.makeText(this,"Не все поля заполнены", Toast.LENGTH_LONG).show()
            } else {
                val db = DBhelper(this, null)
                val isAuth = db.getUser(email, pass)
                if (isAuth){
                    Toast.makeText(this,"Пользователь $email авторизован",Toast.LENGTH_LONG).show()
                    userLogin.text.clear()
                    userPass.text.clear()

                    val intent = Intent(this, MainScreen::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this,"Пользователь не найден",Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}