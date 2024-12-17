package com.example.bar.ui.reg

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bar.Note2Activity
import com.example.bar.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlin.random.Random

class RegLogInActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg_log_in) // Ваш лэйаут для логина

        // Инициализация Firebase
        auth = FirebaseAuth.getInstance()

        // Элементы интерфейса
        val emailField = findViewById<EditText>(R.id.editText)
        val passwordField = findViewById<EditText>(R.id.editTextTextPassword)
        val loginButton = findViewById<Button>(R.id.button3)
        val avatarField = findViewById<EditText>(R.id.ava) // Это поле для аватарки

        // Обработчик нажатия кнопки
        loginButton.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()
            val avatarUrl = avatarField.text.toString().trim() // Считываем URL аватарки

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginOrRegister(email, password, avatarUrl) // Добавляем avatarUrl
            } else {
                Toast.makeText(this, "Введите email и пароль", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loginOrRegister(email: String, password: String, avatarUrl: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Переход на Note2Activity
                    val intent = Intent(this, Note2Activity::class.java)
                    intent.putExtra("USER_ID", auth.currentUser?.uid)
                    startActivity(intent)
                    finish()

                    // Обновляем аватар, если нужно
                    updateAvatar(avatarUrl)
                } else {
                    // Если вход не удался, регистрируем нового пользователя
                    registerNewUser(email, password, avatarUrl)
                }
            }
    }

    private fun registerNewUser(email: String, password: String, avatarUrl: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Логиним пользователя сразу после регистрации
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { loginTask ->
                            if (loginTask.isSuccessful) {
                                // Генерация рандомного юзернейма
                                val randomUsername = generateRandomUsername()
                                val userId = auth.currentUser?.uid

                                // Запись в Realtime Database
                                val database = FirebaseDatabase.getInstance()
                                val userRef = database.getReference("users").child(userId!!)

                                // Создаем объект с юзернеймом и ссылкой на аватар
                                val userMap = mutableMapOf<String, Any>(
                                    "username" to randomUsername,
                                    "avatar" to avatarUrl
                                )

                                userRef.setValue(userMap)
                                    .addOnSuccessListener {
                                        Toast.makeText(this, "Учётная запись создана и пользователь вошел", Toast.LENGTH_SHORT).show()

                                        // Переход на Note2Activity
                                        val intent = Intent(this, Note2Activity::class.java)
                                        intent.putExtra("USER_ID", userId)
                                        startActivity(intent)
                                        finish()
                                    }
                                    .addOnFailureListener { e ->
                                        Log.e("FirebaseError", "Ошибка записи в БД", e)
                                        Toast.makeText(this, "Ошибка записи в БД: ${e.message}", Toast.LENGTH_LONG).show()
                                    }
                            } else {
                                Toast.makeText(this, "Ошибка входа: ${loginTask.exception?.message}", Toast.LENGTH_LONG).show()
                                Log.e("FirebaseLogin", "Ошибка входа после регистрации", loginTask.exception)
                            }
                        }
                } else {
                    Log.e("FirebaseAuth", "Ошибка создания пользователя", task.exception)
                    Toast.makeText(this, "Ошибка: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun updateAvatar(avatarUrl: String) {
        Log.e("AvatarUpdate", "Обновление аватара")
        val userId = auth.currentUser?.uid
        if (avatarUrl.isNotEmpty() && userId != null) {
            val database = FirebaseDatabase.getInstance()
            val userRef = database.getReference("users").child(userId)

            val userMap = mapOf("avatar" to avatarUrl)

            userRef.updateChildren(userMap) // Обновляем только аватар
                .addOnSuccessListener {
                    Log.d("AvatarUpdate", "Аватар успешно обновлен")
                }
                .addOnFailureListener { e ->
                    Log.e("AvatarUpdateError", "Ошибка обновления аватара", e)
                }
        }
    }

    private fun generateRandomUsername(): String {
        val chars = "abcdefghijklmnopqrstuvwxyz"
        return (1..5)
            .map { chars[Random.nextInt(chars.length)] }
            .joinToString("")
    }
}
