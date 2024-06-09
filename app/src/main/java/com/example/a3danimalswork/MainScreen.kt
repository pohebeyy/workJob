package com.example.a3danimalswork


import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.a3danimalswork.DBhelper
import com.example.a3danimalswork.User


class MainScreen : AppCompatActivity() {

    private lateinit var db: DBhelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)

        db = DBhelper(this, null)


        val logut:TextView = findViewById(R.id.textView2)
        val accountInfoLayout = findViewById<LinearLayout>(R.id.accountInfoLayout)
        val textViewAccountInfo: TextView = findViewById(R.id.textViewAccountInfo)
        val textViewEmail: TextView = findViewById(R.id.textViewEmail)
        val textViewEmailValue: TextView = findViewById(R.id.textViewEmailValue)
        val buttonChangePassword: Button = findViewById(R.id.buttonChangePassword)
        val buttonModel1: Button = findViewById(R.id.buttonPurchasedModel1)
        val buttonModel2: Button = findViewById(R.id.buttonPurchasedModel2)


        val user = db.getCurrentUser()
        if (user != null) {
            textViewAccountInfo.text = "Информация аккаунта"
            textViewEmail.text = "Email адрес:"
            textViewEmailValue.text = user.email
            accountInfoLayout.visibility = TextView.VISIBLE

            buttonChangePassword.setOnClickListener {
                // Логика смены пароля
            }

            // Проверяем статус подписки и обновляем доступные модели
            updateAvailableModels(user)
        }

        logut.setOnClickListener {
            val preferenceManager = PreferenceManager(this)
            preferenceManager.clearUser()
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK // Этот флаг закрывает все активити в стеке и создает новый стек для AuthActivity
            startActivity(intent)

            finish() // Закрываем текущую активити, чтобы пользователь не мог вернуться на нее нажатием кнопки "назад"
        }


        // Обработчики событий для кнопок перехода к 3D моделям
        buttonModel1.setOnClickListener {

                val intent = Intent(this,SceletonCatActivty::class.java)
                startActivity(intent)

        }

        buttonModel2.setOnClickListener {
            // Логика перехода ко второй модели
        }
    }
    private fun updateEmailProfile() {
        val dbHelper = DBhelper(this, null)
        val currentUser = dbHelper.getCurrentUser()

        if (currentUser != null) {
            val emailTextView = findViewById<TextView>(R.id.textViewEmail)
            emailTextView.text = "Email адрес: ${currentUser.email}"
        }
    }

    private fun updateAvailableModels(user: User?) {
        if (user != null && isSubscriptionActive(user)) {
            findViewById<Button>(R.id.buttonPurchasedModel2).visibility = Button.VISIBLE
            findViewById<TextView>(R.id.textViewAvailableModels).text = "Доступные 3D модели: Модель 1, Модель 2"
        } else {
            findViewById<Button>(R.id.buttonPurchasedModel2).visibility = Button.GONE
            findViewById<TextView>(R.id.textViewAvailableModels).text = "Доступные 3D модели: Модель 1"
        }
    }

    private fun isSubscriptionActive(user: User): Boolean {
        // Здесь логика проверки подписки в базе данных SQLite
        return true // Замените это на вашу реальную логику проверки подписки
    }
}