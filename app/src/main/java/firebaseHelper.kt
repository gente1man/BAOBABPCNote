package com.example.bar

import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.example.bar.CardLibrary

object FirebaseManager {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()

    // Функция для добавления записи
    fun addRecordToCurrentUser(cardLibrary: CardLibrary, recordName: String, context: Context) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val recordData = cardLibrary.exportToDatabase()
            val userRef = database.getReference("users").child(userId).child("cardLibraries")

            // Добавляем запись в Firebase с названием и данными из cardLibrary
            val recordMap = mutableMapOf<String, Any>(
                recordName to recordData
            )

            Toast.makeText(context, "Сохранено", Toast.LENGTH_SHORT).show()

            userRef.updateChildren(recordMap)
                .addOnSuccessListener {
                    // Логируем успешное добавление записи
                    println("Запись успешно добавлена для пользователя $userId")
                }
                .addOnFailureListener { e ->
                    // Логируем ошибку при добавлении записи
                    println("Ошибка при добавлении записи: ${e.message}")
                }
        } else {
            println("Ошибка: Пользователь не авторизован")
        }
    }

    // Функция для получения сборки по имени
    fun getRecordByName(recordName: String, callback: (Map<String, List<Component>>?) -> Unit) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val userRef = database.getReference("users").child(userId).child("cardLibraries").child(recordName)

            userRef.get()
                .addOnSuccessListener { snapshot ->
                    if (snapshot.exists()) {
                        // Получаем данные как Map<String, Any>
                        val recordData = snapshot.value as? Map<String, Any>
                        if (recordData != null) {
                            // Преобразуем данные в Map<String, List<Component>>
                            val componentsMap = mutableMapOf<String, List<Component>>()

                            for ((cardId, components) in recordData) {
                                if (components is List<*>) {
                                    val componentList = components.filterIsInstance<Map<String, Any>>().map { componentData ->
                                        // Создаем Component на основе данных из HashMap
                                        val name = componentData["name"] as? String ?: ""
                                        val link = componentData["link"] as? String ?: ""
                                        val price = componentData["price"] as? String ?: ""
                                        val type = ComponentType.valueOf(componentData["type"] as? String ?: "OTHER")
                                        val componentId = componentData["id"] as? String ?: ""

                                        Component(name, link, price, type, componentId)
                                    }
                                    componentsMap[cardId] = componentList
                                }
                            }

                            // Возвращаем данные в callback
                            callback(componentsMap)
                        } else {
                            println("Ошибка: Невозможно преобразовать данные из базы данных")
                            callback(null)
                        }
                    } else {
                        println("Ошибка: Запись с именем $recordName не найдена")
                        callback(null)
                    }
                }
                .addOnFailureListener { e ->
                    println("Ошибка при загрузке записи: ${e.message}")
                    callback(null)
                }
        } else {
            println("Ошибка: Пользователь не авторизован")
            callback(null)
        }
    }

}