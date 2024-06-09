package com.example.a3danimalswork

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(context: Context) {

    companion object {
        private const val PREF_NAME = "MyPrefs"
        private const val KEY_NAME = "name"
        private const val KEY_SURNAME = "surname"
        private const val KEY_EMAIL = "email"
        private const val KEY_PASS = "pass"
    }

    private val preferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun saveUser(user: User) {
        preferences.edit().apply {
            putString(KEY_NAME, user.name)
            putString(KEY_SURNAME, user.surname)
            putString(KEY_EMAIL, user.email)
            putString(KEY_PASS, user.pass)
            apply()
        }
    }

    fun getUser(): User? {
        val name = preferences.getString(KEY_NAME, null)
        val surname = preferences.getString(KEY_SURNAME, null)
        val email = preferences.getString(KEY_EMAIL, null)
        val pass = preferences.getString(KEY_PASS, null)

        return if (name != null && surname != null && email != null && pass != null) {
            User(name, surname, email, pass)
        } else {
            null
        }
    }

    fun clearUser() {
        preferences.edit().remove(KEY_NAME).remove(KEY_SURNAME).remove(KEY_EMAIL).remove(KEY_PASS).apply()
    }
}
