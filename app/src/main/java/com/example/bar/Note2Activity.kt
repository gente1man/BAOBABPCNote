package com.example.bar

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.example.bar.databinding.ActivityNote2Binding
import com.google.firebase.database.FirebaseDatabase
import com.bumptech.glide.Glide
import com.example.bar.ui.reg.RegLogInActivity
import com.google.firebase.auth.FirebaseAuth

class Note2Activity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityNote2Binding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.v("USERINF", "EUI0")
        super.onCreate(savedInstanceState)
        binding = ActivityNote2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarNote2.toolbar)

        auth = FirebaseAuth.getInstance()
        val drawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView

        // Получаем userId из интента
        val userId = intent.getStringExtra("USER_ID")
        if (userId == null) {
            Toast.makeText(this, "Ошибка: отсутствует идентификатор пользователя", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Проверяем, авторизован ли пользователь
        val currentUser = auth.currentUser
        if (currentUser == null) {
            Toast.makeText(this, "Ошибка: пользователь не авторизован", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Инициализируем элементы бокового меню
        val headerView = navView.getHeaderView(0)
        val usernameTextView = headerView.findViewById<TextView>(R.id.username)
        val userIconImageView = findViewById<ImageView>(R.id.userIcon3)
        val defaultUserIconImageView = headerView.findViewById<ImageView>(R.id.ic_user)
        val navController = findNavController(R.id.nav_host_fragment_content_note2)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Загружаем юзернейм и аватар
        Log.v("USERINF", "EUI1")
        loadUserData(userId, usernameTextView, userIconImageView, defaultUserIconImageView)

        // Обработчик для открытия бокового меню
        val addIcon2 = findViewById<View>(R.id.addIcon2)
        addIcon2.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        fun showLogoutConfirmationDialog(context: Context) {
            // Инициализация диалога с кастомным дизайном
            val inflater = LayoutInflater.from(context)
            val dialogView = inflater.inflate(R.layout.logout, null)

            // Создание диалога без стандартных кнопок
            val dialog = AlertDialog.Builder(context)
                .setView(dialogView)
                .setCancelable(false)  // Делает диалог некликабельным в пустой области
                .create()

            // Находим кастомные кнопки
            val confirmButton = dialogView.findViewById<Button>(R.id.confirmButton)
            val cancelButton = dialogView.findViewById<Button>(R.id.cancelButton)

            // Устанавливаем логику для кнопки "Да"
            confirmButton.setOnClickListener {
                // Переход на активность с очищением стека
                val intent = Intent(context, RegLogInActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                context.startActivity(intent)

                // Закрываем диалог
                dialog.dismiss()
            }

            // Устанавливаем логику для кнопки "Отмена"
            cancelButton.setOnClickListener {
                dialog.dismiss() // Просто закрываем диалог
            }

            dialog.show() // Показываем кастомный диалог
        }

        val logout = findViewById<View>(R.id.menu_ic)
        logout.setOnClickListener {
            showLogoutConfirmationDialog(this)  // Показываем диалог подтверждения
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.note2, menu)
        return true
    }

    private fun loadUserData(userId: String, usernameTextView: TextView, userIconImageView: ImageView, userIconImageView2: ImageView) {
        Log.v("USERINF", "EUI2")
        val userRef = FirebaseDatabase.getInstance().getReference("users").child(userId)

        userRef.get().addOnSuccessListener { snapshot ->
            if (!snapshot.exists()) {
                Toast.makeText(this, "Пользователь не найден в базе данных", Toast.LENGTH_SHORT).show()
                return@addOnSuccessListener
            }

            // Получаем юзернейм
            val username = snapshot.child("username").getValue(String::class.java)
            usernameTextView.text = username ?: "User"

            // Получаем ссылку на аватар
            val avatarUrl = snapshot.child("avatar").getValue(String::class.java)
            if (!avatarUrl.isNullOrEmpty()) {
                Glide.with(this)
                    .load(avatarUrl) // Загружаем картинку по ссылке
                    .circleCrop()
                    .placeholder(R.drawable.ic_user) // Плейсхолдер при загрузке
                    .error(R.drawable.ic_user) // Дефолтная картинка при ошибке
                    .into(userIconImageView)

                Glide.with(this)
                    .load(avatarUrl)
                    .circleCrop()
                    .placeholder(R.drawable.ic_user)
                    .error(R.drawable.ic_user)
                    .into(userIconImageView2)
            } else {
                userIconImageView.setImageResource(R.drawable.ic_user)
            }
        }.addOnFailureListener {
            usernameTextView.text = "Ошибка загрузки"
            userIconImageView.setImageResource(R.drawable.ic_user)
            Toast.makeText(this, "Ошибка загрузки данных пользователя", Toast.LENGTH_SHORT).show()
        }
    }
}
