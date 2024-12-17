package com.example.bar

import android.os.Bundle
import android.view.Menu
import android.view.View
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

class Note2Activity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityNote2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNote2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_note2)

        // Настройка бокового меню
        navView.setupWithNavController(navController)

        // Добавляем обработчик нажатия на кнопку с id=addIcon2
        val addIcon2 = findViewById<View>(R.id.addIcon2)
        addIcon2.setOnClickListener {
            // Открытие бокового меню (Drawer)
            drawerLayout.openDrawer(GravityCompat.START) // Открывает меню с левой стороны
        }
    }
}
